package at.gruppeb.uni.unoplus;

import android.util.Log;

public class Card {

	static enum 				colors { RED, BLUE, GREEN, YELLOW, BLACK;
									public static colors getRandom() {
									return values()[(int) (Math.random() * values().length)];
								}}
	static enum 				values { ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, SKIP, TAKE_TWO, RETOUR, TAKE_FOUR, CHOOSE_COLOR;
									public static values getRandom() {
										return values()[(int) (Math.random() * values().length)];
								}}
	colors 						color;
	values 						value;
	boolean						actionCard = false;
	private static final String DEBUGTAG = "at.gruppeb.uni.unoplus";


	public Card(Card.colors color, Card.values value) {
									Log.i(DEBUGTAG, " card created ");
									this.color = color;
									this.value = value;
									if 	(this.color.equals(Card.colors.BLACK)
										|| this.value.equals(Card.values.SKIP)
										|| this.value.equals(Card.values.TAKE_TWO)
										|| this.value.equals(Card.values.RETOUR)) {
										this.actionCard = true;
									} else {
										this.actionCard = false;
									}
								}

	
	public String get_name() {
		return ((this.color).toString() + "_" + (this.value).toString());
	}
	
	public boolean	isActionCard() {
			return this.actionCard;
	}

}






