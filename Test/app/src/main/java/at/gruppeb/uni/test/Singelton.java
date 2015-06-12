package at.gruppeb.uni.test;

/**
 * Created by Luki on 08.06.2015.
 */
public class Singelton {
    static TextClass tc;

    public static TextClass getTc() {
        if(tc == null)
            tc = new TextClass("Name");

        return tc;

    }
}
