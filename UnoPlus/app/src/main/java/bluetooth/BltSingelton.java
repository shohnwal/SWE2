package bluetooth;

import android.content.Context;
import android.os.Handler;

/**
 * Created by Luki on 08.06.2015.
 */
public class BltSingelton {

    static BluetoothService singleService;
    public BltSingelton(Context context, Handler handler, ActivityHelper activity){
        if(singleService==null)
            singleService=new BluetoothService(context,  handler, activity);
    }

    private BluetoothService getService(){
        return singleService;
    }
}
