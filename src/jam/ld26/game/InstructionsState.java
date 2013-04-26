package jam.ld26.game;

import infinitedog.frisky.events.InputEvent;
import infinitedog.frisky.game.ManagedGameState;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

/**
 *
 * @author InfiniteDog
 */
public class InstructionsState extends ManagedGameState {
    
    public InstructionsState(int stateID) {
        super(stateID);
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        em.setGameState(C.States.INSTRUCTIONS_STATE.name);
        evm.addEvent(C.Events.BACK.name, new InputEvent(InputEvent.KEYBOARD, Input.KEY_SPACE, (Integer) C.Logic.SELECT_OPTION_DELAY.data));
        evm.addEvent(C.Events.CLOSE_WINDOW.name, new InputEvent(InputEvent.KEYBOARD, Input.KEY_ESCAPE));
        
        //TODO
        tm.addTexture(C.Textures.START_BACKGROUND.name, C.Textures.START_BACKGROUND.path);
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        em.setGameState(C.States.INSTRUCTIONS_STATE.name);
        g.setColor(Color.white);
        g.drawString("press <SPACE> to back", 530, 50);
        g.drawString("Instructions", 100, 50);
        
        g.drawString("You are the Zombie 0!", 100, 100);
        g.drawString("Bite enemies and turn them to zombies.", 100, 115);
        g.drawString("Bring humanity to the next evolution!", 100, 130);
        
        g.drawString("Whatch out! You have 60 seconds before they escape!", 100, 169);
        
        
        g.drawString("WASD: Movement", 100, 200);
        g.drawString("Space: Bite enemy", 100, 215);
        
        em.render(container, g);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        em.setGameState(C.States.INSTRUCTIONS_STATE.name);
        
        em.update(container, delta);
        evm.update(container, delta);
        
        if(evm.isHappening(C.Events.BACK.name, container)) {
            game.enterState(C.States.START_STATE.value, new FadeOutTransition(), new FadeInTransition());
        }
        if(evm.isHappening(C.Events.CLOSE_WINDOW.name, container)) {
            container.exit();
        }
    }
    
}
