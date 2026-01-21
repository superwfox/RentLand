package sudark.rentland.Listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import sudark.rentland.File.FileManager;
import sudark.rentland.Onebot.OneBotClient;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static sudark.rentland.File.DataSniffer.*;

public class BookController implements Listener {

    @EventHandler
    public void onPlayerEditBOOK(PlayerEditBookEvent e) {
        if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasLore()
                && e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLore().contains("§e地产证")) {

            BookMeta bm = e.getNewBookMeta();
            List<String> pages = bm.getPages();
            Player pl = e.getPlayer();
            String whole = "";
            reloadQQMap();

            for (String s : pages) {
                whole += s;
            }

            String[] lands = whole.split("§l-土地详情 \\|");
            for (String land : lands) {
                String name = findLandName(land);
                String time = findLandDuration(land);
                int loser = findLandPlayerIndex(land);
                String LandID = findLandID(land);

                if (name == null || time == null || LandID == null) continue;

                List<List<String>> data = FileManager.readCSV(FileManager.landFile);
                for (int i = 0; i < data.size(); i++) {
                    List<String> row = data.get(i);
                    if (!(row.get(2) + "," + row.get(4)).equals(LandID)) continue;

                    if (Objects.equals(name, row.get(1)) && Objects.equals(time, row.get(0)) && loser == -1) continue;

                    if (!name.equals(row.get(1))) {
                        pl.sendMessage("§7领地名已修改为§e§l" + name);
                        row.set(1, name);
                        FileManager.writeCSV(FileManager.landFile, data);
                        return;
                    }

                    if (!time.equals(row.get(0))) {
                        int x = Integer.parseInt(row.get(2)), x1 = Integer.parseInt(row.get(3));
                        int z = Integer.parseInt(row.get(4)), z1 = Integer.parseInt(row.get(5));
                        int t = Integer.parseInt(row.get(0));
                        int newT = Math.max(Integer.parseInt(time), 0);
                        int area = (x1 - x) * (z1 - z);
                        int deltaT = (t - newT) * area / 700 - 1;

                        if (Integer.parseInt(time) <= 0) {
                            String msg = "您的领地【" + row.get(1) + "】已被回收，服务器不再提供任何保护和传送";
                            OneBotClient.sendP(row.get(6), msg);
                            pl.sendMessage("[§e领地§f] 【§b" + row.get(1) + "§f】已被回收，服务器不再提供任何保护");
                            pl.giveExpLevels(deltaT);
                            data.remove(i);
                            FileManager.writeCSV(FileManager.landFile, data);
                            return;
                        }

                        if (-deltaT > pl.getLevel()) {
                            pl.sendMessage("[§e领地§f] 等级不足，无法修改租赁时间\n目前每周租金为 " + area / 100 + " 级");
                            return;
                        }

                        pl.giveExpLevels(deltaT);
                        pl.sendMessage("[§e领地§f] 修改租赁时间为§e" + time + "§f天 | §b" + -deltaT + "§f级");
                        row.set(0, time);
                        FileManager.writeCSV(FileManager.landFile, data);
                        return;
                    }

                    if (loser != -1) {
                        String qq = row.get(6 + loser);
                        if (row.contains(qq)) {
                            row.remove(qq);
                            pl.sendMessage("§7已为§b" + qq2NameMap.get(qq) + "§f[§e" + qq + "§f] §7移除§l" + row.get(1) + "§r§7的领地权限");
                            FileManager.writeCSV(FileManager.landFile, data);
                        }
                        return;
                    }
                }
            }
        }
    }

    public String findLandName(String str) {
        String regex = "<(.*?)>";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);

        while (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public String findLandDuration(String str) {
        Pattern pattern1 = Pattern.compile("-§0剩余租赁天数: §l(\\d+)");
        Matcher matcher1 = pattern1.matcher(str);
        if (matcher1.find()) {
            return matcher1.group(1);
        }
        return null;
    }

    public int findLandPlayerIndex(String str) {
        int atIndex = str.lastIndexOf('@');
        if (atIndex == -1) return -1;

        String sub = str.substring(0, atIndex);

        Pattern pattern = Pattern.compile("§6·(\\d+)");
        Matcher matcher = pattern.matcher(sub);

        int lastNumber = -1;
        while (matcher.find()) {
            lastNumber = Integer.parseInt(matcher.group(1));
        }
        return lastNumber;
    }

    public String findLandID(String str) {
        Pattern pattern = Pattern.compile("\\[§6([+-]?\\d+)§0,§6([+-]?\\d+)§0]");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            String x = matcher.group(1);
            String z = matcher.group(2);
            return x + "," + z;
        }
        return null;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
            ItemMeta itemMeta = e.getPlayer().getInventory().getItemInMainHand().getItemMeta();
            Player pl = e.getPlayer();

            if (itemMeta != null && itemMeta.hasLore() && itemMeta.getLore().contains("§e地产证")) {
                e.getPlayer().getInventory().getItemInMainHand().setItemMeta(reform(pl));
            }
        }
    }

    public ItemMeta reform(Player pl) {
        reloadQQMap();
        BookMeta bookMeta = (BookMeta) (new ItemStack(Material.WRITABLE_BOOK)).getItemMeta();
        bookMeta.setRarity(ItemRarity.RARE);

        List<List<String>> data = FileManager.readCSV(FileManager.landFile);
        for (List<String> row : data) {
            if (row.subList(6, row.size()).contains(getQQ(pl))) {
                int x = Integer.parseInt(row.get(2));
                int x1 = Integer.parseInt(row.get(3));
                int z = Integer.parseInt(row.get(4));
                int z1 = Integer.parseInt(row.get(5));

                String name = row.get(1);
                String ownerQQ = row.get(6);
                String ownerName = qq2NameMap.get(ownerQQ);
                int t = Integer.parseInt(row.get(0));

                int dx = x1 - x, dz = z1 - z;
                int area = dx * dz;

                String P = "§l-土地详情 | <" + name + ">" +
                        "\n\n §6§lA§0:[§6" + x + "§0,§6" + z + "§0]" +
                        "\n §0§lB§0:[§6" + x1 + "§0,§6" + z1 + "§0]§l\n\n" +
                        "  -§0面积: §0§l" + area + "\n " +
                        " -§0剩余租赁天数: §l" + t +
                        "\n\n  =§0雇主: §l" + ownerName +
                        "\n\n§7   \"服务器土地管理部\"";
                bookMeta.addPage(P);

                if (row.size() == 7) continue;

                List<String> qqList = row.subList(7, row.size());

                int turn = 0;
                String X = "";

                for (String qq : qqList) {
                    turn++;
                    X += "\n\n§6·" + turn + " : §0" + qq2NameMap.get(qq);
                    if (turn == 6) {
                        bookMeta.addPage(X);
                        X = "";
                    }
                }

                if (turn % 6 != 0) bookMeta.addPage(X);
            }
        }
        bookMeta.setLore(List.of("§r§e地产证"));
        return bookMeta;
    }
}
