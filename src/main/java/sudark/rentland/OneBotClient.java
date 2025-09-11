package sudark.rentland;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.bukkit.Bukkit;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static sudark.rentland.LandNotice.*;
import static sudark.rentland.RentLand.QQGroup;
import static sudark.rentland.RentLand.client;

public class OneBotClient extends WebSocketClient {

    public OneBotClient(URI serverUri) {
        super(serverUri);
    }

    public static void sendP(String user, String message) {
        JSONObject connected = new JSONObject();
        JSONObject connectedi = new JSONObject();
        connectedi.put("user_id", user);
        connectedi.put("message", message);
        connectedi.put("auto_escape", "false");
        connected.put("action", "send_private_msg");
        connected.put("params", connectedi);

        try {
            client.send(connected.toString());
        } catch (Exception var6) {
            Exception e = var6;
            e.printStackTrace();
        }

    }

    public static void sendG(String message) {
        JSONObject connected = new JSONObject();
        JSONObject connectedi = new JSONObject();
        connectedi.put("group_id", QQGroup);
        connectedi.put("message", message);
        connectedi.put("auto_escape", "false");
        connected.put("action", "send_group_msg");
        connected.put("params", connectedi);

        try {
            client.send(connected.toString());
        } catch (Exception var5) {
            Exception e = var5;
            e.printStackTrace();
        }

    }

    public static void at(String id) {
        JSONObject json = new JSONObject();
        JSONObject inner = new JSONObject();
        JSONArray msg = new JSONArray();
        JSONObject qq = new JSONObject();
        JSONObject type = new JSONObject();
        json.put("action", "send_group_msg");
        qq.put("qq", id);
        type.put("data", qq);
        type.put("type", "at");
        msg.add(type);
        inner.put("group_id", QQGroup);
        inner.put("message", msg);
        json.put("params", inner);

        try {
            client.send(json.toString());
        } catch (Exception var8) {
            Exception e = var8;
            e.printStackTrace();
        }

    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
    }

    @Override
    public void onMessage(String s) {
        JSONObject json = JSONObject.fromObject(s);
        if (!(json.containsKey("sender") && json.containsKey("user_id"))) return;
        JSONObject sender = json.getJSONObject("sender");
        String qq = JSONObject.fromObject(sender).getString("user_id");

        if (qqs.contains(qq)) {

            if (json.getString("post_type").equals("message")) {
                if (json.getString("message_type").equals("private")) {
                    if (json.getString("raw_message").equals("允许")) {
                        int index = qqs.indexOf(qq);
                        List<List<String>> data = FileManager.readCSV(sudark.rentland.FileManager.landFile);

                        for (List<String> row : data) {
                            int x = Integer.parseInt(row.get(2));
                            int y = Integer.parseInt(row.get(4));

                            String LandID = x + "," + y;
                            String landID = landIDs.get(index);

                            if (LandID.equals(landID)) {
                                row.add(uuids.get(index));

                                qqs.remove(qq);
                                uuids.remove(uuids.get(index));
                                landIDs.remove(landID);
                                break;
                            }

                        }

                        FileManager.writeCSV(FileManager.landFile, data);
                        sendP(qq, "已为【" + uuids.get(index) + "】 添加权限，若您无法上线管理，可以将此消息发送至管理员处理");
                        Bukkit.getPlayer(UUID.fromString(uuids.get(index))).sendMessage("[§e领地§f] 您已获得该领地权限");
                        task2.cancel();
                    }
                }
            }
        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {

    }

    @Override
    public void onError(Exception e) {

    }
}
