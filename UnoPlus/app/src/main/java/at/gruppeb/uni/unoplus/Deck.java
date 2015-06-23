package at.gruppeb.uni.unoplus;


import java.util.ArrayList;
import java.util.List;

public class Deck {
    public List<Card> deck;

    public Deck () {
        this.deck = new ArrayList<Card>();
    }

    public Card takeTopCard() {							// needs interface implement later!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        Card temp = this.deck.get(0);
        this.deck.remove(0);
        return temp;
    }

    public Card getTopCard() {
        return this.deck.get(0);
    }
    public boolean                  checkEmptyDeck(){
        if(this.deck.size()==0)
            return true;
        return false;
    }
}
