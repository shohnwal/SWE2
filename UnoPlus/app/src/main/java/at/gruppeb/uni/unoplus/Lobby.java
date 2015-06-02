package at.gruppeb.uni.unoplus;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

    private Button btnCreateGame;
    private Button btnJoinGame;
    private ImageButton iBSetting;
    private ImageButton iBVolumeOn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set the Screen in Landscape mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //Remove the Title Bar
        getSupportActionBar().hide();
        //Remove the notification Bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_lobby);
        serviceStart();

    }

    @Override
    protected void onStart() {
        super.onStart();
        init();

    }

    @Override
    public void onDestroy() {
        serviceStop();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (MyService.isInstanceCreated()) {
            serviceStop();
        }
        super.onPause();

    }

    @Override
    protected void onResume() {
        if (MyService.isInstanceCreated()) {
            serviceStart();
        }
        super.onResume();


    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder al = new AlertDialog.Builder(Lobby.this);
        al.setTitle("Beenden!");
        al.setMessage("Wollen Sie das Spiel wirklich beenden?");
        al.setPositiveButton("Ja", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        al.setNegativeButton("Nein", null);
        al.show();
    }

    public void init() {
        setupBtnCreateGame();
        setupBtnJoinGame();
        setupImageButtonSetting();
        setupImageButtonVolume();

    }

    public void setupBtnCreateGame() {
        btnCreateGame = (Button) findViewById(R.id.button_start);
        btnCreateGame.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("Create Game");
                CreateGameDialog();
            }
        });
    }


    public void setupBtnJoinGame() {
        btnJoinGame = (Button) findViewById(R.id.button_join);
        btnJoinGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("Join Game");
                startActivity(new Intent("at.gruppeb.uni.unoplus.JoinGame"));
            }
        });

    }

    private void setupImageButtonSetting() {
        iBSetting = (ImageButton) findViewById(R.id.imageButton_settings);
        iBSetting.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("Settings");
            }
        });
    }

    public void setupImageButtonVolume() {
        iBVolumeOn = (ImageButton) findViewById(R.id.imageButton_volumeOn);
        if (!(MyService.isInstanceCreated())) {
            iBVolumeOn.setActivated(true);
        } else {
            iBVolumeOn.setActivated(false);
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

    public void CreateGameDialog() {

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

                Toast.makeText(getBaseContext(), input.getText() + " erstellen...", Toast.LENGTH_SHORT).show();
                Intent nextScreen = new Intent("at.gruppeb.uni.unoplus.HostGame");
                //Sending the Host- Player- name to the new Activity
                nextScreen.putExtra("hostName", input.getText().toString());
                startActivity(nextScreen);

            }
        });
        alertDialog.setNegativeButton("Abbruch", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(getBaseContext(), "Abbruch!", Toast.LENGTH_SHORT).show();


            }
        });

        alertDialog.show();

    }

    public void serviceStart() {
        startService(new Intent(this, MyService.class));
    }

    public void serviceStop() {
        stopService(new Intent(this, MyService.class));
    }

}
