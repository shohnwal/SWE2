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
        System.out.println("creating player");
        this.player_id = id;
        this.hand = new ArrayList<Card>();
        this.gameActivity=gameActivity;
        System.out.println("player " + this.player_id + " created");
    }

    public void setPlaydeckCard(Card card) {
        this.playdeckTop.color = card.color;
        this.playdeckTop.value = card.value;
     /*   if (this.gameActivity.playdeckColor != this.playdeckTop.color) {
            this.playdeckTop.color = this.gameActivity.playdeckColor;
        }
        */
        //TODO SEND IT TO OTHER PLAYERS
        System.out.println("Playdeckcard set...");
    }

    public Card getPlaydeckTop() {
        if (this.playdeckTop == null) {
            System.out.println("Error : no playdeck top");
            Card errorcard = new Card(Card.colors.RED, Card.values.ZERO);
            return errorcard;
        }
        else {
            System.out.println("Playdeck top card is not null");
            return this.playdeckTop;
        }

    }

    public void prepareHand(int NumberOfPlayer){
        System.out.print("Preparing hand..." + this.player_id);
        this.hand.add(new Card(Card.colors.RED, Card.values.ZERO));

        if (this.gameActivity.stringList.size() > 0) {
            //String messagestring = this.gameActivity.stringList.get(0);
            System.out.println("incoming message : " + this.gameActivity.stringList.get(0));
            this.gameActivity.stringList.remove(0);
        }
         /*   int playernumber =Integer.parseInt(messagestring.substring(1, 2));
            if (playernumber == this.player_id) {
                String command = messagestring.substring(3,6);
                switch (command) {
                    case "get":
                        String color = messagestring.substring(6, 7);
                        String value = messagestring.substring(messagestring.length() - 1);
                        this.hand.add(new Card(Card.colors.RED, Card.values.ZERO));
                        break;
                    default:
                        break;
                }
            }
            this.gameActivity.stringList.remove(0);

        }*/

/*        String temp="p"+this.player_id;
        String superstring="";
        String cStr,color,value;
        for(int i=0;i<NumberOfPlayer-1;i++){
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
        } */
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
        System.out.println("Clientloop active " + this.player_id);
        if (this.player_id == 1) {
            for (int i = 0; i < 5; i++) {
                this.gameActivity.sendMessage("01");
            }
        }
        if (this.player_id == 0) {
            while (this.gameActivity.stringList.size() > 0) {
                System.out.println(this.gameActivity.stringList.get(0));
                this.gameActivity.stringList.remove(0);
            }
        }
/*
        //TODO : protected Card.colors playdeckColor; von gameactivity setzen, falls choosecolor gespielt wurde

    while (this.gameActivity.stringList.size() > 0) {
        System.out.print(this.gameActivity.stringList.get(0));
        String messagestring = this.gameActivity.stringList.get(0);
        if (messagestring.substring(0,5).equals("gamee")) {
            int winningPlayer= (int)messagestring.charAt(7);
            //TODO implement windows with the winning player
        }
        else if (messagestring.substring(0,5) == "playd") {
            String color = ""+messagestring.charAt(7);
            String value = ""+messagestring.charAt(8);
            Card tempcard = new Card(color, value);
            this.setPlaydeckCard(tempcard);

        }
        else {
            System.out.println("Errortest"  + messagestring);
            System.out.println("playernumber in string : " + messagestring.charAt(1));
            char playerchar = messagestring.charAt(1);
            int playernumber =(int)playerchar;
            String command = messagestring.substring(2,5);
            if (playernumber == this.player_id) {
                switch (command)
                {
                    case "get":
                        String color = ""+ messagestring.charAt(5);
                        String value = ""+ messagestring.charAt(6);
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
                        int unonr = (int)(messagestring.charAt(5));
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
    */
}

    public void setCurrentPlayerId(int id){
        this.gameActivity.currentPlayerID=id;
    }






    public void					takeCard (BluetoothService mBlt) {				// take card from takedec
        System.out.println("Player takes card");
        if(this.gameActivity.currentPlayerID==0){
            this.hand.add(this.gameActivity.game.takedeck.deck.get(0));
            this.gameActivity.game.takedeck.deck.remove(0);
            this.gameActivity.sendMessage(this.gameActivity.game.getEndTurnString(0));
            System.out.println("take card method, endturnstring : " + this.gameActivity.game.getEndTurnString(0));
        }
        else {
            String sendstring = "p" + this.player_id + "tak";
            this.gameActivity.sendMessage(sendstring);
            System.out.println("take card method, endturnstring : " + sendstring);
            this.gameActivity.sendMessage(this.gameActivity.game.getEndTurnString(0));
        }
    }

   public void					playCard (Card card) {
       System.out.println("Player plays card...");
        if(player_id==0){
            this.gameActivity.game.playdeck.deck.add(card);
            this.gameActivity.player.setPlaydeckCard(card);
            this.hand.remove(card);
            if(card.value == Card.values.SKIP) {
                this.gameActivity.sendMessage(this.gameActivity.game.getEndTurnString(1));
                System.out.println("play card method, endturnstring : " + this.gameActivity.game.getEndTurnString(1));
            }
            else {
                this.gameActivity.sendMessage(this.gameActivity.game.getEndTurnString(0));
                System.out.println("play card method, endturnstring : " + this.gameActivity.game.getEndTurnString(1));
            }


        }else {
            String cStr = "";
            cStr += "p" + this.player_id + "ply";
            if (card.color != Card.colors.BLACK) {
                cStr += card.toString().substring(0, 1);
            } else {
                cStr += 'S';
            }
            int Ord = card.value.ordinal();
            if (Ord <= 9) {
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
