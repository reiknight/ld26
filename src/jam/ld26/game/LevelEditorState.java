package jam.ld26.game;

import infinitedog.frisky.events.InputEvent;
import infinitedog.frisky.game.ManagedGameState;
import jam.ld26.entities.CrossHair;
import jam.ld26.levels.Level;
import jam.ld26.tiles.TileSet;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;
import org.json.simple.parser.ParseException;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

public class LevelEditorState extends ManagedGameState {
    private boolean paused = false;
    private boolean showTileSetMenu = false;
    private Level lvl;
    private int[] hoverTilePosition = {0,0};
    private int tileSetIdSelected = 0;
    private boolean loadLevel = false;
    private boolean saveLevel = false;
    private float msgTimer = 0;
    
    public LevelEditorState(int stateID)
    {
        super(stateID);
        em.setGameState(C.States.LEVEL_EDITOR_STATE.name);
    }
    
    @Override
    public void init(GameContainer gc, StateBasedGame game) throws SlickException {
        em.setGameState(C.States.LEVEL_EDITOR_STATE.name);
        evm.addEvent(C.Events.CLOSE_WINDOW.name, new InputEvent(InputEvent.KEYBOARD, Input.KEY_ESCAPE));
        evm.addEvent(C.Events.CLICK_LEFT_EDITOR.name, new InputEvent(InputEvent.MOUSE_CLICK, 
                Input.MOUSE_LEFT_BUTTON, (Integer) C.Logic.CLICK_EDITOR_DELAY.data));
        evm.addEvent(C.Events.LOAD_LEVEL.name, new InputEvent(InputEvent.KEYBOARD, Input.KEY_F2));
        evm.addEvent(C.Events.SAVE_LEVEL.name, new InputEvent(InputEvent.KEYBOARD, Input.KEY_F3));
        evm.addEvent(C.Events.EDITOR_TILE_SET_MENU.name, new InputEvent(InputEvent.KEYBOARD, Input.KEY_T, 
                (Integer) C.Logic.SELECT_OPTION_DELAY.data));
        evm.addEvent(C.Events.EDITOR_ERASE_LEVEL.name, new InputEvent(InputEvent.KEYBOARD, Input.KEY_C, 
                (Integer) C.Logic.SELECT_OPTION_DELAY.data));
        
        //Load textures
        tm.addTexture(C.Textures.DEFAULT_TILE_SET.name, C.Textures.DEFAULT_TILE_SET.path);
        
        //Crosshair movement
        evm.addEvent(C.Events.CROSSHAIR_MOVED.name, new InputEvent(InputEvent.MOUSE_MOVE, 
                new Rectangle(0, 0, C.SCREEN_WIDTH, C.SCREEN_HEIGHT)));
        //Add Crosshair
        em.addEntity(C.Entities.CROSSHAIR.name, new CrossHair());
        
        lvl = new Level("fixtures/levels/dummy.json");
        loadLevel();
    }
    
    @Override
    public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
        em.setGameState(C.States.LEVEL_EDITOR_STATE.name);

        em.render(gc, g);

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
        
        CrossHair crosshair = (CrossHair) em.getEntity(C.Entities.CROSSHAIR.name);
        crosshair.render(gc, g);
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
    
    @Override
    public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
        em.setGameState(C.States.LEVEL_EDITOR_STATE.name);
        
        evm.update(gc, delta);
        em.update(gc, delta);
        lvl.update(gc, delta);
        
        CrossHair crosshair = (CrossHair) em.getEntity(C.Entities.CROSSHAIR.name);
        hoverTilePosition = lvl.getTilePosition(crosshair.getCenter());
        
        if(evm.isHappening(C.Events.CLICK_LEFT_EDITOR.name, gc)) {
            if(this.showTileSetMenu) {
                tileSetIdSelected = hoverTilePosition[0] + hoverTilePosition[1] * lvl.getTileSet().getCols();
                this.showTileSetMenu = false;
            } else {
                lvl.setTileIdAtPosition(hoverTilePosition, tileSetIdSelected);
            }
        }
        
        if(evm.isHappening(C.Events.CLOSE_WINDOW.name, gc)) {
            gc.exit();
        } else if(evm.isHappening(C.Events.LOAD_LEVEL.name, gc)) {
            loadLevel();
        } else if(evm.isHappening(C.Events.SAVE_LEVEL.name, gc)) {
            saveLevel();
        } else if(evm.isHappening(C.Events.EDITOR_ERASE_LEVEL.name, gc)) {
            eraseLevel();
        }
        
        if(evm.isHappening(C.Events.EDITOR_TILE_SET_MENU.name, gc)) {
            this.showTileSetMenu = !this.showTileSetMenu;
        }
        
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