package sudark.rentland.Command;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import sudark.rentland.File.FileManager;
import sudark.rentland.Onebot.OneBotClient;

import java.util.List;

public class BookCommand implements CommandExecutor {
    @Override
    public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("book")) return false;

        if (sender instanceof Player pl) {
            if (args.length > 0 && args[0].equals("lands")) {
                LandMenu.open(pl);
                return true;
            }

            if (!pl.isOp()) return false;

            if (args.length > 0 && args[0].equals("return")) {
                inLand(pl);
                return true;
            }

            ItemStack property = new ItemStack(Material.WRITABLE_BOOK);
            ItemMeta propertyMeta = property.getItemMeta();
            propertyMeta.setLore(List.of("§r§e地产证"));
            property.setItemMeta(propertyMeta);
            pl.getInventory().addItem(property);
        }
        return true;
    }

    public static boolean inLand(Player pl) {

        List<List<String>> data = FileManager.readCSV(FileManager.landFile);
        for (List<String> row : data) {
            int x = Integer.parseInt(row.get(2));
            int X = Integer.parseInt(row.get(3));
            int y = Integer.parseInt(row.get(4));
            int Y = Integer.parseInt(row.get(5));
            Location loc = pl.getLocation();

            if (loc.getBlockX() >= x && loc.getBlockX() <= X && loc.getBlockZ() >= y && loc.getBlockZ() <= Y) {

                int t = Integer.parseInt(row.get(0));
                int area = (X - x) * (Y - y);
                int deltaT = t * area / 700 - 1;

                String owner = row.get(6);
                String msg = "您的领地[" + row.get(1) + "]已被停用，相关原因请咨询服主\n================\n- 领地价值 ： " + deltaT + "L";
                OneBotClient.at(owner);
                OneBotClient.sendG(msg);

                pl.sendMessage("领地§b[" + row.get(1) + "]§f已被停用，价值为§e " + deltaT + "L§r");
                data.remove(row);
                FileManager.writeCSV(FileManager.landFile, data);
                return true;
            }
        }
        pl.sendMessage("§7您当前不在领地内");
        return false;
    }
}
