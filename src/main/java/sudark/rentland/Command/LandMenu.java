package sudark.rentland.Command;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import sudark.rentland.File.FileManager;

import java.util.List;

import static sudark.rentland.File.DataSniffer.getQQ;
import static sudark.rentland.RentLand.WorldName;

public class LandMenu implements Listener {

    public static final String TITLE = "RentLand | 领地概况图";

    public static void open(Player player) {
        String qq = getQQ(player);
        List<List<String>> data = FileManager.readCSV(FileManager.landFile);

        boolean hasLand = false;
        for (List<String> row : data) {
            if (row.subList(6, row.size()).contains(qq)) {
                hasLand = true;
                break;
            }
        }

        if (!hasLand) {
            player.sendMessage("[§e领地§f] 您当前没有任何领地");
            return;
        }

        Inventory inv = Bukkit.createInventory(null, 18, TITLE);
        ItemStack land = new ItemStack(Material.FILLED_MAP, 1);

        for (List<String> row : data) {
            if (row.subList(6, row.size()).contains(qq)) {
                ItemMeta meta = land.getItemMeta();
                meta.setDisplayName("§f[ §e" + row.get(1) + " §f]");
                int x = Integer.parseInt(row.get(2));
                int X = Integer.parseInt(row.get(3));
                int z = Integer.parseInt(row.get(4));
                int Z = Integer.parseInt(row.get(5));
                int tx = (x + X) / 2;
                int tz = (z + Z) / 2;
                meta.setLore(List.of("§7X: " + tx, "§7Z: " + tz));
                land.setItemMeta(meta);
                inv.addItem(land.clone());
            }
        }

        player.openInventory(inv);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().equals(TITLE)) return;

        e.setCancelled(true);

        if (e.getCurrentItem() == null) return;
        if (!e.getCurrentItem().hasItemMeta()) return;
        if (!e.getCurrentItem().getItemMeta().hasLore()) return;

        Player pl = (Player) e.getWhoClicked();
        List<String> lore = e.getCurrentItem().getItemMeta().getLore();

        int x = Integer.parseInt(lore.get(0).substring(5));
        int z = Integer.parseInt(lore.get(1).substring(5));

        pl.closeInventory();
        pl.teleport(
                new Location(Bukkit.getWorld(WorldName), x, Bukkit.getWorld(WorldName).getHighestBlockYAt(x, z) + 2, z)
        );
        pl.sendTitle(e.getCurrentItem().getItemMeta().getDisplayName(), "");
    }
}
