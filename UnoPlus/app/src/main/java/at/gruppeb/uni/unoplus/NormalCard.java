package classes;

import interfaces.ICard;

/**
 * Created by Luki on 18.03.2015.
 */
public class NormalCard extends Card implements ICard{
    private int _number;

    public NormalCard(String name, String color, int number) {
        super(name,color,false);
        this._number = number;
    }

    public String get_name() {
        return super.get_name();
    }

    public int get_wert() {
        return _number;
    }

    @Override
    public String toString() {
        return  super.get_name() + " " + super.get_color();
    }
}
