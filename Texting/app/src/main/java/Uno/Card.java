package Uno;

import IUno.ICard;

/**
 * Created by Luki on 18.03.2015.
 */
public class Card implements ICard {
    private String _name;
    private String _color;
    private boolean is_action_card;

    public Card(String _name,String color,boolean is_action_card) {
        this._name = _name;
        this._color = color;
        this.is_action_card = is_action_card;
    }

    @Override
    public String get_name() {
        return this._name;
    }

    public String get_color() { return _color; }

    public boolean isActionCard(){
        return is_action_card;
    }

    @Override
    public String toString(){
        return get_name()+","+get_color()+","+isActionCard();
    }
}
