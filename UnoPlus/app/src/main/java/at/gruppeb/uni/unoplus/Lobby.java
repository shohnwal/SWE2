package at.gruppeb.uni.unoplus;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

/**
 * Created by Bernhard on 13.05.2015.
 */
public class Lobby extends ActionBarActivity {

    private Button btnSpielErstellen;
    private Button btnSpielBeitreten;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getSupportActionBar().hide();
        //Error:
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Remove the notification Bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_lobby);


    }

    public void init() {
        setupBtnSpielErstellen();
        setupBtnSpielBeitreten();
    }

    public void setupBtnSpielErstellen() {
        btnSpielErstellen = (Button) findViewById(R.id.button_start);
        btnSpielErstellen.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                System.out.println("Spiel Erstellen");
                //TODO ListView Popup
            }
        });


    }

    public void setupBtnSpielBeitreten() {
        btnSpielBeitreten = (Button) findViewById(R.id.button_join);
        btnSpielBeitreten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("Spiel Beitreten");
            }
        });

    }




}
