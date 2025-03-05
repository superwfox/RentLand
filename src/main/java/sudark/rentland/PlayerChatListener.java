package sudark.rentland;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;

public class PlayerChatListener implements Listener {
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) throws URISyntaxException {
        Player pl = e.getPlayer();

        if (!pl.hasMetadata("RentLand")) return;

        if (pl.getMetadata("RentLand").size() == 2) {
            if (e.getMessage().equals("票据")) {
                String msg = "RENTLAND:\n" +
                        "==============\n" +
                        "地主：" + pl.getName() + "\n" +
                        "面积：" + ((int[]) (pl.getMetadata("RentLand").get(0).value()))[0] + "\n" +
                        "租期：" + ((int[]) (pl.getMetadata("RentLand").get(0).value()))[1] + "周\n" +
                        "==============\n" +
                        "[" + pl.getMetadata("RentLand").get(1).asString() + "]";

                OneBotClient ws = new OneBotClient(new URI("ws://localhost:3001"));
                ws.sendG(msg);
                ws.at(DataSniffer.findQQ(pl.getUniqueId().toString()));
            }
        }

        int weeks = -1;
        try {
            weeks = Integer.parseInt(e.getMessage());
        } catch (NumberFormatException ex) {
            pl.sendMessage("非阿拉伯数字 已取消");
            pl.sendMessage("=================");
            pl.sendMessage("已保存圈地信息 用书本点击可继续");
            pl.removeMetadata("RentLand", Bukkit.getPluginManager().getPlugin("RentLand"));
        }

        if (weeks < 1) {
            pl.sendMessage("?");
            return;
        }

        int[] valves = (int[]) pl.getMetadata("RentLand").get(0).value();
        int area = valves[0];
        int x1 = valves[1];
        int z1 = valves[2];
        int X1 = valves[3];
        int Z1 = valves[4];

        int level = pl.getLevel();

        if (area / 100 * weeks > level) {
            pl.sendMessage("等级不足以支付 已取消");
            pl.sendMessage("=================");
            pl.sendMessage("已保存圈地信息 用书本点击可继续");
            return;
        }

        pl.setLevel(level - area / 100 * weeks);
        pl.sendMessage("");
        success(pl, area, weeks);

        pl.setMetadata("RentLand", new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("RentLand"), new int[]{area, weeks}));
        List<List<String>> data = FileManager.readCSV(FileManager.landFile);
        data.add(List.of(weeks * 7 + "", "null", "" + x1, "" + X1, "" + z1, "" + Z1, pl.getUniqueId().toString()));
        FileManager.writeCSV(FileManager.landFile, data);
    }

    public void success(Player pl, int area, int time) {
        ItemStack property = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta propertyMeta = property.getItemMeta();
        propertyMeta.setLore(List.of("§r§e地产证"));
        property.setItemMeta(propertyMeta);

        LocalDate date = LocalDate.now();
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();
        String t = "" + year + "/" + month + "/" + day;
        pl.getMetadata("RentLand").add(new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("RentLand"), t));

        pl.sendMessage("§e§l" + pl.getName() + " §r§f同志，您于§b" + t + "§f租赁土地§e " + area + " §f格，租期§e " + time * 7 + " §f天");
        pl.sendMessage("§7需要在群内发送票据，请在游戏聊天发送“票据”");

        pl.sendTitle("[§e租赁成功§f]", "§7QQ群与§b南国总督§7私聊发送LAND开启消息通知", 20, 100, 40);
    }
}
