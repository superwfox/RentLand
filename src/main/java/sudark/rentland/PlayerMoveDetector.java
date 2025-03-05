package sudark.rentland;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerMoveDetector {

    public static void main() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player pl : Bukkit.getOnlinePlayers()) {
                    ConcurrentHashMap<Player, Location> locations = new ConcurrentHashMap<>();
                    locations.putIfAbsent(pl, pl.getLocation());

                    if (locations.get(pl) == pl.getLocation()) return;

                    detected(pl);
                    locations.put(pl, pl.getLocation());
                }
            }
        }.runTaskTimer(Bukkit.getPluginManager().getPlugin("RentLand"), 0, 10L);
    }

    public static void detected(Player pl) {

        List<List<String>> data = FileManager.readCSV(FileManager.landFile);
        for (List<String> row : data) {
            int x = Integer.parseInt(row.get(2));
            int X = Integer.parseInt(row.get(3));
            int y = Integer.parseInt(row.get(4));
            int Y = Integer.parseInt(row.get(5));

            if (pl.getLocation().getBlockX() >= x && pl.getLocation().getBlockX() <= X && pl.getLocation().getBlockZ() >= y && pl.getLocation().getBlockZ() <= Y) {
                if (row.subList(6, row.size()).contains(pl.getUniqueId().toString())) {
                    pl.removeMetadata("invader", Bukkit.getPluginManager().getPlugin("RentLand"));
                    return;
                }
                pl.setMetadata("invader", new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("RentLand"), true));
                return;
            }

        }

        pl.removeMetadata("invader", Bukkit.getPluginManager().getPlugin("RentLand"));

    }
}
