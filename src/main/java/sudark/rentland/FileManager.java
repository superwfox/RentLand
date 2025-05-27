package sudark.rentland;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static sudark.rentland.RentLand.*;

public class FileManager {

    static File landFolder = Bukkit.getPluginManager().getPlugin("RentLand").getDataFolder();
    static File landFile = new File(landFolder, "land.csv");

    static File configFile = new File(landFolder, "config.yml");

    public static boolean checkFile(File fileFolder, File file) {

        if (!fileFolder.exists()) {
            fileFolder.mkdirs();
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                initConfig();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        WorldName = config.getString("WorldName");
        port = config.getInt("port");
        QQGroup = config.getString("QQGroup");
        BotName = config.getString("botName");

        return true;

    }

    //初始化配置文件
    public static void initConfig() {
        try (PrintWriter writer = new PrintWriter(configFile, "UTF-8")) {
            writer.println();
            writer.println("# 领地插件管理的世界名称（仅支持一个）");
            writer.println("WorldName: \"\"");
            writer.println();
            writer.println("# WebSocket正向连接端口(默认3001)");
            writer.println("port: ");
            writer.println();
            writer.println("# QQ群号");
            writer.println("QQGroup: \"\"");
            writer.println();
            writer.println("# 机器人名称（可以保持）");
            writer.println("botName: \"机器人\"");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bukkit.getScheduler().runTaskLater(RentLand.getPlugin(RentLand.class),
                () -> System.out.println("\u001B[93m配置文件已生成 修改后请\u001B[96m重启\u001B[93m服务器\u001B[0m")
                , 5 * 20L);
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
