/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jam.ld26.entities;

import jam.ld26.game.C;
import jam.ld26.levels.Level;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;


/**
 *
 * @author Reik Val
 */
public class SquareEnemy extends Enemy {

    private int movimiento = 1;
    private float velX = 0.07f;
    
    public SquareEnemy(float x, float y, Level lvl) {
        super(x, y, lvl);
    }
    
    public SquareEnemy(Vector2f position, Level lvl) {
        super(position.x, position.y, lvl);
    }
    
    @Override
    public void render(GameContainer gc, Graphics g) {
        super.render(gc, g);
        tileSet.render(2, getX(), getY());
    }

    @Override
    public void update(GameContainer gc, int delta) {
        super.update(gc, delta);
        float x = getX();
        float y = getY();
        if(movimiento == 1) {
            int[] position = lvl.getTilePosition(new Vector2f(getX()+getWidth()+12,getY()+getHeight()/2));
            try {
                if(lvl.getMap().get(position[1]).get(position[0]) != 0) {
                    movimiento = -1;
                } else if(lvl.getMap().get(position[1]+1).get(position[0]) == 0) {
                    movimiento = -1;
                }
            } catch(IndexOutOfBoundsException e) {
                movimiento = -1;
            }
        } else if(movimiento == -1) {
            int[] position = lvl.getTilePosition(new Vector2f(getX()-12,getY()+getHeight()/2));
            try {
                if(lvl.getMap().get(position[1]).get(position[0]) != 0) {
                    movimiento = 1;
                } else if(lvl.getMap().get(position[1]+1).get(position[0]) == 0) {
                    movimiento = 1;
                }
            } catch(IndexOutOfBoundsException e) {
                movimiento = 1;
            }
        }
        //Actualizamos la posición
        x += movimiento*velX*delta;
        this.setPosition(new Vector2f(x,y));
    }
    
    @Override
    public int getType() {
        return C.Enemies.SQUARE_ENEMY.id;
    }

}
