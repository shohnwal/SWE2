package Dictate;

import android.app.Activity;
import android.content.Intent;
import android.speech.RecognizerIntent;

import at.gruppeb.uni.unoplus.GameActivity;

/**
 * Created by Luki on 22.06.2015.
 */
public class Record_Speech {

    public static final int SPEECH_RECOGNIZED = 1;

    public static void recordSpeech(Activity a){
        Intent intent = new Intent();
        intent.setAction(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        a.startActivityForResult(intent, SPEECH_RECOGNIZED);
        GameActivity.recordspeedwasexecuted = true;
    }
}
