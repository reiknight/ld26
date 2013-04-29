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
import java.util.Random;
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
    private int frame = 4;
    private float g = .0001f;
    
    private boolean jumping = false;
    private byte movimiento = 0;
    
    private Level lvl;
    
    private Random rand = new Random();
    
    private PhysicsManager pm = PhysicsManager.getInstance();
    private EventManager evm = EventManager.getInstance();
    private SoundManager sm = SoundManager.getInstance();
    
    private TileSet tileSet = new TileSet(C.Textures.ENEMIES_TILE_SET.name, 
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
        switch(movimiento) {
            case 0:
                frame = 4;
                break;
            case 1:
                frame = 5;
                break;
            case -1:
                frame = 6;
                break;
        }
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
            int f = lvl.getMap().get(position[1]).get(position[0]);
            if(f != 0) {
                if(jumping && velY > 0 && f < 21) {    
                    int color = rand.nextInt(3)+1;
                    lightFrom(position[1],position[0], color);
                    lvl.getMap().get(position[1]).remove(position[0]);
                    lvl.getMap().get(position[1]).add(position[0],f+21*color);
                } else if(jumping && velY <= 0) {
                    lightOff(position[1], position[0]);
                    lvl.getMap().get(position[1]).remove(position[0]);
                    lvl.getMap().get(position[1]).add(position[0],f%21);
                }
                jumping = false;
            } else {
                if(!jumping) {
                    jumping = true;
                    velY = 0f;
                    try {
                        f = lvl.getMap().get(position[1]).get(position[0]-movimiento);
                        lightOff(position[1],position[0]-movimiento);
                        lvl.getMap().get(position[1]).remove(position[0]-movimiento);
                        lvl.getMap().get(position[1]).add(position[0]-movimiento,f%21);
                    } catch(IndexOutOfBoundsException e) { }
                }
            }
        } catch(IndexOutOfBoundsException e) {  
            x = lvl.getPlayerPosition().x;
            y = lvl.getPlayerPosition().y;
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
        ArrayList<Enemy> enemies = lvl.getEnemies();
        
        if(!C.GOD_MODE) {
            for(int i = 0; i < enemies.size(); i++) {
                Enemy e = (Enemy) enemies.get(i);
                if(e.hitPlayer(this)) {
                    
                    try {
                        int[] position2 = lvl.getTilePosition(new Vector2f(x+(getWidth()/2),y+getHeight()-5));
                        int f = lvl.getMap().get(position2[1]).get(position2[0]);
                        lightOff(position2[1],position2[0]);
                        lvl.getMap().get(position2[1]).remove(position2[0]);
                        lvl.getMap().get(position2[1]).add(position2[0],f%21);
                    } catch(IndexOutOfBoundsException ioobe) { }
                    x = lvl.getPlayerPosition().x;
                    y = lvl.getPlayerPosition().y;
                    lvl.reset();
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
    
    public void lightFrom(int x, int y, int color) {
        int posAnterior = lvl.getMap().get(x).get(y);
        for(int i = y+1; i < lvl.getMap().get(x).size(); i++) {
            int pos = lvl.getMap().get(x).get(i);
            if(pos != 0) {
                if((posAnterior == 1 || posAnterior == 2 || posAnterior == 4 || posAnterior == 5 || posAnterior == 12 || posAnterior == 13) && (pos == posAnterior+1 || posAnterior == pos)) { 
                    int f = lvl.getMap().get(x).get(i);
                    lvl.getMap().get(x).remove(i);
                    lvl.getMap().get(x).add(i,f+21*color);
                    posAnterior = pos;
                }
            } else {
                break;
            }
        }
        posAnterior = lvl.getMap().get(x).get(y);
        for(int i = y-1; i >0; i--) {
            int pos = lvl.getMap().get(x).get(i);
            if(lvl.getMap().get(x).get(i) != 0) {
                if((posAnterior == 2 || posAnterior == 3 || posAnterior == 5 || posAnterior == 6 || posAnterior == 13 || posAnterior == 14) && (pos == posAnterior-1 || posAnterior == pos)) { 
                    int f = lvl.getMap().get(x).get(i);
                    lvl.getMap().get(x).remove(i);
                    lvl.getMap().get(x).add(i,f+21*color);
                    posAnterior = pos;
                }
            } else {
                break;
            }
        }
    }
    
    public void lightOff(int x, int y) {
        int posAnterior = lvl.getMap().get(x).get(y)%21;
        for(int i = y+1; i < lvl.getMap().get(x).size(); i++) {
            int pos = lvl.getMap().get(x).get(i)%21;
            if(pos != 0) {
                if((posAnterior == 1 || posAnterior == 2 || posAnterior == 4 || posAnterior == 5 || posAnterior == 12 || posAnterior == 13) && (pos == posAnterior+1 || posAnterior == pos)) { 
                    int f = lvl.getMap().get(x).get(i);
                    lvl.getMap().get(x).remove(i);
                    lvl.getMap().get(x).add(i,f%21);
                    posAnterior = pos;
                }
            } else {
                break;
            }
        }
        posAnterior = lvl.getMap().get(x).get(y)%21;
        for(int i = y-1; i >0; i--) {
            int pos = lvl.getMap().get(x).get(i)%21;
            if(lvl.getMap().get(x).get(i) != 0) {
                if((posAnterior == 2 || posAnterior == 3 || posAnterior == 5 || posAnterior == 6 || posAnterior == 13 || posAnterior == 14) && (pos == posAnterior-1 || posAnterior == pos)) { 
                    int f = lvl.getMap().get(x).get(i);
                    lvl.getMap().get(x).remove(i);
                    lvl.getMap().get(x).add(i,f%21);
                    posAnterior = pos;
                }
            } else {
                break;
            }
        }
    }
       
    public boolean won() {
        Goal goal = lvl.getGoal();
        if(goal != null && goal.hitPlayer(this)) {
            return true;
        }
        return false;
    }
    
    public void reset(Level lvl) {
        this.lvl = lvl;
        this.setPosition(lvl.getPlayerPosition());
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
