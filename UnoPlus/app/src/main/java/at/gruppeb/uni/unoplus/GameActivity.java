package at.gruppeb.uni.unoplus;

import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Dictate.Record_Speech;
import bluetooth.ActivityHelper;
import bluetooth.BltSingelton;
import bluetooth.BluetoothService;
import bluetooth.GameObject;

/**
 * Created by Natascha on 28/05/2015.
 */
public class GameActivity extends ActionBarActivity implements View.OnTouchListener, View.OnDragListener {


    private static final String DEBUGTAG = "at.gruppeb.uni.unoplus";

    private ImageView ivcCurrentCard, ivcStackCard;
    private ImageView[] ivPlayers;
    private ArrayList<ImageViewCard> ivcHandCards;
    private ViewGroup ivCurrentCardParent;
    private Timer Timer;
    private int height, width, NumberOfPlayers;
    protected Gamemanager game;
    protected Player player;
    protected GameObject gameObject;

    //TODO GUI grey out HandCards not curentPlayer

    private SensorManager mSensorManager;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity

    //Game mechanics variables
    protected int currentPlayerID = 0;

    //Bluetooth
    private static final boolean D = true;
    private static final String TAG = "GameActivity";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    // Name of the connected device
    private String mConnectedDeviceName = null;
    // Array adapter for the conversation thread
    private List<String> mConversationArrayAdapter;
    // String buffer for outgoing messages
    private StringBuffer mOutStringBuffer;
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter;
    // Member object for the chat services
    protected BluetoothService mBltService = null;

    private ActivityHelper aHelper;
    protected ArrayList<String> stringList=new ArrayList<String>();

    private int ready=0;
    private boolean allReady = true;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set the Screen in Landscape mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //Remove the Title Bar
        getSupportActionBar().hide();
        //Remove the notification Bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_game);

        mBltService = new BltSingelton(null,null).getInstance();
        mBltService.setmActivity(this);
        mBltService.setHandler(mHandler);
        aHelper = mBltService.getmActivity();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mOutStringBuffer = new StringBuffer("");
        mConversationArrayAdapter = new ArrayList<>();



