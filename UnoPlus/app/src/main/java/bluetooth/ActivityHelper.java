package bluetooth;

import android.app.Activity;
import android.os.Handler;

import java.io.Serializable;

/**
 * Created by Luki on 05.06.2015.
 */
public class ActivityHelper implements Serializable{
    Activity activity;
    Handler handler;
    int messageState;

    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    public static int playerNR =-1;

    public boolean isServer;

    public ActivityHelper(Activity a,Handler h){
        activity = a;
        handler = h;
        playerNR++;
    }

    public ActivityHelper(Activity a){
        activity = a;
        playerNR++;
    }

    public int getMessageState() {
        return messageState;
    }

    public void setMessageState(int messageState) {
        this.messageState = messageState;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public int getPlayerNr(){
        return this.getPlayerNr();
    }

    public boolean isServer() {
        return isServer;
    }

    public void setIsServer(boolean isServer) {
        this.isServer = isServer;
    }
}
