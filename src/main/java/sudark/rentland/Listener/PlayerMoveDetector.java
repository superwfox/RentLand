package sudark.rentland.Listener;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static sudark.rentland.File.DataSniffer.getQQ;
import static sudark.rentland.Listener.LandNotice.ask;
import static sudark.rentland.RentLand.WorldName;
import static sudark.rentland.RentLand.checkData;

public class PlayerMoveDetector {

    public static void main() {
        ConcurrentHashMap<Player, Location> locations = new ConcurrentHashMap<>();

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player pl : Bukkit.getOnlinePlayers()) {
                    locations.putIfAbsent(pl, pl.getLocation());

                    if (pl.getGameMode().equals(GameMode.SPECTATOR)) continue;
                    if (pl.getGameMode().equals(GameMode.CREATIVE)) continue;

                    if (locations.get(pl).equals(pl.getLocation())) continue;

                    if (!pl.getLocation().getWorld().getName().equals(WorldName)) {
                        if (pl.hasMetadata("invader"))
                            pl.removeMetadata("invader", Bukkit.getPluginManager().getPlugin("RentLand"));
                        continue;
                    }

                    try {
                        detected(pl);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                    locations.put(pl, pl.getLocation());
                }
            }
        }.runTaskTimer(Bukkit.getPluginManager().getPlugin("RentLand"), 0, 10L);
    }

    public static void detected(Player pl) throws URISyntaxException {
        for (List<String> row : checkData) {
            int x = Integer.parseInt(row.get(2));
            int X = Integer.parseInt(row.get(3));
            int y = Integer.parseInt(row.get(4));
            int Y = Integer.parseInt(row.get(5));
            Location loc = pl.getLocation();

            if (loc.getBlockX() >= x && loc.getBlockX() <= X && loc.getBlockZ() >= y && loc.getBlockZ() <= Y) {

                if (row.subList(6, row.size()).contains(getQQ(pl))) {
                    pl.removeMetadata("invader", Bukkit.getPluginManager().getPlugin("RentLand"));
                    return;
                }

                if (pl.hasMetadata("invader")) return;

                String LandID = x + "," + y;
                pl.setMetadata("invader", new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("RentLand"), row.get(6) + "|" + row.get(1) + "|" + LandID));
                ask(pl);

                return;
            }
        }

        pl.removeMetadata("invader", Bukkit.getPluginManager().getPlugin("RentLand"));
    }
}
