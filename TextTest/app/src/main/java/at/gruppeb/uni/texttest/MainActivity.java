package at.gruppeb.uni.texttest;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;


public class MainActivity extends ActionBarActivity {

    private BluetoothAdapter bluetooth;
    private BluetoothSocket socket;
    private UUID uuid = UUID.fromString("a60f35f0-b93a-11de-8a39-08002009c666");
    private ArrayList<BluetoothDevice> foundDevices;
    private ArrayList<BluetoothDevice> connectedDevices;


    private void configureBluetooth() {
        bluetooth = BluetoothAdapter.getDefaultAdapter();
    }
    private ArrayAdapter<BluetoothDevice> aa;
    private ListView list;

    private void setupSearchButton() {
        Button searchButton = (Button) findViewById(R.id.button_search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                registerReceiver(discoveryResult,
                        new IntentFilter(BluetoothDevice.ACTION_FOUND));
                if (!bluetooth.isDiscovering()) {
                    foundDevices.clear();
                    bluetooth.startDiscovery();
                }
            }
        });
    }

    private void setupListView() {
        foundDevices = new ArrayList();
        connectedDevices = new ArrayList<>();
        aa = new ArrayAdapter<BluetoothDevice>(this,
                android.R.layout.simple_list_item_1,
                foundDevices);
        list = (ListView)findViewById(R.id.list_discovered);
        list.setAdapter(aa);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View view,
                                    int index, long arg3) {
                AsyncTask<Integer, Void, Void> connectTask =
                        new AsyncTask<Integer, Void, Void>() {
                            @Override
                            protected Void doInBackground(Integer...params) {
                                try {
                                    BluetoothDevice device = foundDevices.get(params[0]);
                                    socket = device.createRfcommSocketToServiceRecord(uuid);
                                    socket.connect();
                                    connectedDevices.add(device);
                                } catch (IOException e) {
                                    Log.d("BLUETOOTH_CLIENT", e.getMessage());
                                }
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void result) {

                                //switchViews();
                            }
                        };
                connectTask.execute(index);
            }
        });
    }

    private Handler handler = new Handler();
    private void switchUI() {
        final TextView messageText = (TextView)findViewById(R.id.text_messages);
        final EditText textEntry = (EditText)findViewById(R.id.text_message);
        messageText.setVisibility(View.VISIBLE);
        list.setVisibility(View.GONE);
        textEntry.setEnabled(true);
        textEntry.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_DPAD_CENTER)) {
                    sendMessage(socket, textEntry.getText().toString());
                    textEntry.setText("");
                    return true;
                }
                return false;
            }
        });

        BluetoothSocketListener bsl = new BluetoothSocketListener(socket,
                handler, messageText);
        Thread messageListener = new Thread(bsl);
        messageListener.start();
    }

    private void sendMessage(BluetoothSocket socket, String msg) {
        OutputStream outStream;
        try {
            outStream = socket.getOutputStream();
            byte[] byteString = (msg + " ").getBytes();
            //stringAsBytes[byteString.length−1] = 0;
            outStream.write(byteString);
        } catch (IOException e) {
            Log.d("BLUETOOTH_COMMS", e.getMessage());
        }
    }

    private void setupSendButton() {
        Button sendButton = (Button)findViewById(R.id.button_send);
        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(socket != null) {
                    String text = ((EditText) findViewById(R.id.text_message)).getText().toString();
                    sendMessage(socket, text);
                }
                else{
                    Toast.makeText(getApplicationContext(),"please connect before sending messages",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    BroadcastReceiver discoveryResult = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            BluetoothDevice remoteDevice;
            remoteDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (bluetooth.getBondedDevices().contains(remoteDevice)) {
                foundDevices.add(remoteDevice);
                aa.notifyDataSetChanged();
            }
        }
    };


    private static int DISCOVERY_REQUEST = 1;
    private void setupListenButton() {
        Button listenButton = (Button)findViewById(R.id.button_listen);
        listenButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent disc;
                disc = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                startActivityForResult(disc, DISCOVERY_REQUEST);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent
            data) {
        if (requestCode == DISCOVERY_REQUEST) {
            boolean isDiscoverable = resultCode > 0;
            if (isDiscoverable) {
                String name = "bluetoothserver";
                try {
                    final BluetoothServerSocket btserver =
                            bluetooth.listenUsingRfcommWithServiceRecord(name, uuid);
                    AsyncTask<Integer, Void, BluetoothSocket> acceptThread =
                            new AsyncTask<Integer, Void, BluetoothSocket>() {
                                @Override
                                protected BluetoothSocket doInBackground(Integer...params) {

                                    try {
                                        socket = btserver.accept(params[0]*1000);
                                        return socket;
                                    } catch (IOException e) {
                                        Log.d("BLUETOOTH", e.getMessage());
                                    }

                                    return null;
                                }

                                @Override
                                protected void onPostExecute(BluetoothSocket result) {
                                    if (result != null)
                                        switchUI();
                                }
                            };
                    acceptThread.execute(resultCode);
                } catch (IOException e) {
                    Log.d("BLUETOOTH", e.getMessage());
                }
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){
        // Get the Bluetooth Adapter
        configureBluetooth();
        // Setup the ListView of discovered devices
        setupListView();
        // Setup search button
        setupSearchButton();
        // Setup listen button
        setupListenButton();
        // Setup send button
        setupSendButton();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class MessagePoster implements Runnable {
        private TextView textView;
        private String message;
        public MessagePoster(TextView textView, String message) {
            this.textView = textView;
            this.message = message;
        }
        public void run() {
            textView.setText(message);
        }
    }

    private class BluetoothSocketListener implements Runnable {
        private BluetoothSocket socket;
        private TextView textView;
        private Handler handler;
        public BluetoothSocketListener(BluetoothSocket socket,
                                       Handler handler, TextView textView) {
            this.socket = socket;
            this.textView = textView;
            this.handler = handler;
        }

        public void run() {
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            try {
                InputStream instream = socket.getInputStream();
                int bytesRead = -1;
                String message = "";
                while (true) {
                    message = "";
                    bytesRead = instream.read(buffer);
                    if (bytesRead != -1) {
                        while ((bytesRead==bufferSize)&&(buffer[bufferSize-1] != 0)) {
                            message = message + new String(buffer, 0, bytesRead);
                            bytesRead = instream.read(buffer);
                        }
                        message = message + new String(buffer, 0, bytesRead - 1);
                        handler.post(new MessagePoster(textView, message));
                        socket.getInputStream();
                    }
                }
            } catch (IOException e) {
                Log.d("BLUETOOTH_COMMS", e.getMessage());
            }
        }
    }

}
