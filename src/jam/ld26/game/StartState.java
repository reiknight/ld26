package jam.ld26.game;

import infinitedog.frisky.events.InputEvent;
import infinitedog.frisky.game.ManagedGameState;
import infinitedog.frisky.gui.Button;
import jam.ld26.entities.CrossHair;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.BlobbyTransition;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class StartState extends ManagedGameState {
    private Image background;
    private Button button_start, button_instructions, button_credits, button_editor;
    
    private boolean start_game = false;
    
    public StartState(int stateID) {
        super(stateID);
    }
    
    @Override
    public void init(GameContainer gc, StateBasedGame game) throws SlickException {
        em.setGameState(C.States.START_STATE.name);
        //Add events
        evm.addEvent(C.Events.CLICK_BUTTON.name, new InputEvent(InputEvent.MOUSE_CLICK, 
                Input.MOUSE_LEFT_BUTTON, (Integer) C.Logic.SELECT_OPTION_DELAY.data));
        //Crosshair movement
        evm.addEvent(C.Events.CROSSHAIR_MOVED.name, new InputEvent(InputEvent.MOUSE_MOVE, 
                new Rectangle(0, 0, C.SCREEN_WIDTH, C.SCREEN_HEIGHT)));
        //Load textures
        tm.addTexture(C.Textures.START_BACKGROUND.name, C.Textures.START_BACKGROUND.path);
        tm.addTexture(C.Textures.BUTTON_CREDITS.name, C.Textures.BUTTON_CREDITS.path);
        tm.addTexture(C.Textures.BUTTON_PLAY.name, C.Textures.BUTTON_PLAY.path);
        tm.addTexture(C.Textures.BUTTON_INSTRUCTIONS.name, C.Textures.BUTTON_INSTRUCTIONS.path);
        tm.addTexture(C.Textures.BUTTON_EDITOR.name, C.Textures.BUTTON_EDITOR.path);
        tm.addTexture(C.Textures.ENEMIES_TILE_SET.name, C.Textures.ENEMIES_TILE_SET.path);
        //Load entities
        button_start = new Button(C.Buttons.START_GAME.textureName,
                "button_start", C.Groups.BUTTONS.name,
                C.Buttons.START_GAME.label, C.Buttons.START_GAME.labelPosition);
        button_start.setPosition(C.Buttons.START_GAME.position);
        em.addEntity(button_start.getName(), button_start);
        
        button_instructions = new Button(C.Buttons.INSTRUCTIONS.textureName,
                "button_instructions", C.Groups.BUTTONS.name,
                C.Buttons.INSTRUCTIONS.label, C.Buttons.INSTRUCTIONS.labelPosition);
        button_instructions.setPosition(C.Buttons.INSTRUCTIONS.position);
        em.addEntity(button_instructions.getName(), button_instructions);
        
        button_credits = new Button(C.Buttons.CREDITS.textureName,
                "button_credits", C.Groups.BUTTONS.name,
                C.Buttons.INSTRUCTIONS.label, C.Buttons.CREDITS.labelPosition);
        button_credits.setPosition(C.Buttons.CREDITS.position);
        em.addEntity(button_credits.getName(), button_credits);
        
        button_editor = new Button(C.Buttons.EDITOR.textureName,
                "button_editor", C.Groups.BUTTONS.name,
                C.Buttons.EDITOR.label, C.Buttons.EDITOR.labelPosition);
        button_editor.setPosition(C.Buttons.EDITOR.position);
        em.addEntity(button_editor.getName(), button_editor);
        
        
        //Add Crosshair
        em.addEntity(C.Entities.CROSSHAIR.name, new CrossHair());
        //Add background
        background = tm.getTexture(C.Textures.START_BACKGROUND.name);
    }
    
    @Override
    public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
        em.setGameState(C.States.START_STATE.name);
        background.draw(0, 0);
        em.render(gc, g);
        CrossHair crosshair = (CrossHair) em.getEntity(C.Entities.CROSSHAIR.name);
        crosshair.render(gc, g);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
        evm.update(gc, delta);
        em.setGameState(C.States.START_STATE.name);
        em.update(gc, delta);
        
        if(evm.isHappening(C.Events.CLICK_BUTTON.name, gc)) {
            CrossHair crosshair = (CrossHair) em.getEntity(C.Entities.CROSSHAIR.name);
                                
            if(pm.testCollisionsEntity(crosshair, button_start)) {
                ((MainState)game.getState(C.States.MAIN_STATE.value)).restart();
                game.enterState(C.States.MAIN_STATE.value, new FadeOutTransition(), new FadeInTransition());
            }
            else if(pm.testCollisionsEntity(crosshair, button_instructions)) {
                game.enterState(C.States.INSTRUCTIONS_STATE.value, new FadeOutTransition(), new FadeInTransition());
            }
            else if(pm.testCollisionsEntity(crosshair, button_credits)) {
                game.enterState(C.States.CREDITS_STATE.value, new FadeOutTransition(), new FadeInTransition());
            }
            else if(pm.testCollisionsEntity(crosshair, button_editor)) {
                game.enterState(C.States.INSTRUCTIONS_LEVEL_EDITOR_STATE.value, new FadeOutTransition(), new FadeInTransition());
            }
        }
        if(evm.isHappening(C.Events.CLOSE_WINDOW.name, gc)) {
            gc.exit();
        }
    }   
}
