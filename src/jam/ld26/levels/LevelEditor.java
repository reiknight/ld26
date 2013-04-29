package jam.ld26.levels;

import jam.ld26.entities.CrossHair;
import jam.ld26.entities.Enemy;
import jam.ld26.entities.EnemyFactory;
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
    private Enemy dummyEnemy = null;
    private State state;
    private int[] hoverTilePosition = {0,0};
    private int tileSetIdSelected = 0;
    private int enemyIdSelected = 0;
    private LevelManager lvlManager = null;
    private MessageManager msgManager = null;
    
    static enum State { 
        DRAWING, CHOOSING_TILE, CREATING_NEW_LEVEL, PLACING_PLAYER, PLACING_ENEMY, PLACING_GOAL
    };
    
    public LevelEditor() {
        msgManager = new MessageManager();
        state = State.DRAWING;
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
    }
    
    public void render(GameContainer gc, Graphics g) throws SlickException {
        if (state == State.CHOOSING_TILE) {
            TileSet tileSet = lvl.getTileSet();
            for(int i = 0; i < tileSet.getRows(); i += 1) {
                for(int j = 0; j < tileSet.getCols(); j += 1) {
                    tileSet.render(i * tileSet.getCols() + j, j, i);       
                }
            }
        } else if (state != State.CREATING_NEW_LEVEL) {
            lvl.render(gc, g);      
            drawCursor(gc, g);
            drawGrid(gc, g);
        }
        msgManager.render(gc, g);
    }
    
    public void update(GameContainer gc, int delta, CrossHair crosshair) {
        //lvl.update(gc, delta);    
        hoverTilePosition = lvl.getTilePosition(crosshair.getCenter());
        msgManager.update(gc, delta); 
        
        if (state == State.CREATING_NEW_LEVEL && msgManager.isInputFinished()) {
            lvl.setFilePath("resources/levels/editor");
            lvl.setName(msgManager.getUserInput());
            lvlManager.addLevel(lvl.getName());
            saveLevel();
            state = State.DRAWING;
        }
              
        if(state == State.PLACING_PLAYER) {
            lvl.setPlayerPosition(new Vector2f(hoverTilePosition[0] * lvl.getTileSize(), 
                    hoverTilePosition[1] * lvl.getTileSize()));
        } else if (state == State.PLACING_GOAL) {
            lvl.setGoalPosition(new Vector2f(hoverTilePosition[0] * lvl.getTileSize(), 
                    hoverTilePosition[1] * lvl.getTileSize()));
        } else if (state == State.PLACING_ENEMY) {
            dummyEnemy.setPosition(new Vector2f(hoverTilePosition[0] * lvl.getTileSize(), 
                    hoverTilePosition[1] * lvl.getTileSize()));
        }
    }
    
    public void handleClick() {
        if(state != State.CREATING_NEW_LEVEL) {
            if(state == State.PLACING_PLAYER) {
                msgManager.clearMsg();
            } else if(state == State.PLACING_ENEMY) {
                lvl.addEnemy(dummyEnemy);
                msgManager.clearMsg();
            } else if(state == State.PLACING_GOAL) {
               msgManager.clearMsg();
            } else if(state == State.CHOOSING_TILE) {
                tileSetIdSelected = hoverTilePosition[0] + hoverTilePosition[1] * lvl.getTileSet().getCols();
            } else { // Drawing
                lvl.setTileIdAtPosition(hoverTilePosition, tileSetIdSelected);
            }
            state = State.DRAWING;
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
        if (state == State.PLACING_ENEMY) {
            dummyEnemy.render(gc, g);
        } else {
            if (state != State.PLACING_PLAYER && state != State.PLACING_GOAL) {
                lvl.getTileSet().render(tileSetIdSelected, hoverTilePosition[0], hoverTilePosition[1]);
            }
        }
    }
    
    public void loadLevel() {
        lvl = lvlManager.loadLevel();
        msgManager.announce("Map '" + lvl.getName() + "' loaded.");
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
        if(state != State.CREATING_NEW_LEVEL) {
            lvl.eraseLevel();
            msgManager.announce("Erased Map.");
        }
    }
    
    public void newLevel() {
        if(lvl != null) {
            saveLevel();
        }
        lvl = new Level();
        msgManager.input("Enter new level name:");
        state = State.CREATING_NEW_LEVEL;
    }
    
    public void nextLevel() {
        if(state != State.CREATING_NEW_LEVEL) {
            lvl = lvlManager.nextLevel();
            msgManager.announce("Map '" + lvl.getName() + "' loaded.");
        }
    }
        
    public void prevLevel() {
        if(state != State.CREATING_NEW_LEVEL) {
            lvl = lvlManager.prevLevel();
            msgManager.announce("Map '" + lvl.getName() + "' loaded."); 
        }
    }
    
    public void choseTile() {
        if(state != State.CREATING_NEW_LEVEL) {
            state = State.CHOOSING_TILE;
        }
    }
    
    public void placePlayer() {
        if(state != State.CREATING_NEW_LEVEL) {
            state = State.PLACING_PLAYER;
            msgManager.fix("Move player and click to set his position.");
        }
    }
    
    public void placeEnemy() {
        if(state == State.DRAWING) {
            enemyIdSelected = 0;
        }
        
        if(state != State.CREATING_NEW_LEVEL) {
            state = State.PLACING_ENEMY;
            dummyEnemy = EnemyFactory.createEnemy(enemyIdSelected, new Vector2f(0,0), lvl);
            enemyIdSelected = (enemyIdSelected + 1) % 2;
            msgManager.fix("Move enemy and click to set his position. Press E again to change enemy type.");
        }
    }
    
    public void placeGoal() {
        if(state != State.CREATING_NEW_LEVEL) {
            state = State.PLACING_GOAL;
            msgManager.fix("Move goal and click to set its position.");
        }
    }
}