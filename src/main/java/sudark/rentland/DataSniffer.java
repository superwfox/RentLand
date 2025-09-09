package sudark.rentland;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.util.List;

public class DataSniffer {

    static String findQQ(String uuid) {
        File file = new File(Bukkit.getPluginManager().getPlugin("Courier").getDataFolder(), "allowlist.csv");
        List<List<String>> data = FileManager.readCSV(file);

        for (List<String> row : data) {
            if (row.get(0).equals(uuid)) {
                return row.get(2);
            }
        }

        return null;
    }

    public String getUUID(Player pl) {
        NamespacedKey key = new NamespacedKey("PlayerSkinManager", "uuid");
        return pl.getPersistentDataContainer().get(key, PersistentDataType.STRING);
    }

    static String findName(String qq) {
        File file = new File(Bukkit.getPluginManager().getPlugin("Courier").getDataFolder(), "allowlist.csv");
        List<List<String>> data = FileManager.readCSV(file);

        for (List<String> row : data) {
            if (row.get(2).equals(qq)) {
                return row.get(1);
            }
        }

        return null;
    }

    static String findUUID(String name) {
        File file = new File(Bukkit.getPluginManager().getPlugin("Courier").getDataFolder(), "allowlist.csv");
        List<List<String>> data = FileManager.readCSV(file);

        for (List<String> row : data) {
            if (row.get(1).equals(name)) {
                return row.get(0);
            }
        }
        return null;
    }

}
