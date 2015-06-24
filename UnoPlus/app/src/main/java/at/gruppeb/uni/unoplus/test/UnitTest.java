package at.gruppeb.uni.unoplus.test;

import android.test.InstrumentationTestCase;

/**
 * Created by Luki on 24.06.2015.
 */
public class UnitTest extends InstrumentationTestCase{
    public void test()throws Exception{
        final int e = 1;
        final int r = 2;
        assertEquals(e,r);
    }
}
