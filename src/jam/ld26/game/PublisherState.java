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
public class PublisherState extends ManagedGameState {
    private int elapsedTime = 0;
    
    public PublisherState(int stateID) {
        super(stateID);
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        em.setGameState(C.States.PUBLISHER_STATE.name);
        evm.addEvent(C.Events.CLOSE_WINDOW.name, new InputEvent(InputEvent.KEYBOARD, Input.KEY_ESCAPE));
        tm.addTexture(C.Textures.LOGO.name, C.Textures.LOGO.path);
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        em.setGameState(C.States.PUBLISHER_STATE.name);
        tm.getTexture(C.Textures.LOGO.name).draw(200, 100);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        elapsedTime += delta;
        em.setGameState(C.States.PUBLISHER_STATE.name);
        em.update(container, delta);
        evm.update(container, delta);

        if(elapsedTime > (Integer) C.Logic.PUBLISHER_TIME.data || evm.isHappening(C.Events.CLOSE_WINDOW.name, container)) {
            game.enterState(C.States.START_STATE.value, new FadeOutTransition(), new FadeInTransition());
        }
    }
    
}
