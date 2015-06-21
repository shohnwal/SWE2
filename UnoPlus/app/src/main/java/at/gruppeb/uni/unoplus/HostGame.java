package at.gruppeb.uni.unoplus;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import bluetooth.ActivityHelper;
import bluetooth.BltSingelton;
import bluetooth.BluetoothService;
import bluetooth.DeviceListActivity;


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

        mBltService = new BltSingelton(null,null).getInstance();
        mBltService.setmActivity(this);
        mBltService.setHandler(mHandler);
        aHelper = mBltService.getmActivity();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        btn_start = (Button)findViewById(R.id.btnStart);

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextScreen = new Intent("at.gruppeb.uni.unoplus.GameActivity");
                startActivity(nextScreen);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        init();

        btn_start.setClickable(mBltService.isServer());

        if(mBltService.isServer()){
            ensureDiscoverable();
        }

    }

    private void ensureDiscoverable() {
        if(D) Log.d(TAG, "ensure discoverable");
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
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



    /**
     * Sends a message.
     * @param message  A string of text to send.
     */
    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mBltService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mBltService.write(send);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
        }
    }

    // The action listener for the EditText widget, to listen for the return key
    private TextView.OnEditorActionListener mWriteListener =
            new TextView.OnEditorActionListener() {
                public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                    // If the action is a key-up event on the return key, send the message
                    if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                        String message = view.getText().toString();
                        sendMessage(message);
                    }
                    if(D) Log.i(TAG, "END onEditorAction");
                    return true;
                }
            };

    // The Handler that gets information back from the BluetoothChatService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what== ActivityHelper.MESSAGE_STATE_CHANGE){
                if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                switch (msg.arg1) {
                    case BluetoothService.STATE_CONNECTED:
                        Log.i("connected","pairing ok");
                        //mConversationArrayAdapter.clear();
                        break;
                    case BluetoothService.STATE_CONNECTING:
                        Log.i("conecting..","try to connect");
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(D) Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    // Get the device MAC address
                    String address = data.getExtras()
                            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    // Get the BLuetoothDevice object
                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                    // Attempt to connect to the device
                    mBltService.connect(device);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    // Initialize the BluetoothChatService to perform bluetooth connections
                    mBltService = new BluetoothService( mHandler,aHelper);

                    // Initialize the buffer for outgoing messages
                    mOutStringBuffer = new StringBuffer("");
                } else {
                    // User did not enable Bluetooth or an error occured
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }
}
