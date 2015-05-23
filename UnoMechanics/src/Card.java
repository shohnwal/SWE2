
public class Card {

	static enum 				colors { RED, BLUE, GREEN, YELLOW, BLACK };
	static enum 				values { ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, SKIP, TAKE_TWO, RETOUR, TAKE_FOUR, CHOOSE_COLOR };
	colors 						color;
	values 						value;
	boolean						actionCard = false;
	
	public 						Card (Card.colors color, Card.values value) {
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
	
	public String				get_name() {								// needs interface implement!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
									return ((this.color).toString() + "_" + (this.value).toString());
								}
	
	public boolean				isActionCard() {
									if (this.actionCard) { 
										return true; } 
									else { 
										return false; 
									}
								}
}




