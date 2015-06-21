package at.gruppeb.uni.unoplus;

import java.util.ArrayList;
import java.util.List;

import at.gruppeb.uni.unoplus.Card;
import bluetooth.BluetoothService;


public class Player {

    List<Card> hand;
    public int player_id = 0;
    boolean itsmyturn = false; // TALK TO NATASHA ABOUT THAT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    GameActivity gameActivity;
    Card playdeckTop;


    public Player(int id,GameActivity gameActivity) {
        this.player_id = id;
        this.hand = new ArrayList<Card>();
        this.gameActivity=gameActivity;
    }

    public void setPlaydeckCard(String color, String value) {
        this.playdeckTop = new Card(color, value);
    }

    public void prepareHand(int NumberOfPlayer){
        String temp="p"+this.player_id;
        String superstring="";
        String cStr,color,value;
        for(int i=0;i<NumberOfPlayer-2;i++){
            if(temp==(this.gameActivity.stringList.get(i).substring(0,2))){
                superstring=this.gameActivity.stringList.get(i).substring(2,this.gameActivity.stringList.get(i).length());
                this.gameActivity.stringList.remove(i);
            } else this.gameActivity.stringList.remove(i);
        }
        while(superstring.length() > 0){
            cStr=superstring.substring(0,2);
            color = cStr.substring(0,1);
            value = cStr.substring(1,2);
            this.hand.add(new Card (color,value));
            superstring=superstring.substring(2);
        }
    }


    public List<Card>			getHand() {
        return this.hand;
    }

    public Card					layCard() {
        Card help = this.hand.get(0);
        this.hand.remove(0);
        return help;
    }

    protected void clientloop() {

        //TODO : protected Card.colors playdeckColor; von gameactivity setzen, falls choosecolor gespielt wurde

        while (this.gameActivity.stringList.size() > 0) {
            String messagestring = this.gameActivity.stringList.get(0);
            if (messagestring.substring(0,8).equals("gameend")) {
                int winningPlayer= Integer.parseInt(messagestring.substring(messagestring.length()-1));
                //TODO implement windows with the winning player
            }
            else if (messagestring.substring(0,5) == "playd") {
                String color = messagestring.substring(8,9);
                String value = messagestring.substring(messagestring.length()-1);
                this.setPlaydeckCard(color, value);

            }
            else {
                 int playernumber =Integer.parseInt(messagestring.substring(1,2));
                String command = messagestring.substring(3,6);
                if (playernumber == this.player_id) {
                    switch (command)
                    {
                        case "get":
                            String color = messagestring.substring(6,7);
                            String value = messagestring.substring(messagestring.length()-1);
                            this.hand.add( new Card(color, value) );
                            break;
                        case "set":
                            this.itsmyturn = true;
                            this.setCurrentPlayerId(playernumber);
                            break;
                        case "ply":
                            break;
                        case "tak":
                            break;
                        case "uno":
                            int unonr = Integer.parseInt(messagestring.substring(messagestring.length()-1));
                            if (unonr == 1) {
                                //..
                            } else if (unonr == 2) {
                                //...
                            }
                        default:break;
                    }
                } else {
                    switch (command) {
                        case "set":
                            this.itsmyturn = false;
                            this.setCurrentPlayerId(playernumber);
                            break;
                        default: break;
                    }
                }


            }
            this.gameActivity.stringList.remove(0);
        }


    }

    public void setCurrentPlayerId(int id){
        this.gameActivity.currentPlayerID=id;
    }






    public void					takeCard (BluetoothService mBlt) {				// take card from takedec
        if(this.gameActivity.currentPlayerID==0){
            this.hand.add(this.gameActivity.game.takedeck.deck.get(0));
            this.gameActivity.game.takedeck.deck.remove(0);
            this.gameActivity.sendMessage(this.gameActivity.game.getEndTurnString(0));
        }
        else {
            String sendstring = "p" + this.player_id + "tak";
            this.gameActivity.sendMessage(sendstring);
            this.gameActivity.sendMessage(this.gameActivity.game.getEndTurnString(0));
        }
    }

   public void					playCard (Card card) {
        if(player_id==0){
            this.gameActivity.game.playdeck.deck.add(card);
            if(!this.hand.remove(card)){
                //problem to play the card
            }
            if(card.value == Card.values.SKIP) {
                this.gameActivity.sendMessage(this.gameActivity.game.getEndTurnString(1));
            }
            else
            this.gameActivity.sendMessage(this.gameActivity.game.getEndTurnString(0));

        }else {
            String cStr = "";
            cStr += "p" + this.player_id + "ply";
            if (card.color != Card.colors.BLACK) {
                cStr += card.toString().substring(0, 1);
            } else {
                cStr += 'S';
            }
            int Ord = card.value.ordinal();
            if (Ord >= 9) {
                cStr += Ord;
            } else if (Ord == 10) {
                cStr += 'S';
            } else if (Ord == 11) {
                cStr += 'X';
            } else if (Ord == 12) {
                cStr += 'R';
            } else if (Ord == 13) {
                cStr += 'Y';
            } else if (Ord == 14) {
                cStr += 'C';
            }
            this.removeCardFromPlayer(card);
            //TODO : REMOVE CARD FROM HAND, ASK NATASHA!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

            this.gameActivity.sendMessage(cStr);
        }
   }
















        //TODO : implement bool method that takes 2 cards (Card cardtoplay, Card playdecktop)
        public boolean CheckCard(Card playCard,Card topCard){

        if (playCard.color == topCard.color) {					// if color matches
            return true;
        } else if (playCard.value== topCard.value) {			// if amount matches
            return true;
        } else if (playCard.color == Card.colors.BLACK) {
            return true;
        }
            //System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!That's not possible!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        return false;
        }

    public void					removeCardFromPlayer(Card card) {
        for (Card i : this.hand) {
            if ((i.color == card.color) && (i.value == card.value)) {
                this.hand.remove(this.hand.indexOf(i));
                break;
            }
        }
    }

}
