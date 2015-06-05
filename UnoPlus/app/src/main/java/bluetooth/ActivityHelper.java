package bluetooth;

import android.app.Activity;

/**
 * Created by Luki on 05.06.2015.
 */
public class ActivityHelper {
    Activity activity;
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


    public ActivityHelper(Activity a){
        activity = a;
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
}
