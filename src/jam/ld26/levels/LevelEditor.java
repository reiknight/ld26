package jam.ld26.levels;

import jam.ld26.entities.CrossHair;
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

public class LevelEditor {
    private Level lvl;
    private boolean paused = false;
    private boolean showTileSetMenu = false;
    private boolean needNewLevelName = false;
    private int[] hoverTilePosition = {0,0};
    private int tileSetIdSelected = 0;
    private MessageManager msgManager = null;
    
    public LevelEditor() {
        msgManager = new MessageManager();
        newLevel();
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
        }
        
        drawGrid(gc, g);
        drawCursor(gc, g);
        msgManager.render(gc, g);
    }
    
    public void update(GameContainer gc, int delta, CrossHair crosshair) {
        lvl.update(gc, delta);    
        hoverTilePosition = lvl.getTilePosition(crosshair.getCenter());
        msgManager.update(gc, delta); 
        
        if (needNewLevelName && msgManager.isInputFinished()) {
            lvl.setName(msgManager.getUserInput());
            needNewLevelName = false;
        }
    }
    
    public void handleClick() {
        if(!this.needNewLevelName) {
            if(this.showTileSetMenu) {
                tileSetIdSelected = hoverTilePosition[0] + hoverTilePosition[1] * lvl.getTileSet().getCols();
                this.showTileSetMenu = false;
            } else {
                lvl.setTileIdAtPosition(hoverTilePosition, tileSetIdSelected);
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
        g.fillRect(hoverTilePosition[0] * lvl.getTileSize(), hoverTilePosition[1] * lvl.getTileSize(),
            lvl.getTileSize(), lvl.getTileSize());
    }
    
    public void toggleTileSetMenu() {
        if(!this.needNewLevelName) {
            this.showTileSetMenu = !this.showTileSetMenu;
        }
    }
    
    public void loadLevel() {
        try {
            lvl.load();
            msgManager.announce("Map '" + lvl.getName() + "' loaded.");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LevelEditorState.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(LevelEditorState.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
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
    }

    public void newLevel() {
        if(lvl != null) {
            saveLevel();
        }
        lvl = new Level();
        msgManager.input("Enter new level name:");
        needNewLevelName = true;
    }
}
