package jam.ld26.game;
 
import infinitedog.frisky.events.InputEvent;
import infinitedog.frisky.game.ManagedGameState;
import jam.ld26.entities.Player;
import jam.ld26.levels.Level;
import jam.ld26.levels.LevelEditor;
import jam.ld26.levels.LevelManager;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;
import org.json.simple.parser.ParseException;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class MainState extends ManagedGameState {
    private boolean paused = false;
    private Level lvl;
    private LevelManager lvlManager = null;
    private boolean musicOn = true;


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
        evm.addEvent(C.Events.SOUND_OFF.name, new InputEvent(InputEvent.KEYBOARD, Input.KEY_M, 1000));
        evm.addEvent(C.Events.PAUSED.name, new InputEvent(InputEvent.KEYBOARD, Input.KEY_P, 500));
        //Load textures
        tm.addTexture(C.Textures.ENEMIES_TILE_SET.name, C.Textures.ENEMIES_TILE_SET.path);
        tm.addTexture(C.Textures.PORTAL.name, C.Textures.PORTAL.path);
        //Load sounds
        //Load sounds
        sm.addSound(C.Sounds.ENCENDIDO.name, C.Sounds.ENCENDIDO.path);
        sm.addSound(C.Sounds.JUMP.name, C.Sounds.JUMP.path);
        sm.addSound(C.Sounds.MUERTE.name, C.Sounds.MUERTE.path);
        sm.addSound(C.Sounds.AVISTADO.name, C.Sounds.AVISTADO.path);
        sm.addSound(C.Sounds.PORTAL.name, C.Sounds.PORTAL.path);
        sm.addMusic(C.Sounds.MUSIC.name, C.Sounds.MUSIC.path);
        
        evm.addEvent(C.Events.CLOSE_WINDOW.name, new InputEvent(InputEvent.KEYBOARD, Input.KEY_ESCAPE));
                
        try {
            lvlManager = new LevelManager("resources/levels/game");
        } catch (ParseException ex) {
            Logger.getLogger(LevelEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LevelEditor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainState.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        try {
            lvl = lvlManager.nextLevel();
        } catch (IOException ex) {
            Logger.getLogger(MainState.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        restart();
    }
    
    @Override
    public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
        if (lvl != null) {
            lvl.render(gc, g);
            em.render(gc, g);
        }
    }

    @Override
    public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
        em.setGameState(C.States.MAIN_STATE.name);
        evm.update(gc, delta);
        if(!paused) {
            em.update(gc, delta);
            lvl.update(gc, delta);
            if(evm.isHappening(C.Events.SOUND_OFF.name, gc)) {
                if(musicOn)
                    sm.getMusic(C.Sounds.MUSIC.name).stop();
                else
                    sm.playMusic(C.Sounds.MUSIC.name);
                musicOn = !musicOn;
            }
            if(!((Music)sm.getMusic(C.Sounds.MUSIC.name)).playing() && musicOn) {
                sm.playMusic(C.Sounds.MUSIC.name);
            }
            Player p = lvl.getPlayer();
            if(p.won()) {
                try {
                    lvl = lvlManager.nextLevelWithoutLoop();
                } catch (IOException ex) {
                    Logger.getLogger(MainState.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
                if (lvl == null) {
                    game.enterState(C.States.CREDITS_STATE.value, new FadeOutTransition(), new FadeInTransition());
                }
            }
        }
                
        if(evm.isHappening(C.Events.CLOSE_WINDOW.name, gc)) {
            gc.exit();
        }
        if(evm.isHappening(C.Events.PAUSED.name, gc)) {
            paused = !paused;
        }
    }

    void restart() {
        
    }
}
