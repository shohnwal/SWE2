package Connect;

import java.lang.Object; /**
 * Created by Bernhard on 28.04.2015.
 */
public interface IConnection {

    //return: Karten Objekt Array
    //param: int anzahl- wieviele Karte gezogen werden sollen
    //param: int id- Spieler ID
    public Object[] getHandCards(int anzahl, int id);

    //return: boolean- ob Abzulegende Karte gültig ist
    //param: Object card- Welche Karte der Spieler ablegen möchte
    //param: int id- Spieler ID
    public boolean discardHandCard(Object card, int id);

    //return: Karten Objekt- Welche Karte aktuell offen liegt
    public Object getCurrentOpenCard();

    public void setCurrentOpenCard();

    //return: boolean, ob der Spieler UNO sagen darf
    //param: int id- Spieler ID
    public boolean sayUNO(int id);

    //return: int- Spieler ID
    //Welcher Spieler aktuell dran ist
    public int currentPlayer();

    //Setzt den nächsten Player
    public void setNextPlayer();

    //return next Player
    //Wechselt zum nächsten Player
    public int getNextPlayer();

    //return next Player
    //Ändert die Spielrichtung
    public int changePlayDirection();

    //return next Player
    //Überspringt einen Player
    public int stepOverNextPlayer();



}
