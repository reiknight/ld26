package jam.ld26.levels;

import infinitedog.frisky.textures.TextureManager;
import jam.ld26.levels.Level;
import jam.ld26.tiles.TileSet;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Logger;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import junit.framework.TestCase;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.ResourceLoader;
import org.newdawn.slick.util.ResourceLocation;

public class LevelTest extends TestCase {
    private static Level lvl;

    private String dummyLevelFile = "fixtures/levels/dummy.json";
       
    public LevelTest() {
    }

    @Before
    public void setUp() throws SlickException {
        try {
            lvl = new Level(dummyLevelFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LevelTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(LevelTest.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }
    
    @After
    public void tearDown() {
    }
    
    public void testGetTileSize() {
        assertEquals(32, lvl.getTileSize());
    }
    
    public void testGetTileSet() {
        //assertEquals(new TileSet("dummy_tileset", 32), lvl.getTileSet());
    }

    public void testGetRows() {
        assertEquals(3, lvl.getRows());
    }
    
    public void testGetCols() {
        assertEquals(10, lvl.getCols());
    }
    
    public void testGetMap() {
        ArrayList<ArrayList<Integer>> expectedMap = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < 3; i++) {
            expectedMap.add(new ArrayList<Integer>());
            for (int j = 0; j < 10; j++) {
                expectedMap.get(i).add(i);
            }
        }

        assertEquals(expectedMap, lvl.getMap());
    }
}
