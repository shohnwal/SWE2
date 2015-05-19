package at.gruppeb.uni.unoplus;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

/**
 * Created by Bernhard on 18.05.2015.
 */
public class MyService extends Service {

    MediaPlayer mp;
    private static boolean ONOFF = true;


    public static boolean isInstanceCreated() {
        return ONOFF;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        mp = MediaPlayer.create(this, R.raw.backgroundmusic);
        ONOFF = true;
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mp.setLooping(true);
        mp.setVolume(0.3f, 0.3f);
        mp.start();


        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        if (mp.isPlaying() && mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
        ONOFF = false;
        super.onDestroy();
    }

/*
    public void pauseMusic() {
        if (mp.isPlaying() && mp != null) {
            mp.pause();
            musicLength = mp.getCurrentPosition();

        }
    }

    public void resumeMusic() {
        if (mp.isPlaying() == false) {
            mp.seekTo(musicLength);
            mp.start();

        }
    }
*/
}
