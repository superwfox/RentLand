package sudark.rentland;

import org.bukkit.Bukkit;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    static File  landFolder = Bukkit.getPluginManager().getPlugin("RentLand").getDataFolder();
    static File landFile = new File(landFolder,"land.csv");

    public static void checkFile(File fileFolder,File file) {

        if (!fileFolder.exists()) {
            fileFolder.mkdirs();
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    //读文件
    public static List<List<String>> readCSV(File file) {
        List<List<String>> data = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] columns = line.split(",");
                List<String> row = new ArrayList<>();
                for (String column : columns) {
                    row.add(column.trim());
                }
                data.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    //写文件
    public static void writeCSV(File file, List<List<String>> data) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (List<String> row : data) {

                String line = String.join(",", row);
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
