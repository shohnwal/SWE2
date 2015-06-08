package bluetooth;

import android.content.Context;
import android.os.Handler;

/**
 * Created by Luki on 08.06.2015.
 */
public class BltSingelton {

    static BluetoothService singleService;
    private static Handler h;
    private static ActivityHelper ah;
    public BltSingelton(Handler handler, ActivityHelper activity){
        if(singleService==null){
            this.h = handler;
            this.ah = activity;
        }
    }

    public static BluetoothService getInstance(){
        if(singleService==null)
            singleService=new BluetoothService(h, ah);
        return singleService;
    }
}
