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

import static sudark.rentland.PurChaseListener.Tloc;

public class PlayerChatListener implements Listener {
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) throws URISyntaxException {
        Player pl = e.getPlayer();

        if (!pl.hasMetadata("RentLand")) return;


        if (pl.getMetadata("RentLand").size() == 3) {
            String[] strs = pl.getMetadata("RentLand").get(0).asString().split("//|");
            String date = pl.getMetadata("RentLand").get(1).asString();
            String time = pl.getMetadata("RentLand").get(2).asString();
            if (e.getMessage().equals("票据")) {
                String msg = "RENTLAND:\n" +
                        "==============\n" +
                        "地主：" + pl.getName() + "\n" +
                        "面积：" + strs[0] + "\n" +
                        "租期：" + time + "周\n" +
                        "==============\n" +
                        "[" + date + "]";

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
            pl.sendMessage("你现在可以重新圈地了");
            Tloc.remove(pl);
            pl.removeMetadata("RentLand", Bukkit.getPluginManager().getPlugin("RentLand"));
        }

        if (weeks < 1) {
            pl.sendMessage("[§e领地§f] ? 数字不合法");
            return;
        }

        String[] strs = pl.getMetadata("RentLand").get(0).asString().split(",");

        int area = Integer.parseInt(strs[0]);
        int x1 = Integer.parseInt(strs[1]);
        int X1 = Integer.parseInt(strs[2]);
        int z1 = Integer.parseInt(strs[3]);
        int Z1 = Integer.parseInt(strs[4]);

        int level = pl.getLevel();

        if (area / 100 * weeks > level) {
            pl.sendMessage("[§e领地§f] 等级不足以支付 请重试");
            return;
        }

        pl.setLevel(level - area / 100 * weeks);
        success(pl, area, weeks);

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
        String t = year + "/" + month + "/" + day;

        pl.getMetadata("RentLand").add(new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("RentLand"), t));
        pl.getMetadata("RentLand").add(new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("RentLand"), time));

        pl.sendMessage("§e§l" + pl.getName() + " §r§f同志，您于§b" + t + "§f租赁土地§e " + area + " §f格，租期§e " + time * 7 + " §f天");
        pl.sendMessage("§7需要在群内发送票据，请在游戏聊天发送“票据”");

        pl.sendTitle("[§e租赁成功§f]", "§7QQ群与§b南国总督§7私聊发送LAND开启消息通知", 20, 100, 40);
    }
}
