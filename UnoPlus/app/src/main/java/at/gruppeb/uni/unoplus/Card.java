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
			case "R": this.color=Card.colors.RED;
				break;
			case "Y": this.color=Card.colors.YELLOW;
				break;
			case "B": this.color=Card.colors.BLUE;
				break;
			case "G": this.color=Card.colors.GREEN;
				break;
			case "S": this.color=Card.colors.BLACK;
		}
		switch(value){
			case "0": this.value=Card.values.ZERO;
				break;
			case "1": this.value=Card.values.ONE;
				break;
			case "2": this.value=Card.values.TWO;
				break;
			case "3": this.value=Card.values.THREE;
				break;
			case "4": this.value=Card.values.FOUR;
				break;
			case "5": this.value=Card.values.FIVE;
				break;
			case "6": this.value=Card.values.SIX;
				break;
			case "7": this.value=Card.values.SEVEN;
				break;
			case "8": this.value=Card.values.EIGHT;
				break;
			case "9": this.value=Card.values.NINE;
				break;
			case "S": this.value=Card.values.SKIP;
				break;
			case "X": this.value=Card.values.TAKE_TWO;
				break;
			case "R": this.value=Card.values.RETOUR;
				break;
			case "Y": this.value=Card.values.TAKE_FOUR;
				break;
			case "C": this.value=Card.values.CHOOSE_COLOR;
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




