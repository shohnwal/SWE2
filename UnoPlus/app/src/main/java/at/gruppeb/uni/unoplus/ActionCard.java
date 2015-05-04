package classes;

import interfaces.ICard;

/**
 * Created by Luki on 18.03.2015.
 */
public class ActionCard extends Card implements ICard {
    private String _action;

    public ActionCard(String name, String action, String color) {
        super(name,color,true);
        this._action = action;
    }

    @Override
    public String get_name() {
        return super.get_name();
    }

    public String get_action() {
        return _action;
    }

    @Override
    public String toString() {
        return  super.get_name() + " " + _action;
    }
}
