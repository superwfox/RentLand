package sudark.rentland;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerInteractEvent event) {
        Player pl = event.getPlayer();

        if(pl.hasMetadata("invader")){{
            event.setCancelled(true);
            pl.playSound(pl.getLocation(), Sound.BLOCK_IRON_DOOR_CLOSE, 1, 1);
        }}

    }
}
