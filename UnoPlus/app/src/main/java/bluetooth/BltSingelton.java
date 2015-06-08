package bluetooth;

import android.content.Context;
import android.os.Handler;

/**
 * Created by Luki on 08.06.2015.
 */
public class BltSingelton {

    static BluetoothService singleService;
    private static Context c;
    private static Handler h;
    private static ActivityHelper ah;
    public BltSingelton(Context context, Handler handler, ActivityHelper activity){
        if(singleService==null){
            this.c = context;
            this.h = handler;
            this.ah = activity;
        }
    }

    public static BluetoothService getInstance(){
        if(singleService==null)
            singleService=new BluetoothService(c,  h, ah);
        return singleService;
    }
}
