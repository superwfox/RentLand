package sudark.rentland.File;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static sudark.rentland.File.FileManager.*;
import static sudark.rentland.RentLand.checkData;

public class DataSniffer {

    public static Map<String, String> qq2NameMap = new HashMap<>();
    public static Map<String, String> name2QQMap = new HashMap<>();
    public static Map<String, String> UUID2QQMap = new HashMap<>();

    public static void reloadQQMap() {
        List<List<String>> data = readCSV(courierFile);

        for (List<String> row : data) {
            qq2NameMap.put(row.get(2), row.get(1));
            name2QQMap.put(row.get(1), row.get(2));
            UUID2QQMap.put(row.get(0), row.get(2));
        }

        List<List<String>> dataT = readCSV(landFile);
        if(checkData == dataT)return;

        for (List<String> row : dataT) {
            for (int i = 6; i < row.size(); i++) {
                String uuid = row.get(i);
                String qq = UUID2QQMap.get(uuid);
                if (qq == null) continue;
                row.set(i, qq);
            }
        }
        FileManager.writeCSV(landFile, dataT);
    }

    public static String getQQ(Player pl) {
        NamespacedKey key = new NamespacedKey("sudark", "qq");
        return pl.getPersistentDataContainer().get(key, PersistentDataType.STRING);
    }

    public static Player getPlayerByQQ(String qq) {
        for (Player pl : Bukkit.getOnlinePlayers()) {
            if (getQQ(pl).equals(qq))
                return pl;
        }
        return null;
    }
}
