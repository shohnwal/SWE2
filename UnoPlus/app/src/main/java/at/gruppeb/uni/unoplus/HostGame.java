package at.gruppeb.uni.unoplus;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import bluetooth.ActivityHelper;
import bluetooth.BltSingelton;
import bluetooth.BluetoothService;


public class HostGame extends ActionBarActivity {

    private TextView hostName;
    String hostNameString;
    private ImageView iBVolumeOn;
    private Button btn_start;

    //Bluetooth
    private static final boolean D = true;
    private static final String TAG = "Lobby";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    // Name of the connected device
    private String mConnectedDeviceName = null;
    // Array adapter for the conversation thread
    private ArrayAdapter<String> mConversationArrayAdapter;
    // String buffer for outgoing messages
    private StringBuffer mOutStringBuffer;
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the chat services
    private BluetoothService mBltService = null;

    private ActivityHelper aHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set the Screen in Landscape mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //Remove the Title Bar
        getSupportActionBar().hide();
        //Remove the notification Bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_host_game);

        hostName = (TextView) findViewById(R.id.textView_hostName);
        Intent i = getIntent();
        hostNameString = i.getStringExtra("hostName");
        hostName.setText(hostNameString);

        mBltService = new BltSingelton(this,mHandler,aHelper).getInstance();

        btn_start = (Button)findViewById(R.id.btnStart);
    }

    @Override
    protected void onStart() {
        super.onStart();
        init();

        if(!mBltService.isServer()){
            btn_start.setClickable(false);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        serviceStop();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        serviceStop();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    public void init() {
        setupImageButtonVolume();


    }

    public void setupImageButtonVolume() {
        iBVolumeOn = (ImageButton) findViewById(R.id.imageButton_host);
        if (!(MyService.isInstanceCreated())) {
            iBVolumeOn.setActivated(!iBVolumeOn.isActivated());
        }

        iBVolumeOn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("Volume");
                v.setActivated(!v.isActivated());
                if (v.isActivated()) {
                    serviceStop();
                } else {
                    serviceStart();
                }
            }
        });
    }

    public void serviceStart() {
        startService(new Intent(this, MyService.class));
    }

    public void serviceStop() {
        stopService(new Intent(this, MyService.class));
    }

    // The Handler that gets information back from the BluetoothChatService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what== ActivityHelper.MESSAGE_STATE_CHANGE){
                if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                switch (msg.arg1) {
                    case BluetoothService.STATE_CONNECTED:
                        mConversationArrayAdapter.clear();
                        break;
                    case BluetoothService.STATE_CONNECTING:
                        break;
                    case BluetoothService.STATE_LISTEN:
                    case BluetoothService.STATE_NONE:
                        break;
                }
            }
            if(msg.what== ActivityHelper.MESSAGE_WRITE){
                byte[] writeBuf = (byte[]) msg.obj;
                // construct a string from the buffer
                String writeMessage = new String(writeBuf);
                mConversationArrayAdapter.add("Me:  " + writeMessage);
            }
            if(msg.what== ActivityHelper.MESSAGE_READ){
                byte[] readBuf = (byte[]) msg.obj;
                // construct a string from the valid bytes in the buffer
                String readMessage = new String(readBuf, 0, msg.arg1);
                if (readMessage.length() > 0) {
                    mConversationArrayAdapter.add(mConnectedDeviceName+":  " + readMessage);
                }
            }
            if(msg.what== ActivityHelper.MESSAGE_DEVICE_NAME){
                // save the connected device's name
                mConnectedDeviceName = msg.getData().getString(ActivityHelper.DEVICE_NAME);
                Toast.makeText(getApplicationContext(), "Connected to "
                        + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
            }
            if(msg.what== ActivityHelper.MESSAGE_TOAST){
                //if (!msg.getData().getString(TOAST).contains("Unable to connect device")) {
                Toast.makeText(getApplicationContext(), msg.getData().getString(ActivityHelper.TOAST),
                        Toast.LENGTH_SHORT).show();
                //}
            }


        }
    };
}
