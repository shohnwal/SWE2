package IUno;

import java.util.List;

import Uno.Card;

/**
 * Created by Luki on 18.03.2015.
 */
public interface IPlayer {

    public List<Card> getHand();

    public void getCard(Card card);

    public Card layCard();

}
