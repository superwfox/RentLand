package sudark.rentland;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static sudark.rentland.DataSniffer.getPlayerByQQ;
import static sudark.rentland.DataSniffer.getQQ;

public class LandNotice implements Listener {

    public static List<String> qqs = new ArrayList<>();
    public static List<String> uuids = new ArrayList<>();
    public static List<String> landIDs = new ArrayList<>();
    static BukkitTask task = null;
    static BukkitTask task2 = null;

    public static void ask(Player pl) {
        String[] strs = pl.getMetadata("invader").get(0).asString().split("\\|");
        String qq = strs[0];
        String name = strs[1];
        String landID = strs[2];

        Player p = getPlayerByQQ(qq);

        if (p != null) {
            p.sendMessage("  [§e领地§f] §7" + pl.getName() + "正在" + name + "闲逛，是否给予权限\n   .回答“§b是§7”确认，否则请忽略此消息");
            p.setMetadata("RightManage", new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("RentLand"), getQQ(pl) + "|" + landID));

            task = new BukkitRunnable() {
                @Override
                public void run() {
                    p.removeMetadata("RightManage", Bukkit.getPluginManager().getPlugin("RentLand"));
                    p.sendMessage("[§e领地§f] 已自动拒绝");
                }
            }.runTaskLater(Bukkit.getPluginManager().getPlugin("RentLand"), 300);

            return;
        }

        String[] msgs = {
                "有玩家进入领地，请注意。",
                "玩家已进入，请留意动向。",
                "领地有玩家进入，请关注。",
                "注意！有玩家进入领地。",
                "其他玩家进入，请保持警惕。"
        };
        Random rd = new Random();
        String msg = msgs[rd.nextInt(msgs.length)];

        pl.sendMessage("[§e领地§f] 您当前位于他人领地，发送“询问”向领地主询求权限");
        OneBotClient.sendP(qq, msg);
    }


    @EventHandler
    public void onPlayerTalk(AsyncPlayerChatEvent e) {
        Player pl = e.getPlayer();

        if ((!pl.hasMetadata("invader")) && (!pl.hasMetadata("RightManage"))) return;

        if (e.getMessage().equals("询问")) {

            e.setCancelled(true);
            pl.sendMessage("[§e领地§f] 请等待地主回复");
            String[] invader = pl.getMetadata("invader").get(0).asString().split("\\|");
            String qq = invader[0];
            String name = invader[1];
            String landID = invader[2];
            ask2(qq, pl, name);
            qqs.add(qq);
            uuids.add(getQQ(pl));
            landIDs.add(landID);

            task2 = new BukkitRunnable() {
                @Override
                public void run() {
                    qqs.remove(qq);
                    uuids.remove(getQQ(pl));
                    landIDs.remove(landID);
                    pl.sendMessage("[§e领地§f] 地主没有对您的请求做出回复");
                }
            }.runTaskLater(Bukkit.getPluginManager().getPlugin("RentLand"), 300);
        }

        if (pl.hasMetadata("RightManage") && e.getMessage().equals("是")) {
            e.setCancelled(true);

            String[] strs = pl.getMetadata("RightManage").get(0).asString().split("\\|");
            String qq = strs[0];
            String landID = strs[1];
            List<List<String>> data = FileManager.readCSV(sudark.rentland.FileManager.landFile);

            for (List<String> row : data) {

                int x = Integer.parseInt(row.get(2));
                int y = Integer.parseInt(row.get(4));

                String LandID = x + "," + y;

                if (LandID.equals(landID)) {

                    row.add(qq);
                    pl.removeMetadata("RightManage", Bukkit.getPluginManager().getPlugin("RentLand"));
                    Player p = getPlayerByQQ(qq);

                    if (p != null) {
                        pl.sendMessage("[§e领地§f] 已为[ §e" + qq + "§f ](§b" + p.getName() + "§f)添加权限");
                        p.sendMessage("[§e领地§f] 您已获得该领地权限");
                    }
                    task.cancel();
                    break;
                }
            }

            FileManager.writeCSV(sudark.rentland.FileManager.landFile, data);
        }
    }

    public static void ask2(String qq, Player pl, String name) {
        OneBotClient.sendP(qq, pl.getName() + "请求进入领地" + name + "，回复“允许”来同意，否则忽略这条消息");
    }
}
