/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jam.ld26.entities;

import infinitedog.frisky.entities.Entity;
import infinitedog.frisky.entities.EntityManager;
import infinitedog.frisky.events.EventManager;
import infinitedog.frisky.physics.PhysicsManager;
import infinitedog.frisky.sounds.SoundManager;
import jam.ld26.game.C;
import jam.ld26.tiles.TileSet;
import java.awt.Frame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Reik Val
 */
public class Player extends Entity {
    //Characteristics.
    private float velX = .3f;
    private float velY = -.1f;
    private int frame = 36;
    
    private EntityManager em = EntityManager.getInstance();
    private PhysicsManager pm = PhysicsManager.getInstance();
    
    private TileSet tileSet = new TileSet(C.Textures.TILE_SET.name, 
            (Integer) C.Logic.TILE_SIZE.data);
    
    public Player() {
        name = C.Entities.PLAYER.name;
        group = C.Groups.PLAYER.name;
        setWidth((Integer)C.Logic.RECTANGLE_INITIAL_SIZE.data);
        setHeight((Integer)C.Logic.RECTANGLE_INITIAL_SIZE.data);
    }
     
    @Override
    public void render(GameContainer gc, Graphics g) {
        super.render(gc, g);
        tileSet.render(frame, getX(), getY());
    }
    
    @Override
    public void update(GameContainer gc, int delta) {
        super.update(gc, delta);
        
        EventManager evm = EventManager.getInstance();
        SoundManager sm = SoundManager.getInstance();
        float x = getX();
        float y = getY();
        float vx = 0;
        float vy = velY;
    
        // Player movement
        if(evm.isHappening(C.Events.MOVE_LEFT.name, gc)) {
            vx -= velX * delta;
        }else if(evm.isHappening(C.Events.MOVE_RIGHT.name, gc)) {
            vx += velX * delta;
        }
        
        vy += velY*delta;
        velY += .0001f;
        x += vx;
        y += vy;
        this.setPosition(new Vector2f(x,y));

        // Check if any enemy see you

        if(!C.GOD_MODE) {

        }

        // Player action
        

        // Next and previous zombie
            
        

    }

    public float getVelX() {
        return velX;
    }

    public void setVelX(float velX) {
        this.velX = velX;
    }

    public float getVelY() {
        return velY;
    }

    public void setVelY(float velY) {
        this.velY = velY;
    }

}
