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
public class LazyTriangleEnemy extends Enemy {
    
    public LazyTriangleEnemy(float x, float y) {
        super(x,y);
    }
    
    public LazyTriangleEnemy(float x, float y, Level lvl) {
        super(x,y,lvl);
    }
    
    public LazyTriangleEnemy(Vector2f position, Level lvl) {
        super(position.x, position.y, lvl);
    }
     
    @Override
    public void render(GameContainer gc, Graphics g) {
        super.render(gc, g);
        tileSet.render(3, getX(), getY());
    }

    @Override
    public int getType() {
        return C.Enemies.LAZY_TRIANGLE_ENEMY.id;
    }
    
}