        Toast.makeText(this.getApplicationContext(),"Player "+this.mBltService.getPlayerId() + this.mBltService.getPlayerName(),Toast.LENGTH_LONG).show();
        Log.i(TAG, "Game startet");
    }

    /*private void sendMessageToSingle(String message ,int pos) {
        // Check that we're actually connected before trying anything
        if (mBltService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            String send = message;
            mBltService.write(send);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
        }
    }*/

    @Override
    protected void onStart() {
        super.onStart();
        this.player=new Player(this.mBltService.getPlayerId(),this);
        if(this.mBltService.isServer()) {
            this.serverinit();
        }
        System.out.println("\n====================\nnow trying to get number of connected players\n====================\n");
        this.NumberOfPlayers = this.mBltService.getNrOfPlayers();

        init();
    }

    protected void serverinit() {
        this.game = new Gamemanager(this.mBltService, this);
        this.game.decksinit();
        this.game.createCards();
        this.game.putFirstCardDown();

        this.gameObject = new GameObject(0,true,false,this.game.playdeck,this.game.takedeck,this.game.dealCards(this.mBltService));

        this.sendMessage(gameObject);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mSensorListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor
                (Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void init() {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) this.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
// the results will be higher than using the activity context object or the getWindowManager() shortcut
        wm.getDefaultDisplay().getMetrics(displayMetrics);

        this.setHeight(displayMetrics.heightPixels);
        this.setWidth(displayMetrics.widthPixels);



        this.initIvCurrentCard();
        this.initivcStackCard();
        this.initIvPlayers();
        initShakeListener();


        initTimer();

    }
    private void initTimer() {
        int refreshTime = 1000;
        Timer = new Timer();
        Timer.schedule(new TimerTask() {
            @Override
            public void run() {
                TimerMethod();
            }
        }, 0, refreshTime);
    }


    private void TimerMethod() {
        this.runOnUiThread(Timer_Tick);
    }

    private Runnable Timer_Tick = new Runnable() {
        public void run() {

            //This method runs in the same thread as the UI.

            //TODO GameMech GameLoop (in mehtode initTimer -> refreschTime)
            if (mBltService.isServer() && !game.game_ended) {
                game.serverloop(mBltService);
            }


            player.clientloop();

            removeAllViews();
            renderAllViews();

            Log.i(DEBUGTAG, "loop refresh");
            //TODO finde where to check uno was shaked
            if (mAccel > 2.5) {
                if (player.itsmyturn && player.isallowedtocheat) {
                    player.cheat();
                }
                Log.i(DEBUGTAG, "shake it!!! ~(^o^~) (~^o^)~");
                //TODO  cheat mechanics
            }
            //Do something to the UI thread here

        }
    };

    private void initShakeListener() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor
                (Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
    }

    private void initivcStackCard() {
        ivcStackCard = (ImageView) findViewById(R.id.iview_stackCard);
        int cardSize = height / 3;
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(cardSize, cardSize);
        int puffer = width / 2 - cardSize;

        ivcStackCard.setX(puffer);
        ivcStackCard.setY((height - cardSize) * .48f);

        ivcStackCard.setLayoutParams(lp);
        ivcStackCard.setBackground(this.getResources().getDrawable(R.drawable.back));
        ivcStackCard.setOnTouchListener(this);

    }

    private void initIvCurrentCard() {
        ivcCurrentCard = (ImageView) findViewById(R.id.iview_currentCard);
        int cardSize = height / 3;
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(cardSize, cardSize);
        int puffer = width / 2;

        ivcCurrentCard.setX(puffer);
        ivcCurrentCard.setY((height - cardSize) * .48f);

        ivCurrentCardParent = (ViewGroup) ivcCurrentCard.getParent();
        ivcCurrentCard.setLayoutParams(lp);
        ivcCurrentCard.setOnDragListener(this);
    }

    private void initIvPlayers() {


        if (NumberOfPlayers == 2) {
            ivPlayers = new ImageView[1];
            ivPlayers[0] = (ImageView) findViewById(R.id.iview_playerOne);
        } else if (NumberOfPlayers == 1) { //TODO Debug
            ivPlayers = new ImageView[0];
            //ivPlayers[0] = (ImageView) findViewById(R.id.iview_playerFour);
        }else if (NumberOfPlayers == 3) {
            ivPlayers = new ImageView[2];
            ivPlayers[0] = (ImageView) findViewById(R.id.iview_playerOne);
            ivPlayers[1] = (ImageView) findViewById(R.id.iview_playerTwo);
        } else if (NumberOfPlayers == 4) {
            ivPlayers = new ImageView[3];
            ivPlayers[0] = (ImageView) findViewById(R.id.iview_playerOne);
            ivPlayers[1] = (ImageView) findViewById(R.id.iview_playerTwo);
            ivPlayers[2] = (ImageView) findViewById(R.id.iview_playerThree);
        } else if (NumberOfPlayers == 5) {
            ivPlayers = new ImageView[4];
            ivPlayers[0] = (ImageView) findViewById(R.id.iview_playerOne);
            ivPlayers[1] = (ImageView) findViewById(R.id.iview_playerTwo);
            ivPlayers[2] = (ImageView) findViewById(R.id.iview_playerThree);
            ivPlayers[3] = (ImageView) findViewById(R.id.iview_playerFour);
        }

        for (int i = 0,j=0; i < ivPlayers.length; i++, j++) {
            ivPlayers[i].setVisibility(View.VISIBLE);
            ivPlayers[i].setX(ivPlayers[i].getX() + i * 100);
            if (!(this.player.player_id == i)){
                ivPlayers[i].setTag(j);
            }else j++;
//TODO GUI Set position & size of other players
            /*ivPlayers[i].layout(Math.round(this.width / NumberOfPlayers),  (int) Math.round(this.height * .99f),
Math.round((i + 1) * this.width / NumberOfPlayers), (int) Math.round(this.height * .99f));
            ivPlayers[i].setBaselineAlignBottom(true);

            this.addContentView(ivPlayers[i], ivPlayers[i].getLayoutParams());
            */
        }
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private void removeAllViews() {
        //TODO GUI remove:
        removeHandCards();
        //TODO GUI Players
    }

    private void removeHandCards() {
        if (ivcHandCards != null)
            for (ImageViewCard ivc : ivcHandCards) {
                ViewGroup parent = (ViewGroup) ivc.getParent();
                parent.removeView(ivc);
            }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private void renderAllViews() {
        renderHandCards();
        renderCurrentCard();
       // renderPlayers();
        //renderCurrentPlayer();

    }

    private void renderPlayers() {
        //TODO GUI highlight player

        for (int i = 0; i < ivPlayers.length; i++) {
            System.out.println("ivp[i]= " + i + "ivp[i] Tag= " + ivPlayers[i].getTag());
            //white = curr player
            if (this.player.player_id!=
                    this.currentPlayerID &&
                    (int)ivPlayers[i].getTag()
                            ==(this.currentPlayerID))
                ivPlayers[i].getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
            else
                ivPlayers[i].getBackground().setColorFilter(getCurrentPlayerColor(i), PorterDuff.Mode.MULTIPLY);

        }
    }

    private int getCurrentPlayerColor(int id) {
        int temp = 0;

        if (id == 0) {
            temp = Color.GREEN;
        } else if (id == 1) {
            temp = Color.BLUE;
        } else if (id == 2) {
            temp = Color.RED;
        } else if (id == 3) {
            temp = Color.YELLOW;
        } else if (id == 4) {
            temp = Color.CYAN;
        }
        return temp;
    }

    private void renderCurrentCard() {
        ivcCurrentCard.setBackground(ImageViewCard.getDrawableForCard(this.player.getPlaydeckTop(),
                ivcCurrentCard));
    }

    private void renderHandCards() {
        generateHandCards();

        int cardSize = getCardSize();
        int pufferLeft = (int) Math.floor((width - (cardSize * .30f * ivcHandCards.size() + cardSize * .70f)) / 2);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(cardSize, cardSize);

        //generate the ImageViewCard for the handcards
        for (ImageViewCard ivc : this.ivcHandCards) {
            //horizontal position set
            ivc.setX(Math.round(pufferLeft + ivcHandCards.indexOf(ivc) * cardSize * .30f));
            // vertical position set
            ivc.setY((height - cardSize) * .99f);


            ivc.setLayoutParams(lp);
            ivc.setBaselineAlignBottom(true);
            this.addContentView(ivc, ivc.getLayoutParams());

            if (this.player.itsmyturn){
                ivc.setOnTouchListener(this);
                ivc.getBackground().clearColorFilter();}
            else {
                ivc.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
            }
        }
    }

    private int getCardSize() {
        //TODO GUI fix Scaling size
        int cardSizeH = (int) Math.floor(height * .94f / 3);
        //int cardSizeW = (int) Math.floor(width *.94f / ivcHandCards.size());
        //int cardSize= cardSizeH;
        //cardSize = ((cardSizeW*.3f*ivcHandCards.size() < cardSizeH) ? cardSizeW : cardSizeH);
        return cardSizeH;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    protected void generateHandCards() {
        ArrayList<ImageViewCard> temp = new ArrayList<ImageViewCard>();


        for (Card c:this.player.getHand())
            temp.add(new ImageViewCard(this, c));

        this.ivcHandCards = temp;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public boolean onDrag(View v, DragEvent event) {
        ImageViewCard view = (ImageViewCard) event.getLocalState();

        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                break;
            case DragEvent.ACTION_DROP:
                //TODO GameMech player layed Card (to get the (Hand)Card that was drooped -> view.getCard();)
                //-> player.playcard(view.toString());
                //PopUp to ask which color if color choice

                //view.getCard() = card that the player wants to play
                //TODO : check : is playcard compatible with playdeck card (-> player.checkIfCardsMatch(view.getCard(), this.player.playdeckTop))
                if(player.checkCard(view.getCard(), this.player.playdeckTop)) {
                    //TODO : Implement action card mechanics (game mechanics)
                    if (view.getCard().isActionCard() && (view.getCard().value == Card.values.CHOOSE_COLOR) || (view.getCard().value == Card.values.TAKE_FOUR))
                        this.playerColorChoice();

                    this.player.playCard(view.getCard());
                }



                Log.i(DEBUGTAG, " card drop name over ImageViewCard: " + view.toString());
                Log.i(DEBUGTAG, " card drop name over Card Object in ImageViewCard: " + view.getCard().get_name());

                break;
            case DragEvent.ACTION_DRAG_ENDED:
                initTimer();
                //TODO GUI + GameMech Say Uno
                                                                        if(this.player.hand.size() == 1) {
                                                                            //TODO offer so say uno

                                                                            //startet sprachaufzeichnung
                                                                            Record_Speech.recordSpeech(GameActivity.this);

                                                                            //wenn uno gesagt wurde
                                                                            if(_uno_said){
                                                                                _uno_said = false;
                                                                                boolean saysuno = true;
                                                                                if (saysuno) {
                                                                                    String sendstring = "p" + this.player.player_id + "uno11";
                                                                                    //this.sendMessage(sendstring);
                                                                                }else
                                                                                {
                                                                                    this.player.takeCard();
                                                                                }
                                                                            }



                                                                        } else if (this.player.hand.size() == 0) {

                                                                            //startet sprachaufzeichnung
                                                                            Record_Speech.recordSpeech(GameActivity.this);

                                                                            //wenn unouno gesagt wurde
                                                                            if(_uno_said){
                                                                                _uno_said = false;

                                                                                boolean saysuno = true;

                                                                                if (saysuno) {
                                                                                    //TODO offer to say unouno (Button)
                                                                                    String sendstring = "p" + this.player.player_id + "uno22";
                                                                                    //this.sendMessage(sendstring);
                                                                                }
                                                                                else
                                                                                {
                                                                                    this.player.takeCard();
                                                                                    //maybe TODO : implement so player takes 2 cards
                                                                                }
                                                                            }


                                                                        }
                break;
            default:
                break;
    }
        return true;
    }

    private void playerColorChoice() {

        Dialog d = new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT)
                .setTitle("Choose a Color")
                .setItems(new String[]{"Red", "Blue", "Yellow", "Green"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dlg, int position) {
                        if (position == 0) {
                           player.playdeckTop.color=Card.colors.RED;
                            dlg.cancel();
                        } else if (position == 1) {
                            player.playdeckTop.color=Card.colors.BLUE;
                            dlg.cancel();
                        } else if (position == 2) {
                            player.playdeckTop.color=Card.colors.YELLOW;
                            dlg.cancel();
                        } else if (position == 3) {
                            player.playdeckTop.color=Card.colors.GREEN;
                            dlg.cancel();
                        }
                    }
                })
                .create();
        d.setCanceledOnTouchOutside(false);
        d.show();

    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == event.ACTION_DOWN) {
            if (v.getId() != ivcStackCard.getId()) {
                Timer.cancel();
                ClipData cd = ClipData.newPlainText("", "");
                // draged image
                View.DragShadowBuilder sb = new View.DragShadowBuilder(v);
                v.startDrag(cd, sb, v, 0);
            } else if (v.getId() == ivcStackCard.getId()&& this.player.itsmyturn) {
                this.player.takeCard();
                removeHandCards();
                renderHandCards();
            }
        }
        return false;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private final SensorEventListener mSensorListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent se) {
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta; // perform low-cut filter
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~



    // The Handler that gets information back from the BluetoothChatService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == ActivityHelper.MESSAGE_STATE_CHANGE) {
                if (D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                switch (msg.arg1) {
                    case BluetoothService.STATE_CONNECTED:
                        break;
                    case BluetoothService.STATE_CONNECTING:
                        break;
                    case BluetoothService.STATE_LISTEN:
                    case BluetoothService.STATE_NONE:
                        break;
                }
            }
            if (msg.what == ActivityHelper.MESSAGE_WRITE) {
                byte[] writeBuf = (byte[]) msg.obj;
                // construct a string from the buffer
                String writeMessage = new String(writeBuf);
            }
            if (msg.what == ActivityHelper.MESSAGE_READ) {

                setGameObject((GameObject)msg.obj);
                Log.d(TAG,"game object : " + gameObject.toString());

                
            }
            if (msg.what == ActivityHelper.MESSAGE_DEVICE_NAME) {
                // save the connected device's name
                mConnectedDeviceName = msg.getData().getString(ActivityHelper.DEVICE_NAME);
                Toast.makeText(getApplicationContext(), "Connected to "
                        + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
            }
            if (msg.what == ActivityHelper.MESSAGE_TOAST) {
                //if (!msg.getData().getString(TOAST).contains("Unable to connect device")) {
                Toast.makeText(getApplicationContext(), msg.getData().getString(ActivityHelper.TOAST),
                        Toast.LENGTH_SHORT).show();
                //}
            }

            if(msg.what == ActivityHelper.MESSAGE_WRITE_OBJECT){

            }

            if(msg.what == ActivityHelper.MESSAGE_READ_OBJECT){
                setGameObject((GameObject)msg.obj);
                Log.d(TAG,"game object : " + gameObject.toString());
            }

        }
    };

    public GameObject getGameObject() {
        return gameObject;
    }

    public void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }

    /**
     * Sends a message.
     *
     * @param message A string of text to send.
     */
    /*protected void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mBltService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            String send = message;
            mBltService.write(send);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
        }
    }*/

    /**
     * Sends a GameObject.
     *
     * @param gameObject A GameObject to send.
     */
    protected void sendMessage(GameObject gameObject) {
        // Check that we're actually connected before trying anything
        if (mBltService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (!gameObject.equals(null)) {
            // Get the message bytes and tell the BluetoothChatService to write
            mBltService.write(gameObject);
            Log.d(TAG,"object send : " + gameObject.toString());
            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
        }
    }

    /**
     * Sends a object.
     *
     * @param go A string of text to send.
     */
    protected void sendObject(GameObject go) {
        // Check that we're actually connected before trying anything
        if (mBltService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (!go.equals(null)) {
            // Get the message bytes and tell the BluetoothChatService to write
            mBltService.write(go);
            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
        }
    }
    private  boolean _uno_said = false;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Record_Speech.SPEECH_RECOGNIZED){
            if(data != null){
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if(!result.isEmpty()){
                    if(result.get(0).toLowerCase().contains("uno") || result.get(0).toLowerCase().contains("u") || result.get(0).toLowerCase().contains("o")){
                        _uno_said = true;
                    }
                }
            }
        }
    }
}
