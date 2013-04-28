package jam.ld26.levels;

import jam.ld26.entities.CrossHair;
import jam.ld26.entities.Player;
import jam.ld26.game.C;
import jam.ld26.game.LevelEditorState;
import jam.ld26.tiles.TileSet;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;
import org.json.simple.parser.ParseException;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class LevelEditor {
    private Level lvl;
    private Player player;
    private boolean paused = false;
    private boolean showTileSetMenu = false;
    private boolean needNewLevelName = false;
    private boolean placingPlayer = false;
    private int[] hoverTilePosition = {0,0};
    private int tileSetIdSelected = 0;
    private LevelManager lvlManager = null;
    private MessageManager msgManager = null;
    
    public LevelEditor() {
        msgManager = new MessageManager();
        try {
            lvlManager = new LevelManager("resources/levels/editor");
        } catch (ParseException ex) {
            Logger.getLogger(LevelEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LevelEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        lvl = lvlManager.nextLevel();
        if (lvl == null) {
            newLevel();
        }
        player = new Player(lvl);
        player.setPosition(lvl.getPlayerPosition());
    }
    
    public void render(GameContainer gc, Graphics g) throws SlickException {
        if (this.showTileSetMenu) {
            TileSet tileSet = lvl.getTileSet();
            for(int i = 0; i < tileSet.getRows(); i += 1) {
                for(int j = 0; j < tileSet.getCols(); j += 1) {
                    tileSet.render(i * tileSet.getCols() + j, j, i);       
                }
            }
        } else {
            lvl.render(gc, g);
            player.render(gc, g);        
            drawCursor(gc, g);
            msgManager.render(gc, g);
        }
        drawGrid(gc, g);
    }
    
    public void update(GameContainer gc, int delta, CrossHair crosshair) {
        lvl.update(gc, delta);    
        hoverTilePosition = lvl.getTilePosition(crosshair.getCenter());
        msgManager.update(gc, delta); 
        
        if (needNewLevelName && msgManager.isInputFinished()) {
            lvl.setFilePath("resources/levels/editor");
            lvl.setName(msgManager.getUserInput());
            lvlManager.addLevel(lvl.getName());
            saveLevel();
            needNewLevelName = false;
        }
              
        if(placingPlayer) {
            player.setPosition(new Vector2f(hoverTilePosition[0] * lvl.getTileSize(), 
                    hoverTilePosition[1] * lvl.getTileSize()));
        }
    }
    
    public void handleClick() {
        if(placingPlayer) {
            placingPlayer = false;
            lvl.setPlayerPosition(new Vector2f(player.getX(), player.getY()));
            msgManager.clearMsg();
        } else {
            if(!needNewLevelName) {
                if(showTileSetMenu) {
                    tileSetIdSelected = hoverTilePosition[0] + hoverTilePosition[1] * lvl.getTileSet().getCols();
                    showTileSetMenu = false;
                } else {
                    lvl.setTileIdAtPosition(hoverTilePosition, tileSetIdSelected);
                }
            }
        }
    }
    
    public void drawGrid(GameContainer gc, Graphics g) throws SlickException {        
        g.setColor(Color.gray);
        for(int i = 0; i < lvl.getRows(); i += 1) {            
            g.drawLine(0, i * lvl.getTileSize(), lvl.getCols() * lvl.getTileSize(), i * lvl.getTileSize());
        }
        for(int j = 0; j < lvl.getCols(); j += 1) {
            g.drawLine(j * lvl.getTileSize(), 0, j * lvl.getTileSize(), lvl.getRows() * lvl.getTileSize());    
        }         
    }
    
    public void drawCursor(GameContainer gc, Graphics g) throws SlickException {
        lvl.getTileSet().render(tileSetIdSelected, hoverTilePosition[0], hoverTilePosition[1]);
        g.setColor(new Color(255, 255, 0, 150));
        if(!placingPlayer) {
            g.fillRect(hoverTilePosition[0] * lvl.getTileSize(), hoverTilePosition[1] * lvl.getTileSize(),
                lvl.getTileSize(), lvl.getTileSize());
        }
    }
    
    public void toggleTileSetMenu() {
        if(!this.needNewLevelName) {
            this.showTileSetMenu = !this.showTileSetMenu;
        }
    }
    
    public void loadLevel() {
        lvl = lvlManager.loadLevel();
        msgManager.announce("Map '" + lvl.getName() + "' loaded.");
        player.setPosition(lvl.getPlayerPosition());
    }
    
    public void saveLevel() {
        try {
            lvl.save();
        } catch (IOException ex) {
            Logger.getLogger(LevelEditorState.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        msgManager.announce("Map '" + lvl.getName() + "' saved.");
    }
    
    public void eraseLevel() {
        lvl.eraseLevel();
        msgManager.announce("Erased Map.");
    }
    
    public void newLevel() {
        if(lvl != null) {
            saveLevel();
        }
        lvl = new Level();
        msgManager.input("Enter new level name:");
        needNewLevelName = true;
    }
    
    public void nextLevel() {
        lvl = lvlManager.nextLevel();
        msgManager.announce("Map '" + lvl.getName() + "' loaded.");
        player.setPosition(lvl.getPlayerPosition());
    }
        
    public void prevLevel() {
        lvl = lvlManager.prevLevel();
        msgManager.announce("Map '" + lvl.getName() + "' loaded.");
        player.setPosition(lvl.getPlayerPosition());
    }
    
    public void placePlayer() {
        placingPlayer = true;
        msgManager.fix("Move player and click to set his position.");
    }
}