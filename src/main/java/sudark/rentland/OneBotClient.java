package sudark.rentland;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.List;

import static sudark.rentland.LandNotice.*;

public class OneBotClient extends WebSocketClient {

    public OneBotClient(URI serverUri) {
        super(serverUri);
    }

    public void sendP(String user, String message) {
        JSONObject connected = new JSONObject();
        JSONObject connectedi = new JSONObject();
        connectedi.put("user_id", user);
        connectedi.put("message", message);
        connectedi.put("auto_escape", "false");
        connected.put("action", "send_private_msg");
        connected.put("params", connectedi);

        try {
            this.send(connected.toString());
        } catch (Exception var6) {
            Exception e = var6;
            e.printStackTrace();
        }

    }

    public void sendG(String message) {
        JSONObject connected = new JSONObject();
        JSONObject connectedi = new JSONObject();
        connectedi.put("group_id", "1007142639");
        connectedi.put("message", message);
        connectedi.put("auto_escape", "false");
        connected.put("action", "send_group_msg");
        connected.put("params", connectedi);

        try {
            this.send(connected.toString());
        } catch (Exception var5) {
            Exception e = var5;
            e.printStackTrace();
        }

    }

    public void at(String id) {
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
        inner.put("group_id", "571591801");
        inner.put("message", msg);
        json.put("params", inner);

        try {
            this.send(json.toString());
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
        JSONObject sender = json.getJSONObject("sender");
        String qq = JSONObject.fromObject(sender).getString("user_id");

        if (qqs.contains(qq)) {

            if (json.getString("post_type").equals("message")) {
                if (json.getString("message_type").equals("private")) {
                    if (json.getString("message").equals("允许")) {
                        int index = qqs.indexOf(qq);
                        List<List<String>> data = FileManager.readCSV(sudark.rentland.FileManager.landFile);
                        for (List<String> row : data) {
                            int x = Integer.parseInt(row.get(2));
                            int X = Integer.parseInt(row.get(3));
                            int y = Integer.parseInt(row.get(4));
                            int Y = Integer.parseInt(row.get(5));

                            String LandID = x + X + y + Y + "";
                            String landID = landIDs.get(index);

                            if (LandID.equals(landID)) {
                                row.add(uuids.get(index));
                                break;
                            }
                        }
                        FileManager.writeCSV(sudark.rentland.FileManager.landFile, data);
                        sendP(qq, "已为【" + uuids.get(index) + "】增加权限，若您无法上线管理权限，可以将此消息发送至管理员处理");
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
