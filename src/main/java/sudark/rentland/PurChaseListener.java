package sudark.rentland;

import it.unimi.dsi.fastutil.Pair;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PurChaseListener implements Listener {

    ConcurrentHashMap<Player, Pair<Location, Location>> Tloc = new ConcurrentHashMap<>();
    static BukkitTask showLand;
    static BukkitTask showLand2;

    @EventHandler
    public void onPurchase(PlayerInteractEvent e) {
        Player pl = e.getPlayer();
        if (!(pl.getItemInHand().getType() == Material.WRITABLE_BOOK && e.getAction().equals(Action.LEFT_CLICK_BLOCK)))
            return;

        if (!Tloc.contains(pl)) {
            Tloc.put(pl, Pair.of(null, null));

            pl.sendTitle("[§e圈定模式§f]", "§7点击方块来确定领地对角");
            pl.playSound(pl.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_HIT, 1, 1);

            return;
        }


        if (Tloc.contains(pl) && Tloc.get(pl).left() == null) {
            Tloc.put(pl, Pair.of(e.getClickedBlock().getLocation(), null));

            pl.sendTitle("[§e圈定模式§f]", "§7还需要确认一个角落");
            pl.playSound(pl.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_HIT, 1, 1);

            return;
        }

        if (Tloc.contains(pl) && Tloc.get(pl).right() == null) {
            Tloc.put(pl, Pair.of(Tloc.get(pl).left(), e.getClickedBlock().getLocation()));

            pl.sendTitle("[§e圈定模式§f]", "§7圈定完成，请等待计算结果");
            pl.playSound(pl.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1, 1);

        }

        Location loc1 = Tloc.get(pl).left();
        Location loc2 = Tloc.get(pl).right();

        if (checkPosition(loc1, loc2, pl)) {

            showParticle(loc1, loc2, pl);

            calculate(loc1, loc2, pl);

            Bukkit.getPluginManager().registerEvents(new PlayerChatListener(), Bukkit.getPluginManager().getPlugin("RentLand"));

        }


    }

    public void calculate(Location loc1, Location loc2, Player pl) {
        int x1 = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int z1 = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        int X1 = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int Z1 = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

        int width = X1 - x1;
        int length = Z1 - z1;

        int area = width * length;

        pl.sendMessage("|| 面积: §e§l" + area + "§r§f ||\n");
        pl.sendMessage("===============");
        pl.sendMessage("|| §b发送您的租赁周数§f ||");
        pl.sendMessage("===============");
        pl.sendMessage("'租赁一周需要§e" + area / 100 + "§f等级'");
        pl.sendMessage("当前最多可购买§b" + pl.getLevel() / (area / 100) + "§f周");
        pl.sendMessage("§7 ( 发送阿拉伯数字，其他字符来取消 )");

        pl.setMetadata("RentLand", new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("RentLand"), new int[]{area, x1, z1, X1, Z1}));
    }

    public static void showParticle(Location loc1, Location loc2, Player pl) {
        pl.sendTitle("[§e圈地成功§f]", "§7选择心怡的租赁时长，稍后可更改");
        int x1 = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int z1 = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        int X1 = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int Z1 = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

        showLand = new BukkitRunnable() {
            int x = x1;
            int z = z1;

            int x2 = x1;
            int z2 = z1;

            @Override
            public void run() {
                Location loc = new Location(pl.getWorld(), x, pl.getWorld().getHighestBlockYAt(x, z), z);
                pl.getWorld().spawnParticle(Particle.SONIC_BOOM, loc, 2, 0, 0, 0, 0);
                Location locT = new Location(pl.getWorld(), x2, pl.getWorld().getHighestBlockYAt(x2, z2), z2);
                pl.getWorld().spawnParticle(Particle.SONIC_BOOM, locT, 2, 0, 0, 0, 0);

                if (x < X1) x++;
                if (x == X1 && z < Z1) z++;
                if (x == X1 && z == Z1) {
                    x = x1;
                    z = z1;
                }

                if (z2 < Z1) z2++;
                if (z2 == Z1 && x2 < X1) x2++;
                if (z2 == Z1 && x2 == X1) {
                    x2 = x1;
                    z2 = z1;
                }

            }
        }.runTaskTimer(Bukkit.getPluginManager().getPlugin("RentLand"), 0, 1);

        showLand2 = new BukkitRunnable() {
            @Override
            public void run() {
                for (int x = x1; x <= X1; x++) {
                    Location loc = new Location(pl.getWorld(), x, pl.getWorld().getHighestBlockYAt(x, z1), z1);
                    Location locT = new Location(pl.getWorld(), x, pl.getWorld().getHighestBlockYAt(x, Z1), Z1);
                    pl.getWorld().spawnParticle(Particle.END_ROD, loc, 1, 0, 0, 0, 0);
                    pl.getWorld().spawnParticle(Particle.END_ROD, locT, 1, 0, 0, 0, 0);
                }
                for (int z = z1; z <= Z1; z++) {
                    Location loc = new Location(pl.getWorld(), x1, pl.getWorld().getHighestBlockYAt(x1, z), z);
                    Location locT = new Location(pl.getWorld(), X1, pl.getWorld().getHighestBlockYAt(X1, z), z);
                    pl.getWorld().spawnParticle(Particle.END_ROD, loc, 1, 0, 0, 0, 0);
                    pl.getWorld().spawnParticle(Particle.END_ROD, locT, 1, 0, 0, 0, 0);
                }
            }
        }.runTaskTimer(Bukkit.getPluginManager().getPlugin("RentLand"), 0, 10);

    }

    public boolean checkPosition(Location loc1, Location loc2, Player pl) {
        List<List<String>> data = FileManager.readCSV(FileManager.landFile);
        for (List<String> row : data) {
            int x = Integer.parseInt(row.get(2));
            int X = Integer.parseInt(row.get(3));
            int z = Integer.parseInt(row.get(4));
            int Z = Integer.parseInt(row.get(5));

            int x1 = Math.min(loc1.getBlockX(), loc2.getBlockX());
            int z1 = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
            int X1 = Math.max(loc1.getBlockX(), loc2.getBlockX());
            int Z1 = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

            if ((X1 - x1) * (Z1 - z1) < 9) {
                pl.sendTitle("[§e圈地失败§f]", "§7圈地面积过小");
                return false;
            }

            if (!(X < x1 || X1 < x || Z < z1 || Z1 < z)) {
                Tloc.remove(pl);
                pl.sendTitle("[§e圈地失败§f]", "§7包含他人领地（自己的也算他人的）");
                return false;
            }
        }
        return true;
    }
}