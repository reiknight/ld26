package jam.ld26.levels;

import junit.framework.TestSuite;

public class LevelTestSuite {

    public static TestSuite suite() {
      TestSuite suite = new TestSuite("Test for jam.ld26.level");
      suite.addTestSuite(LevelTest.class);
      return suite;
    }
}