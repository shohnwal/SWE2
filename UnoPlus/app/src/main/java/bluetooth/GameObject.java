package bluetooth;

import java.util.ArrayList;
import java.util.Vector;

import at.gruppeb.uni.unoplus.Card;
import at.gruppeb.uni.unoplus.Deck;

/**
 * Created by Luki on 23.06.2015.
 */
public class GameObject {
    int current_player;
    int howManyCardsToTake;
    boolean turns_clockwise;
    boolean game_ended;
    boolean changed;
    Deck playdeck;
    Deck takedeck;
    Vector<ArrayList<Card>> handCards;

    public GameObject(int current_player, boolean turns_clockwise, boolean game_ended, Deck playdeck, Deck takedeck, Vector<ArrayList<Card>> handCards) {
        this.current_player = current_player;
        this.turns_clockwise = turns_clockwise;
        this.game_ended = game_ended;
        this.playdeck = playdeck;
        this.takedeck = takedeck;
        this.handCards = handCards;
        this.howManyCardsToTake=0;
        this.game_ended = false;
        this.changed = false;
    }

    public int getCurrent_player() {
        return current_player;
    }

    public boolean isTurns_clockwise() {
        return turns_clockwise;
    }

    public boolean isGame_ended() {
        return game_ended;
    }

    public Deck getPlaydeck() {
        return playdeck;
    }

    public Deck getTakedeck() {
        return takedeck;
    }

    public int getHowManyCardsToTake() {
        return howManyCardsToTake;
    }

    public Card getPlayDeckTopCard(){
        return this.playdeck.getTopCard();
    }

    public Card getTakeDeckTopCard(){
        return this.takedeck.getTopCard();
    }

    public boolean isChanged() {
        return changed;
    }

    public Card takeTakeDeckTopCard(){
        return this.takedeck.takeTopCard();
    }

    public int getNumPlayer(){
        return handCards.size();
    }

    public void setCurrent_player(int offset) {
        if (this.turns_clockwise == true) {
            this.current_player = (this.current_player + offset + 1) % this.getNumPlayer();
        } else if (this.turns_clockwise == false) {
            if (this.current_player == 0) {
                this.current_player = this.getNumPlayer() - 1 - offset;
            } else {
                this.current_player = (this.current_player - offset - 1) % this.getNumPlayer();
            }
        }
    }

    public ArrayList<Card> getHandcards(int playerNr){
        return handCards.get(playerNr);
    }

    public void setTurns_clockwise(boolean turns_clockwise) {
        this.turns_clockwise = turns_clockwise;
    }

    public void setGame_ended(boolean game_ended) {
        this.game_ended = game_ended;
    }

    public void setPlaydeck(Deck playdeck) {
        this.playdeck = playdeck;
    }

    public void setTakedeck(Deck takedeck) {
        this.takedeck = takedeck;
    }

    public void setHowManyCardsToTake(int howManyCardsToTake) {
        this.howManyCardsToTake = howManyCardsToTake;
    }

    public void setPlayDeckTop(Card card){
        this.playdeck.deck.add(0,card);
    }

    public void setHandcards(int playerNr,ArrayList<Card> handCards){
        this.handCards.set(playerNr,handCards);
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }
}