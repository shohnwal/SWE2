package at.gruppeb.uni.unoplus;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;


/**
 * Created by Bernhard on 13.05.2015.
 */
public class Lobby extends ActionBarActivity {

    private Button btnSpielErstellen;
    private Button btnSpielBeitreten;
    private ImageButton iBSetting;
    private ImageButton iBVolumeOn;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set the Screen in Landscape mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //Remove the Title Bar
        getSupportActionBar().hide();
        //Remove the notification Bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_lobby);
        init();

    }

    public void init() {
        setupBtnSpielErstellen();
        setupBtnSpielBeitreten();
        setupImageButtonSetting();
        setupImageButtonVolume();
    }

    public void setupBtnSpielErstellen() {
        btnSpielErstellen = (Button) findViewById(R.id.button_start);
        btnSpielErstellen.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("Spiel Erstellen");
                spielErstellenDialog();


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

    private void setupImageButtonSetting(){
        iBSetting = (ImageButton) findViewById(R.id.imageButton_settings);
        iBSetting.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("Settings");
            }
        });
    }

    public void setupImageButtonVolume(){
        iBVolumeOn = (ImageButton) findViewById(R.id.imageButton_volumeOn);
        iBVolumeOn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("Volume");
                v.setActivated(!v.isActivated());
            }
        });
    }

    public void spielErstellenDialog(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Lobby.this);
        alertDialog.setTitle("Neue Spielrunde erstellen");
        alertDialog.setMessage("Spielname:");
        final EditText input = new EditText(Lobby.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setPositiveButton("Los gehts", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(getBaseContext(), "Spiel erstellt!!", Toast.LENGTH_SHORT).show();

            }
        });
        alertDialog.setNegativeButton("Abbruch", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(getBaseContext(), "Abbruch!!", Toast.LENGTH_SHORT).show();


            }
        });

        alertDialog.show();

    }

}
