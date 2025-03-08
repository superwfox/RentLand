package sudark.rentland;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.URI;
import java.net.URISyntaxException;

public final class RentLand extends JavaPlugin {

    @Override
    public void onEnable() {

        FileManager.checkFile(FileManager.landFolder, FileManager.landFile);

        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new BookController(), this);
        Bukkit.getPluginManager().registerEvents(new LandNotice(), this);
        Bukkit.getPluginManager().registerEvents(new PurChaseListener(), this);

        PlayerMoveDetector.main();

        try {
            OneBotClient client = new OneBotClient(new URI("ws://127.0.0.1:3001"));
            client.connect();
        } catch (URISyntaxException var2) {
            Exception e = var2;
            e.printStackTrace();
        }

    }
}
