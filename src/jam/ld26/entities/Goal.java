/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jam.ld26.entities;

import infinitedog.frisky.entities.Entity;
import jam.ld26.game.C;
import jam.ld26.levels.Level;
import jam.ld26.tiles.TileSet;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Reik Val
 */
public class Goal extends Entity {
        
    private static int number = 0;
    protected Level lvl;
    
    protected TileSet tileSet = new TileSet(C.Textures.TILE_SET.name, 
            (Integer) C.Logic.TILE_SIZE.data);
    
    public Goal(float x, float y) {
        name = C.Entities.GOAL.name;
        group = C.Groups.GOAL.name;
        setPosition(new Vector2f(x, y));
        setWidth((Integer) C.Logic.TILE_SIZE.data);
        setHeight((Integer) C.Logic.TILE_SIZE.data);
    }
    
    public Goal(float x, float y, Level lvl) {
        this(x, y);
        this.lvl = lvl;
    }
    
    @Override
    public void render(GameContainer gc, Graphics g) {
        super.render(gc, g);
        tileSet.render(32, getX(), getY());
    }

    
    public boolean hitPlayer(Player p) {
        return this.r.intersects(p.getR());
    }
    
}
