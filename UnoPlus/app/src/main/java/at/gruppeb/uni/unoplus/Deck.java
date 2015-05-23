package at.gruppeb.uni.unoplus;


import java.util.ArrayList;
import java.util.List;

public class Deck {
	List<Card>		 				deck;
	
	public 							Deck () {
										this.deck = new ArrayList<Card>();
									}
	
	public Card						getTopCard() {							// needs interface implement later!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
										Card temp = this.deck.get(0);
										this.deck.remove(0);
										return temp;
									}
}
