package sudark.rentland;

import org.bukkit.Bukkit;

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

    static String findName(String uuid) {
        File file = new File(Bukkit.getPluginManager().getPlugin("Courier").getDataFolder(), "allowlist.csv");
        List<List<String>> data = FileManager.readCSV(file);

        for (List<String> row : data) {
            if (row.get(0).equals(uuid)) {
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

    }

}
