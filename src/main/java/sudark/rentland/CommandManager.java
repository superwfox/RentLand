package sudark.rentland;

import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class CommandManager implements CommandExecutor {
    @Override
    public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("book")) return false;

        if (sender instanceof Player pl) {
            if (!pl.isOp()) return false;

            ItemStack property = new ItemStack(Material.WRITABLE_BOOK);
            ItemMeta propertyMeta = property.getItemMeta();
            propertyMeta.setLore(List.of("§r§e地产证"));
            property.setItemMeta(propertyMeta);
            pl.getInventory().addItem(property);

        }
        return true;
    }

}
