package at.gruppeb.uni.unoplus;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;


public class JoinGame extends ActionBarActivity {


    private ImageView iBVolumeOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set the Screen in Landscape mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //Remove the Title Bar
        getSupportActionBar().hide();
        //Remove the notification Bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_join_game);
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
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
        iBVolumeOn = (ImageButton) findViewById(R.id.imageButton_join);
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

    public void serviceStart() {
        startService(new Intent(this, MyService.class));
    }

    public void serviceStop() {
        stopService(new Intent(this, MyService.class));
    }


}
