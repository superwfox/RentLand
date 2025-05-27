package sudark.rentland;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.URI;
import java.net.URISyntaxException;

public final class RentLand extends JavaPlugin {

    public static OneBotClient client;
    public static String QQGroup ;
    public static String WorldName;
    public static String BotName;
    public static int port = 3001;

    @Override
    public void onEnable() {

        if (!FileManager.checkFile(FileManager.landFolder, FileManager.landFile)) return;

        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new BookController(), this);
        Bukkit.getPluginManager().registerEvents(new LandNotice(), this);
        Bukkit.getPluginManager().registerEvents(new PurChaseListener(), this);
        Bukkit.getPluginCommand("book").setExecutor(new CommandManager());

        PlayerMoveDetector.main();
        LandTimer.main(this);

        try {
            client = new OneBotClient(new URI("ws://127.0.0.1:"+port));
            client.connect();
        } catch (URISyntaxException var2) {
            Exception e = var2;
            e.printStackTrace();
        }

    }
}
