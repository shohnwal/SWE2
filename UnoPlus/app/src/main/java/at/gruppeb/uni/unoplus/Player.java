package at.gruppeb.uni.unoplus;

import java.util.ArrayList;
import java.util.List;

import bluetooth.GameObject;


public class Player {
    ArrayList<Card> hand;
    public int player_id = 0;
    boolean itsmyturn = false; // TALK TO NATASHA ABOUT THAT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    GameActivity gameActivity;
    boolean isallowedtocheat = true;
    Card playdeckTop;


    public Player(int id,GameActivity gameActivity) {
        this.player_id = id;
        this.hand = new ArrayList<Card>();
        this.gameActivity=gameActivity;
        System.out.println("player " + this.player_id + " created");
    }

    public Card getPlaydeckTop() {
        if (this.playdeckTop == null) {
            System.out.println("Error : no playdeck top");
            Card errorcard = new Card(Card.colors.RED, Card.values.ZERO);
            return errorcard;
        }
        else {
            return this.gameActivity.gameObject.getPlayDeckTopCard();
        }

    }

    public List<Card>			getHand() {
        return this.hand;
    }

    protected void clientloop() {
        GameObject gameObject = this.gameActivity.getGameObject();

        if(gameObject != null){
            this.hand = gameObject.getHandcards(this.player_id);
            this.itsmyturn = gameObject.getCurrent_player()==this.player_id?true:false;
            this.playdeckTop = gameObject.getPlayDeckTopCard();
            if(itsmyturn){
                takeManyCards(gameObject.getHowManyCardsToTake());
            }
            this.gameActivity.currentPlayerID = gameObject.getCurrent_player();
        }
    }

    public void cheat() {
        int handsize_temp = this.hand.size();
        for (int i = 0; i < handsize_temp; i++) {
            this.gameActivity.gameObject.getTakedeck().deck.add(this.hand.get(0));
            this.hand.remove(0);
        }
        for (int i = 0; i < handsize_temp;i++) {
            this.hand.add(this.gameActivity.gameObject.takeTakeDeckTopCard());
        }
        this.isallowedtocheat = false;
    }

    public void takeManyCards(int anz){
        for(int i = 0; i<anz;i++){
            this.hand.add(this.gameActivity.gameObject.takeTakeDeckTopCard());
            this.gameActivity.gameObject.setCurrent_player(0);
        }
        this.gameActivity.gameObject.setHowManyCardsToTake(0);
    }

    public void takeCard() {				// take card from takedec
        this.hand.add(this.gameActivity.gameObject.takeTakeDeckTopCard());
        this.gameActivity.gameObject.setCurrent_player(0);
        endTurn();

    }

   public void playCard(Card card) {
       if (this.checkCard(card, this.playdeckTop)) {
           this.hand.remove(card);
           this.gameActivity.gameObject.setPlayDeckTop(card);
           if(card.value == Card.values.TAKE_TWO){
               this.gameActivity.gameObject.setHowManyCardsToTake(2);
           }
           if(card.value == Card.values.TAKE_FOUR){
               this.gameActivity.gameObject.setHowManyCardsToTake(4);
           }
           if(card.value == Card.values.RETOUR) {
               this.gameActivity.gameObject.changeTurns_clockwise();
           }
           this.gameActivity.gameObject.setCurrent_player(getPlaydeckTop().value == Card.values.SKIP ? 1 : 0);
           if (card.color != Card.colors.BLACK) {
               endTurn();
           }

       }

   }

    public void endTurn(){
        this.itsmyturn = false;
        this.gameActivity.gameObject.setChanged(true);
        this.gameActivity.gameObject.setHandcards(this.player_id, hand);
        GameObject go = this.gameActivity.gameObject;
        this.gameActivity.sendMessage(this.gameActivity.gameObject);
        this.gameActivity.sendMessage(this.gameActivity.gameObject);
    }

        //TODO : implement bool method that takes 2 cards (Card cardtoplay, Card playdecktop)
   public boolean checkCard(Card playCard,Card topCard){

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
