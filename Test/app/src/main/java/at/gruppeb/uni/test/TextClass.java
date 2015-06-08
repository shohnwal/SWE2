package at.gruppeb.uni.test;

/**
 * Created by Luki on 08.06.2015.
 */
public class TextClass {
    static int i;
    String name;
    public TextClass(String name){
        i++;
        this.name = name;
    }

    public static int getI() {
        return i;
    }

    @Override
    public String toString() {
        return "TextClass{" + "name='" + name + '\'' + '}' + i;
    }
}
