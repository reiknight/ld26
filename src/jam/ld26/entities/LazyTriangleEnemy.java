/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jam.ld26.entities;

import jam.ld26.levels.Level;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

/**
 *
 * @author Reik Val
 */
public class LazyTriangleEnemy extends Enemy {
    
    public LazyTriangleEnemy(float x, float y) {
        super(x,y);
    }
    
    public LazyTriangleEnemy(float x, float y, Level lvl) {
        super(x,y,lvl);
    }
     
    @Override
    public void render(GameContainer gc, Graphics g) {
        super.render(gc, g);
        tileSet.render(0, getX(), getY());
    }
    
}
