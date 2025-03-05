package sudark.rentland;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class RentLand extends JavaPlugin {

    @Override
    public void onEnable() {

        FileManager.checkFile(FileManager.landFolder,FileManager.landFile);

        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);

        PlayerMoveDetector.main();

//        OneBotWebsocket ws;
//        try {
//            ws = new OneBotWebsocket(new URI("ws://localhost:3001"));
//        } catch (URISyntaxException e) {
//            throw new RuntimeException(e);
//        }
//
//        ws.sendG("Hello, world!");

    }
}
