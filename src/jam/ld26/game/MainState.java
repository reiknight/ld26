package jam.ld26.game;
 
import infinitedog.frisky.events.InputEvent;
import infinitedog.frisky.game.ManagedGameState;
import jam.ld26.entities.Enemy;
import jam.ld26.entities.Goal;
import jam.ld26.entities.LazyTriangleEnemy;
import jam.ld26.entities.Player;
import jam.ld26.entities.SquareEnemy;
import jam.ld26.levels.Level;
import jam.ld26.levels.LevelEditor;
import jam.ld26.levels.LevelManager;
import java.io.FileNotFoundException;
import java.util.logging.Logger;
import org.json.simple.parser.ParseException;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class MainState extends ManagedGameState {
    private boolean paused = false;
    private Level lvl;
    private LevelManager lvlManager = null;


    public MainState(int stateID)
    {
        super(stateID);
        em.setGameState(C.States.MAIN_STATE.name);
    }

    @Override
    public void init(GameContainer gc, StateBasedGame game) throws SlickException {
        em.setGameState(C.States.MAIN_STATE.name);
        //Player movement
        evm.addEvent(C.Events.MOVE_LEFT.name, new InputEvent(InputEvent.KEYBOARD, Input.KEY_LEFT));
        evm.addEvent(C.Events.MOVE_RIGHT.name, new InputEvent(InputEvent.KEYBOARD, Input.KEY_RIGHT));
        evm.addEvent(C.Events.ACTION.name, new InputEvent(InputEvent.KEYBOARD, Input.KEY_SPACE));
        //Load textures
        tm.addTexture(C.Textures.TILE_SET.name, C.Textures.TILE_SET.path);
        tm.addTexture(C.Textures.ENEMIES_TILE_SET.name, C.Textures.ENEMIES_TILE_SET.path);
        
        evm.addEvent(C.Events.CLOSE_WINDOW.name, new InputEvent(InputEvent.KEYBOARD, Input.KEY_ESCAPE));
                
        try {
            lvlManager = new LevelManager("resources/levels/game");
        } catch (ParseException ex) {
            Logger.getLogger(LevelEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LevelEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        lvl = lvlManager.nextLevel();
                
        Player player = new Player(lvl);
        player.setPosition(lvl.getPlayerPosition());
        em.addEntity(player.getName(), player);
        Enemy lt = new SquareEnemy(300, 256, lvl);
        Enemy lt2 = new LazyTriangleEnemy(300, 256, lvl);
        em.addEntity(lt.getName(), lt);
        em.addEntity(lt2.getName(), lt2);
        Goal goal = new Goal(300, 256);
        em.addEntity(goal.getName(), goal);
        
        restart();
    }
    
    @Override
    public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
        lvl.render(gc, g);
        em.render(gc, g);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
        em.setGameState(C.States.MAIN_STATE.name);
        evm.update(gc, delta);
        em.update(gc, delta);
        lvl.update(gc, delta);
        Player p = (Player) em.getEntity(C.Entities.PLAYER.name);
        if(p.won()) {
            em.removeEntityGroup(C.Groups.ENEMIES.name);
            em.removeEntityGroup(C.Groups.GOAL.name);
            lvl = lvlManager.nextLevel();
            p.reset(lvl);
            //TODO: Logica de añadido de enemigos.
        }
        
        if(evm.isHappening(C.Events.CLOSE_WINDOW.name, gc)) {
            gc.exit();
        }
    }

    void restart() {
        
    }
}
