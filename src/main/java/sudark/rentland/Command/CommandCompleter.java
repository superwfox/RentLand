package sudark.rentland.Command;

import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(org.bukkit.command.CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        if(sender instanceof Player pl){
            if(pl.isOp() && args.length == 1){
                return List.of("return","lands");
            }
            return List.of("lands");
        }
        return null;
    }
}
