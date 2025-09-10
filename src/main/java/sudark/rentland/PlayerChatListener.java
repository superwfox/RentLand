package sudark.rentland;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;;

import java.time.LocalDate;
import java.util.List;

import static sudark.rentland.DataSniffer.getQQ;
import static sudark.rentland.PurChaseListener.*;
import static sudark.rentland.RentLand.BotName;

public class PlayerChatListener implements Listener {
    public void cancel(Player pl) {
        Tloc.remove(pl);
        showLand.cancel();
        showLand2.cancel();
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player pl = e.getPlayer();

        if (!pl.hasMetadata("RentLand")) return;

        e.setCancelled(true);

        int weeks = -1;
        try {
            weeks = Integer.parseInt(e.getMessage());
        } catch (NumberFormatException ex) {
            pl.sendMessage("[§e领地§f] 非阿拉伯数字 已取消");
            pl.sendMessage("§7  你现在可以重新圈地了");
            cancel(pl);
            HandlerList.unregisterAll(this);
            pl.removeMetadata("RentLand", Bukkit.getPluginManager().getPlugin("RentLand"));
            return;
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

        cancel(pl);
        HandlerList.unregisterAll(this);

        TickPrinter tp = new TickPrinter();
        Bukkit.getPluginManager().registerEvents(tp, RentLand.getPlugin(RentLand.class));
        Bukkit.getScheduler().runTaskLater(RentLand.getPlugin(RentLand.class), () -> HandlerList.unregisterAll(tp), 6000L);

        List<List<String>> data = FileManager.readCSV(FileManager.landFile);
        data.add(List.of(weeks * 7 + "", "null", "" + x1, "" + X1, "" + z1, "" + Z1, getQQ(pl)));
        FileManager.writeCSV(FileManager.landFile, data);
    }

    private class TickPrinter implements Listener {
        @EventHandler
        public void onPlayerChat(AsyncPlayerChatEvent e) {
            Player pl = e.getPlayer();

            if (pl.hasMetadata("LandTip")) {
                String[] strs = pl.getMetadata("LandTip").get(0).asString().split(",");

                if (e.getMessage().equals("票据")) {
                    String msg = "RENTLAND:\n" +
                            "==============\n" +
                            "地主：" + pl.getName() + "\n" +
                            "面积：" + strs[0] + "\n" +
                            "租期：" + strs[1] + "周\n" +
                            "==============\n" +
                            "[" + strs[2] + "]";

                    OneBotClient.sendG(msg);
                    OneBotClient.at(getQQ(pl));
                    pl.removeMetadata("LandTip", Bukkit.getPluginManager().getPlugin("RentLand"));
                    HandlerList.unregisterAll(this);
                }
            }
        }
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

        pl.removeMetadata("RentLand", Bukkit.getPluginManager().getPlugin("RentLand"));
        pl.setMetadata("LandTip", new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("RentLand"), area + "," + time + "," + t));

        pl.sendMessage("§e§l" + pl.getName() + " §r§f同志，您于§b" + t + "§f租赁土地§e " + area + " §f格，租期§e " + time * 7 + " §f天");
        pl.sendMessage("§7需要在群内发送票据，请在游戏聊天发送“票据”");

        pl.sendTitle("[§e租赁成功§f]", "§7QQ群与§b" + BotName + "§7私聊发送LAND开启消息通知", 20, 100, 40);
        pl.getInventory().addItem(property);
    }
}
