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
public class CreditsState extends ManagedGameState {
    
    public CreditsState(int stateID) {
        super(stateID);
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        em.setGameState(C.States.CREDITS_STATE.name);
        evm.addEvent(C.Events.BACK.name, new InputEvent(InputEvent.KEYBOARD, Input.KEY_SPACE, (Integer) C.Logic.SELECT_OPTION_DELAY.data));
        tm.addTexture(C.Textures.LOGO.name, C.Textures.LOGO.path);
        evm.addEvent(C.Events.CLOSE_WINDOW.name, new InputEvent(InputEvent.KEYBOARD, Input.KEY_ESCAPE));
        
        //TODO
        tm.addTexture(C.Textures.START_BACKGROUND.name, C.Textures.START_BACKGROUND.path);
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        em.setGameState(C.States.CREDITS_STATE.name);
        g.setColor(Color.white);
        tm.getTexture(C.Textures.LOGO.name).draw(200, 0);
        g.drawString("press <SPACE> to back", 530, 50);
        g.drawString("programmed by", 350, 400);
        g.drawString("@ReikVal", 350, 420);
        g.drawString("@ultrayoshi", 350, 440);
        g.drawString("art by", 350, 470);
        g.drawString("@Soraya_Alb", 350, 490);
        em.render(container, g);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        em.setGameState(C.States.CREDITS_STATE.name);
        
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
