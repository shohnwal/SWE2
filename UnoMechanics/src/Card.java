
public class Card {

	static enum 				colors { RED, BLUE, GREEN, YELLOW, BLACK };
	static enum 				values { ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, SKIP, TAKE_TWO, RETOUR, TAKE_FOUR, CHOOSE_COLOR };
	colors 						color;
	values 						value; 
	
	public 						Card (Card.colors color, Card.values value) {
									this.color = color;
									this.value = value;
								}
	
	public String				get_name() {								// needs interface implement!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
									return ((this.color).toString() + "_" + (this.value).toString());
								}
	
	public boolean				isActionCard() {
									if (this.color == Card.colors.BLACK || this.value == Card.values.SKIP || this.value == Card.values.TAKE_TWO || this.value == Card.values.RETOUR) {
										return true; }
									else {
										return false; }
								}
}




