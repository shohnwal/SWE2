package at.gruppeb.uni.unoplus;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Deck implements Serializable {
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

    @Override
    public String toString() {
        return "Deck{" +
                "deck=" + deck +
                '}';
    }
}
