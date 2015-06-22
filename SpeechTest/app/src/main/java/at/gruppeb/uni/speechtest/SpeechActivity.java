package at.gruppeb.uni.speechtest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import Dictate.Record_Speech;

public class SpeechActivity extends ActionBarActivity {

    public TextView speechtxt;

    public Vibrator vibrator;
    public boolean vibrate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);

        final Button speech_record = (Button)findViewById(R.id.record_speech);
        speechtxt = (TextView)findViewById(R.id.txt_speech);
        speech_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Record_Speech.recordSpeech(SpeechActivity.this);
            }
        });



        Button btn_vibrate = (Button)findViewById(R.id.btn_vibrate);
        btn_vibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(vibrate){
                    vibrate = false;
                    //Set the pattern for vibration
                    long pattern[]={0,50,100,200,300,400,500,600};

                    //Start the vibration
                    vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                    //start vibration with repeated count, use -1 if you don't want to repeat the vibration
                    vibrator.vibrate(pattern, 0);
                }
                else{
                    vibrate = true;
                    vibrator.cancel();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_speech, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Record_Speech.SPEECH_RECOGNIZED){
            if(data != null){
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if(!result.isEmpty()){
                    speechtxt.setText(result.get(0));
                }
            }
        }
    }
}
