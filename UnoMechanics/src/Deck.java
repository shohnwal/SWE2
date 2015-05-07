import java.util.ArrayList;

public class Deck {
	ArrayList<Card> 				deck;
	
	public 							Deck () {
										this.deck = new ArrayList<Card>();
									}
	
	public Card						getTopCard() {							// needs interface implement later!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
										Card temp = this.deck.get(0);
										this.deck.remove(0);
										return temp;
									}
}
