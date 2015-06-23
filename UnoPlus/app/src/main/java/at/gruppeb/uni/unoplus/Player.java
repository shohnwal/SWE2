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
            return this.playdeckTop;
        }

    }

    public void prepareHand(){

    }


    public List<Card>			getHand() {
        return this.hand;
    }

    protected void clientloop() {

    }

    public void					takeCard (BluetoothService mBlt) {				// take card from takedec

    }

   public void					playCard (Card card) {

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


}
