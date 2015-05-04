package classes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luki on 18.03.2015.
 */
public class Player implements interfaces.IPlayer {
    private String _name;
    private List<Card> _hand;

    public Player(String _name) {
        this._name = _name;
        this._hand = new ArrayList<Card>();
    }

    @Override
    public List<Card> getHand() {
        return this._hand;
    }

    @Override
    public void getCard(Card card) {
        this._hand.add(card);
    }

    @Override
    public Card layCard() {
        Card help = this._hand.get(0);
        this._hand.remove(0);
        return help;
    }
}
