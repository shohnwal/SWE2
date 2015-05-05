package Connect;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import Uno.Card;
import at.gruppeb.uni.texting.BluetoothChat;
import at.gruppeb.uni.texting.BluetoothChatService;

/**
 * Created by Luki on 05.05.2015.
 */
public class Translator {

    BluetoothChat btc = new BluetoothChat();

    public Translator(){
    }

    public boolean translate(String toTranslate){
        switch (toTranslate){
            case "send;getHandCards" : return false;
            case "send;discardingHandCards;<card>": return false;
            case "receive;getHandCards" : return false;
            case "receive;discardingHandCards;<card>": return false;
            default : return false;
        }
    }

    private Card translateCardGet(String translate){
        return new Card(translate.split(",")[0],translate.split(",")[1],translate.split(",")[2].equals("true")?true:false);
    }

    private String translateCardSet(Card translate){
        return translate.toString();
    }
}
