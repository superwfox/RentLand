package sudark.rentland.Listener;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import sudark.rentland.File.FileManager;
import sudark.rentland.Onebot.OneBotClient;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class LandTimer {
    public static void main(Plugin plugin) {
        new BukkitRunnable() {
            @Override
            public void run() {

                Calendar now = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai"));
                int hour = now.get(11);
                int minute = now.get(12);

                if (hour == 9 && minute == 30) {
                    LandDecline();
                }
            }
        }.runTaskTimer(plugin, 0, 20 * 59);
    }

    public static void LandDecline() {
        List<List<String>> data = FileManager.readCSV(FileManager.landFile);

        for (int i = data.size() - 1; i >= 0; i--) {
            List<String> row = data.get(i);
            int daysLeft = Integer.parseInt(row.get(0)) - 1;
            row.set(0, String.valueOf(daysLeft));

            String qq = row.get(6);
            String msg;

            if (daysLeft == 0) {
                msg = "您的领地【" + row.get(1) + "】今天到期，请尽快续租";
                OneBotClient.sendP(qq, msg);
            } else if (daysLeft == 1) {
                msg = "您的领地【" + row.get(1) + "】明天到期，请尽快续租";
                OneBotClient.sendP(qq, msg);
            } else if (daysLeft == 6) {
                msg = "您的领地【" + row.get(1) + "】还有一周到期，请尽快续租";
                OneBotClient.sendP(qq, msg);
            }

            if (daysLeft < 0) {
                msg = "您的领地【" + row.get(1) + "】已到期，请您知晓该领地已被回收，服务器不再提供任何保护和传送";
                OneBotClient.sendP(qq, msg);
                data.remove(i);
            }
        }

        FileManager.writeCSV(FileManager.landFile, data);
    }
}
