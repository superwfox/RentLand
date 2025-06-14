package sudark.rentland;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

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

    public static void LandDecline(){
        List<List<String>> data = FileManager.readCSV(FileManager.landFile);

        for (List<String> row : data) {
            row.set(0, Integer.parseInt(row.get(0)) - 1 + "");
            String qq = DataSniffer.findQQ(row.get(6));
            String msg;

            if (row.get(0).equals("0")) {
                msg = "您的领地【" + row.get(1) + "】今天到期，请尽快续租";
                OneBotClient.sendP(qq, msg);
            }
            if (row.get(0).equals("1")) {
                msg = "您的领地【" + row.get(1) + "】明天到期，请尽快续租";
                OneBotClient.sendP(qq, msg);
            }
            if (row.get(0).equals("6")) {
                msg = "您的领地【" + row.get(1) + "】还有一周到期，请尽快续租";
                OneBotClient.sendP(qq, msg);
            }
        }
        FileManager.writeCSV(FileManager.landFile, data);
    }
}
