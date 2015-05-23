package at.gruppeb.uni.unoplus;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public class HostGame extends ActionBarActivity {

    private TextView hostName;
    String hostNameString;
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
        setContentView(R.layout.activity_host_game);

        hostName = (TextView) findViewById(R.id.textView_hostName);
        Intent i = getIntent();
        hostNameString = i.getStringExtra("hostName");
        hostName.setText(hostNameString);


    }

    @Override
    protected void onStart() {
        super.onStart();
        init();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //serviceStop();
    }

    @Override
    protected void onPause() {
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
        if(!(MyService.isInstanceCreated())){
            iBVolumeOn.setActivated(!iBVolumeOn.isActivated());
        }

        iBVolumeOn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                System.out.println("Volume");
                v.setActivated(!v.isActivated());
                if (v.isActivated()) {
                    serviceStop();
                    // myService.pauseMusic();

                } else {
                    serviceStart();
                    //myService.resumeMusic();
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