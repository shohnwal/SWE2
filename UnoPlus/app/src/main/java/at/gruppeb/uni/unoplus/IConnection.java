package at.gruppeb.uni.unoplus;

/**
 * Created by Bernhard on 28.04.2015.
 */
public interface IConnection {

    public Object[] getHandCards(int anzahl, int id);
    //return: Karten Objekt Array
    //param: int anzahl- wieviele Karte gezogen werden sollen
    //param: int id- Spieler ID
    public boolean discardHandCard(Object card, int id);
    //return: boolean- ob Abzulegende Karte gültig ist
    //param: Object card- Welche Karte der Spieler ablegen möchte
    //param: int id- Spieler ID
    public Object currentOpenCard();
    //return: Karten Objekt- Welche Karte aktuell offen liegt
    public boolean sayUNO(int id);
    //return: boolean, ob der Spieler UNO sagen darf
    //param: int id- Spieler ID
    public int currentPlayer();
    //return: int- Spieler ID
    //Welcher Spieler aktuell dran ist

}
