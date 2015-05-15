package at.gruppeb.uni.unoplus;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
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
    private MediaPlayer backgroundmusic = null;
    private int musicLength = 0;

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
        init();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (backgroundmusic != null) {
            try {
                backgroundmusic.stop();
                backgroundmusic.release();
            } finally {
                backgroundmusic = null;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (backgroundmusic != null) {
            pauseMusic();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumeMusic();
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
        setupBtnSpielErstellen();
        setupBtnSpielBeitreten();
        setupImageButtonSetting();
        setupImageButtonVolume();
        startMusic();
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
        iBVolumeOn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("Volume");
                v.setActivated(!v.isActivated());
                if(v.isActivated()){
                    pauseMusic();
                }else{
                    resumeMusic();
                }
            }
        });
    }

    public void spielErstellenDialog() {

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

                Toast.makeText(getBaseContext(), input.getText()+" erstellten...!!", Toast.LENGTH_SHORT).show();

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

    public void startMusic() {
        if (backgroundmusic == null) {
            backgroundmusic = MediaPlayer.create(this, R.raw.backgroundmusic);
        }
        //restart playback end reached
        backgroundmusic.setLooping(true);
        backgroundmusic.setVolume(0.3f, 0.3f);
        backgroundmusic.start();


    }

    public void stopMusic() {
        if (backgroundmusic.isPlaying() && backgroundmusic != null) {
            backgroundmusic.stop();
            backgroundmusic.release();
            backgroundmusic = null;
        }
    }

    public void pauseMusic() {
        if (backgroundmusic.isPlaying() && backgroundmusic != null) {
            backgroundmusic.pause();
            musicLength = backgroundmusic.getCurrentPosition();
        }
    }

    public void resumeMusic() {
        if (backgroundmusic.isPlaying() == false) {
            backgroundmusic.seekTo(musicLength);
            backgroundmusic.start();
        }
    }

}
