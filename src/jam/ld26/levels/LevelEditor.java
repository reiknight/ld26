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
    private int[] hoverTilePosition = {0,0};
    private int tileSetIdSelected = 0;
    private boolean loadLevel = false;
    private boolean saveLevel = false;
    private float msgTimer = 0;
    
    public LevelEditor() {
        lvl = new Level("fixtures/levels/dummy.json");
        loadLevel();
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
        drawMsgs(gc, g);
    }
    
    public void update(GameContainer gc, int delta, CrossHair crosshair) {
        lvl.update(gc, delta);    
        hoverTilePosition = lvl.getTilePosition(crosshair.getCenter());
                
        if(loadLevel) {
            msgTimer += delta;
            if(msgTimer > 1000) {
                msgTimer = 0;
                loadLevel = false;
            }
        } else if(saveLevel) {
            msgTimer += delta;
            if(msgTimer > 1000) {
                msgTimer = 0;
                saveLevel = false;
            }
        }
    }
    
    public void handleClick() {
        if(this.showTileSetMenu) {
            tileSetIdSelected = hoverTilePosition[0] + hoverTilePosition[1] * lvl.getTileSet().getCols();
            this.showTileSetMenu = false;
        } else {
            lvl.setTileIdAtPosition(hoverTilePosition, tileSetIdSelected);
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

    public void drawMsgs(GameContainer gc, Graphics g) throws SlickException { 
        g.setColor(Color.white);
        if(loadLevel) {
           g.drawString("Game loaded.", 10, 10);
        } else if(saveLevel) {
           g.drawString("Game saved.", 10, 10);
        }
    }
    
    public void toggleTileSetMenu() {
        this.showTileSetMenu = !this.showTileSetMenu;
    }
    
    public void loadLevel() {
        try {
            lvl.load();
            loadLevel = true;
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
        saveLevel = true;
    }
    
    public void eraseLevel() {
        lvl.eraseLevel();
    }
}
