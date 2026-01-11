package sudark.rentland;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import sudark.rentland.Command.BookCommand;
import sudark.rentland.Command.CommandCompleter;
import sudark.rentland.Command.LandMenu;
import sudark.rentland.File.DataSniffer;
import sudark.rentland.File.FileManager;
import sudark.rentland.Listener.*;
import sudark.rentland.Onebot.OneBotClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public final class RentLand extends JavaPlugin {

    public static OneBotClient client;
    public static String QQGroup;
    public static String WorldName;
    public static String BotName;
    public static int port = 3001;

    public static List<List<String>> checkData = null;

    @Override
    public void onEnable() {

        if (!FileManager.checkFile(FileManager.landFolder, FileManager.landFile)) return;

        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new BookController(), this);
        Bukkit.getPluginManager().registerEvents(new LandNotice(), this);
        Bukkit.getPluginManager().registerEvents(new PurChaseListener(), this);
        Bukkit.getPluginManager().registerEvents(new LandMenu(), this);
        Bukkit.getPluginCommand("book").setExecutor(new BookCommand());
        Bukkit.getPluginCommand("book").setTabCompleter(new CommandCompleter());

        checkData = FileManager.readCSV(FileManager.landFile);

        PlayerMoveDetector.main();
        LandTimer.main(this);
        DataSniffer.reloadQQMap();

        try {
            client = new OneBotClient(new URI("ws://127.0.0.1:" + port));
            client.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
