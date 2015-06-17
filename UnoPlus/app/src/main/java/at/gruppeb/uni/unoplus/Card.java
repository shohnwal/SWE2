package at.gruppeb.uni.unoplus;

public class Card {

	static enum 				colors { RED, BLUE, GREEN, YELLOW, BLACK };
	static enum 				values { ZERO, ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, SKIP, TAKE_TWO, RETOUR, TAKE_FOUR, CHOOSE_COLOR };
	colors 						color;
	values 						value;
	boolean						actionCard = false;
	
	public Card(Card.colors color, Card.values value) {
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
	public Card (String color,String value){
		switch(color){
			case R: this.color=RED;
				break;
			case Y: this.color=YELLOW;
				break;
			case B: this.color=BLUE;
				break;
			case G: this.color=GREEN;
				break;
			case S: this.color=BLACK;
		}
		switch(value){
			case 0: this.value=ZERO;
				break;
			case 1: this.value=ONE;
				break;
			case 2: this.value=TWO;
				break;
			case 3: this.value=THREE;
				break;
			case 4: this.value=FOUR;
				break;
			case 5: this.value=FIVE;
				break;
			case 6: this.value=SIX;
				break;
			case 7: this.value=SEVEN;
				break;
			case 8: this.value=EIGHT;
				break;
			case 9: this.value=NINE;
				break;
			case S: this.value=SKIP;
				break;
			case X: this.value=TAKE_TWO;
				break;
			case R: this.value=RETOUR;
				break;
			case Y: this.value=TAKE_FOUR;
				break;
			case C: this.value=CHOOSE_COLOR;
				break;
		}
	}
	
	public String get_name() {								// needs interface implement!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
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




