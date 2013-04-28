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
import jam.ld26.levels.Level;
import jam.ld26.tiles.TileSet;
import java.util.ArrayList;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Reik Val
 */
public class Player extends Entity {
    //Characteristics.
    private float velX = .15f;
    private float velY = 0;
    private int frame = 36;
    private float g = .0001f;
    
    private boolean jumping = false;
    private byte movimiento = 0;
    
    private Level lvl;
    
    private EntityManager em = EntityManager.getInstance();
    private PhysicsManager pm = PhysicsManager.getInstance();
    private EventManager evm = EventManager.getInstance();
    private SoundManager sm = SoundManager.getInstance();
    
    private TileSet tileSet = new TileSet(C.Textures.TILE_SET.name, 
            (Integer) C.Logic.TILE_SIZE.data);
    
    public Player() {
        name = C.Entities.PLAYER.name;
        group = C.Groups.PLAYER.name;
        setWidth((Integer)C.Logic.RECTANGLE_INITIAL_SIZE.data);
        setHeight((Integer)C.Logic.RECTANGLE_INITIAL_SIZE.data);
    }
    
    public Player(Level lvl) {
        this();
        this.lvl = lvl;
    }
     
    @Override
    public void render(GameContainer gc, Graphics g) {
        super.render(gc, g);
        tileSet.render(frame, getX(), getY());
    }
    
    @Override
    public void update(GameContainer gc, int delta) {
        super.update(gc, delta);
        float x = getX();
        float y = getY();
        float vx = 0;
        float vy = jumping?velY:0;
        
        //Comprobamos la lógica del salto.
        vy = jump(gc, delta, vy);
        // Player movement
        movement(gc, delta);
        
        //Comprobamos si esta cayendo
        int[] position = lvl.getTilePosition(new Vector2f(x+(getWidth()/2),y+getHeight()-5));
        try {
            if(lvl.getMap().get(position[1]).get(position[0]) != 0) {
                jumping = false;
            } else {
                if(!jumping) {
                    jumping = true;
                    velY = 0f;
                }
            }
        } catch(IndexOutOfBoundsException e) {
            x = 0;
            y = 0;
            velY = 0;
            jumping = false;
        }
        
        //Comprobamos si esta saltando
        position = lvl.getTilePosition(new Vector2f(x+(getWidth()/2),y));
        try {
            if(lvl.getMap().get(position[1]).get(position[0]) != 0) {
                vy = jumping?.08f:0;
                velY = jumping?.08f:0;
            }
        } catch(IndexOutOfBoundsException e) {
            vy = jumping?.08f:0;
            velY = jumping?.08f:0;
        }
        
        if(movimiento == 1) {
            position = lvl.getTilePosition(new Vector2f(getX()+getWidth()-10,getY()+getHeight()/2));
            try {
                if(lvl.getMap().get(position[1]).get(position[0]) != 0) {
                    movimiento = 0;
                }
            } catch(IndexOutOfBoundsException e) {
                movimiento = 0;
            }
        } else if(movimiento == -1) {
            position = lvl.getTilePosition(new Vector2f(getX()+10,getY()+getHeight()/2));
            try {
                if(lvl.getMap().get(position[1]).get(position[0]) != 0) {
                    movimiento = 0;
                }
            } catch(IndexOutOfBoundsException e) {
                movimiento = 0;
            }
        }
  
        // Check if any enemy see you
        ArrayList<Entity> enemies = em.getEntityGroup(C.Groups.ENEMIES.name);
        
        if(!C.GOD_MODE) {
            for(int i = 0; i < enemies.size(); i++) {
                Enemy e = (Enemy) enemies.get(i);
                if(e.hitPlayer(this)) {
                    x = 0;
                    y = 0;
                    movimiento = 0;
                    velY = 0;
                    jumping = false;
                }
            }
        }
        vx = movimiento*velX*delta; 
        
        //Actualizamos la posición
        x += vx;
        y += vy;
        this.setPosition(new Vector2f(x,y));
    }
    
    /**
     * Jump logic.
     * @param gc
     * @param delta
     * @param vy 
     */
    private float jump(GameContainer gc, int delta, float vy) {
        if(evm.isHappening(C.Events.ACTION.name, gc)) {
            if(!jumping) {
                jumping = true;
                velY = -.08f;
            }
        }
        if(jumping) {
            vy += velY*delta;
            velY += g*delta;
        }
        return vy;
    }
    
    /**
     * Movement logic.
     * @param gc
     * @param delta 
     */
    private void movement(GameContainer gc, int delta) {
        if(!jumping) {
            if(evm.isHappening(C.Events.MOVE_LEFT.name, gc)) {
                movimiento = -1;
            } else if(evm.isHappening(C.Events.MOVE_RIGHT.name, gc)) {
                movimiento = 1;
            } else {
                movimiento = 0;
            }
        }
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

    public boolean isJumping() {
        return jumping;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }

}
