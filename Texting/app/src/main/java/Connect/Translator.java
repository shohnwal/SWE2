package Connect;

import Uno.Card;
import at.gruppeb.uni.server.BluetoothChatService_Server;
import at.gruppeb.uni.server.BluetoothChat_Server;

/**
 * Created by Luki on 05.05.2015.
 */
public class Translator {

    BluetoothChat_Server btc = new BluetoothChat_Server();
   // BluetoothChatService_Server btcs = new BluetoothChatService_Server();
    Card c;
    public Translator(){
    }

    public boolean translate(String toTranslate){
        switch (toTranslate.split(";")[0] + ";" + toTranslate.split(";")[1]){
            case "send;getHandCards" : //send;getHandCards;Player

                return false;
            case "send;discardingHandCards": //send;discardingHandCards;Player;<card>

                return false;
            case "send;getCurrentOpenCard" : //send;getCurrentOpenCard;<card>

                return false;
            case "send;setCurrentOpenCard" : //send;setCurrentOpenCard;<card>

                return false;
            case "send;sayUNO" : //send;sayUNO;Player

                return false;
            case "send;currentPlayer" : //send;currentPlayer;Player

                return false;
            case "send;setNextPlayer" : //send;setNextPlayer;Player

                return false;
            case "send;getNextPlayer" : //send;getNextPlayer;Player

                return false;
            case "send;changePlayDirection" : //send;changePlayDirection;Player

                return false;
            case "send;stepOverNextPlayer" : //send;stepOverNextPlayer

                return false;
            case "receive;getHandCards;Player" :

                return false;
            case "receive;discardingHandCards":

                return false;
            case "receive;getCurrentOpenCard" :

                return false;
            case "receive;setCurrentOpenCard" :

                return false;
            case "receive;sayUNO" :

                return false;
            case "receive;currentPlayer" :

                return false;
            case "receive;setNextPlayer" :

                return false;
            case "receive;getNextPlayer" :

                return false;
            case "receive;changePlayDirection" :

                return false;
            case "receive;stepOverNextPlayer" :

                return false;
            default : return false;
        }
    }

    private Card translateCardReceive(String translate){
        return new Card(translate.split(",")[0],translate.split(",")[1],translate.split(",")[2].equals("true")?true:false);
    }

    private String translateCardSend(Card translate){
        return translate.toString();
    }
}
