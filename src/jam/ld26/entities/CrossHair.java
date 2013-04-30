package jam.ld26.entities;

import infinitedog.frisky.entities.Sprite;
import infinitedog.frisky.events.EventManager;
import infinitedog.frisky.events.InputEvent;
import jam.ld26.game.C;
import jam.ld26.tiles.TileSet;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

public class CrossHair extends Sprite {
    protected TileSet tileSet = new TileSet(C.Textures.ENEMIES_TILE_SET.name, 
          (Integer) C.Logic.TILE_SIZE.data);
     
    public CrossHair() {
        super();
        autoRender = false;
    }
    
    public void render(GameContainer gc, Graphics g) {
        super.render(gc, g);
        tileSet.render(7, getX(), getY());
    }
    
    @Override
    public void update(GameContainer gc, int delta) {
        EventManager em = EventManager.getInstance();
        if(em.isHappening(C.Events.CROSSHAIR_MOVED.name, gc)) {
            Input input = ((InputEvent)em.getEvent(C.Events.CROSSHAIR_MOVED.name)).getInput();
            setPosition(new Vector2f(input.getMouseX() - getWidth() / 2, input.getMouseY() - getHeight() / 2));
        }
    }
}
