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
        
        g.drawString("You are a Square!", 100, 100);
        g.drawString("You need to go to the portal to save yourself.", 100, 115);
        
        g.drawString("Whatch out! Circles, Triangles and Diamonds want to kill you!", 100, 169);
        g.drawString("Don't let them touch you and escape through the portal!", 100, 184);
        
        
        g.drawString("Arrow Keys: Movement", 100, 215);
        g.drawString("Space: Jump", 100, 230);
        g.drawString("P: Pause", 100, 245);
        g.drawString("M: Music Off (Not Sound Effects)", 100, 260);
        
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
