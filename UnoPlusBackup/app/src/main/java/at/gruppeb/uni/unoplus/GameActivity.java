package at.gruppeb.uni.unoplus;

import android.content.ClipData;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.nfc.Tag;
import android.os.Bundle;
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
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Natascha on 28/05/2015.
 */
public class GameActivity extends ActionBarActivity implements View.OnTouchListener, View.OnDragListener {


    private static final String TAG = "at.gruppeb.uni.unoplus";

    private ImageView ivcCurrentCard, ivcStackCard;
    private ImageView[] ivPlayers;
    private ArrayList<ImageViewCard> ivcHandCards;
    private ViewGroup ivCurrentCardParent;
    private Timer Timer;
    private int playerId, height, width, NumberOfPlayers;
    //TODO GUI grey out HandCards not curentPlayer
    private boolean currentPlayer;

    private SensorManager mSensorManager;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set the Screen in Landscape mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //Remove the Title Bar
        getSupportActionBar().hide();
        //Remove the notification Bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_game);
    }

    @Override
    protected void onStart() {
        super.onStart();
        init();

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
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void init() {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) this.getApplicationContext().getSystemService(Context.WINDOW_SERVICE); // the results will be higher than using the activity context object or the getWindowManager() shortcut
        wm.getDefaultDisplay().getMetrics(displayMetrics);

        this.setHeight(displayMetrics.heightPixels);
        this.setWidth(displayMetrics.widthPixels);

        //TODO get id from Lobby
        playerId = 0;
        //TODO  if this player is curentPlayer from Lobby/GameMech
        currentPlayer = false;


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
            removeAllViews();
            renderAllViews();

            Log.i(TAG, "loop refresh");
            //TODO finde where to check uno was shaked
            if (mAccel>5){
                Log.i(TAG, "shake it!!! ~(^o^~) (~^o^)~");
            }
            //Do something to the UI thread here

        }
    };

    private void initShakeListener() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
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
        //TODO get number of players from Lobby/GameMech
        NumberOfPlayers = 4;


        if (NumberOfPlayers == 2) {
            ivPlayers = new ImageView[1];
            ivPlayers[0] = (ImageView) findViewById(R.id.iview_playerOne);
        } else if (NumberOfPlayers == 3) {
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

        for (int i = 0; i < ivPlayers.length; i++) {
            ivPlayers[i].setVisibility(View.VISIBLE);
            ivPlayers[i].setX(ivPlayers[i].getX() + i * 100);
//TODO GUI Set position & size of other players
            /*ivPlayers[i].layout(Math.round(this.width / NumberOfPlayers),  (int) Math.round(this.height * .99f), Math.round((i + 1) * this.width / NumberOfPlayers), (int) Math.round(this.height * .99f));
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
        renderPlayers();
        //renderCurrentPlayer();

    }

    private void renderPlayers() {
        //TODO get currentPlayerId & name from Lobby/GameMech
        //TODO GUI highlight player

    }

    private void renderCurrentCard() {
        //TODO get CurrentCard from GameMech and set it as viewBackground (red 1 = test/debug)
        ivcCurrentCard.setBackground(ImageViewCard.getDrawableForCard(new Card(Card.colors.RED, Card.values.ONE), ivcCurrentCard));
    }

    private void renderHandCards() {
        //TODO GameMech Get Handcards (generateHandCards = debug)
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
            ivc.setOnTouchListener(this);
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
    int f = 0;

    protected void generateHandCards() {
        ArrayList<ImageViewCard> temp = new ArrayList<ImageViewCard>();

        //TODO get GameMech PlayerHandcards
        //from here...
        Card cr1 = new Card(Card.colors.BLUE, Card.values.TAKE_TWO), cr2 = new Card(Card.colors.RED, Card.values.TWO), cg1 = new Card(Card.colors.GREEN, Card.values.ONE), cg2 = new Card(Card.colors.GREEN, Card.values.TWO);
        Card cb1 = new Card(Card.colors.BLUE, Card.values.ZERO), cy1 = new Card(Card.colors.YELLOW, Card.values.ONE);
        Card csr = new Card(Card.colors.RED, Card.values.SKIP), cy2 = new Card(Card.colors.RED, Card.values.TWO);
        Card cb3 = new Card(Card.colors.RED, Card.values.THREE), cg3 = new Card(Card.colors.YELLOW, Card.values.THREE), cr3 = new Card(Card.colors.GREEN, Card.values.THREE), cy3 = new Card(Card.colors.BLUE, Card.values.THREE);
        Card cb4 = new Card(Card.colors.GREEN, Card.values.FOUR), cg4 = new Card(Card.colors.YELLOW, Card.values.FOUR), cr4 = new Card(Card.colors.BLUE, Card.values.FOUR), cy4 = new Card(Card.colors.GREEN, Card.values.FOUR);


        Card[] testHCSet = new Card[4];
        if (f % 5 == 0) {
            testHCSet = new Card[6];
            testHCSet[0] = cr1;
            testHCSet[1] = cr2;
            testHCSet[2] = cg1;
            testHCSet[3] = cg2;
            testHCSet[4] = csr;
            testHCSet[5] = cy1;

        } else if (f % 5 == 1) {
            testHCSet = new Card[15];
            testHCSet[0] = cb1;
            testHCSet[1] = cr1;
            testHCSet[2] = cg1;
            testHCSet[3] = cy1;
            testHCSet[4] = cr1;
            testHCSet[5] = cr2;
            testHCSet[6] = cg1;
            testHCSet[7] = cg2;
            testHCSet[8] = cg1;
            testHCSet[9] = cy1;
            testHCSet[10] = cb1;
            testHCSet[11] = cr1;
            testHCSet[12] = cg1;
            testHCSet[13] = cy1;
            testHCSet[14] = cr1;

        } else if (f % 5 == 2) {
            testHCSet = new Card[1];

            testHCSet[0] = cg2;

        } else if (f % 5 == 3) {

            testHCSet[2] = cy3;
            testHCSet[3] = cg3;
            testHCSet[0] = cb3;
            testHCSet[1] = cr3;
        } else if (f % 5 == 4) {

            testHCSet = new Card[20];
            testHCSet[0] = cb1;
            testHCSet[1] = cr1;
            testHCSet[2] = cg1;
            testHCSet[3] = cy1;
            testHCSet[4] = cr1;
            testHCSet[5] = cr2;
            testHCSet[6] = cg1;
            testHCSet[7] = cg2;
            testHCSet[8] = cg1;
            testHCSet[9] = cy1;
            testHCSet[10] = cb1;
            testHCSet[11] = cr1;
            testHCSet[12] = cg1;
            testHCSet[13] = cy1;
            testHCSet[14] = cr1;
            testHCSet[15] = cr2;
            testHCSet[16] = cg1;
            testHCSet[17] = cg2;
            testHCSet[18] = cg1;
            testHCSet[19] = cy1;
        }
        f++;
        if (f > 5) f = 1;

        //... to here Debug test cards

        for (int i = 0; i < testHCSet.length; i++) {
            // Log.i(TAG, "genCard test" + i + "" + testHCSet[i].get_name());
            // Log.i(TAG, testHCSet[i].toString());
            temp.add(new ImageViewCard(this, testHCSet[i]));
        }
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
                //TODO GUI add PopUp to ask which color if color choice

                Log.i(TAG, " card drop name over ImageViewCard: " + view.toString());
                Log.i(TAG, " card drop name over Card Object in ImageViewCard: " + view.getCard().get_name());

                break;
            case DragEvent.ACTION_DRAG_ENDED:
                initTimer();
                //TODO GUI + GameMech Say Uno

                break;
            default:
                break;
        }
        return true;
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
                Log.i(TAG, "Touch HandCard");
            } else if (v.getId() == ivcStackCard.getId()) {
                Log.i(TAG, "Not Touch Hand Card! StackCard to pull");
                //TODO GameMech pull card
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

}

