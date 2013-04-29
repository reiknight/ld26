/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jam.ld26.entities;

import infinitedog.frisky.entities.Entity;
import jam.ld26.game.C;
import jam.ld26.levels.Level;
import jam.ld26.tiles.TileSet;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Reik Val
 */
public abstract class Enemy extends Entity {
    
    private static int number = 0;
    protected Level lvl;
    private Vector2f initialPosition;
    
    protected TileSet tileSet = new TileSet(C.Textures.ENEMIES_TILE_SET.name, 
            (Integer) C.Logic.TILE_SIZE.data);
    
    public Enemy(float x, float y) {
        name = C.Entities.ENEMY.name + ++number;
        group = C.Groups.ENEMIES.name;
        setPosition(new Vector2f(x, y));
        setWidth((Integer) C.Logic.TILE_SIZE.data);
        setHeight((Integer) C.Logic.TILE_SIZE.data);
    }
    
    public Enemy(float x, float y, Level lvl) {
        this(x, y);
        this.lvl = lvl;
        this.initialPosition = new Vector2f(x, y);
    }
    
    public boolean hitPlayer(Player p) {
        return this.r.intersects(p.getR());
    }

    public void setLvl(Level lvl) {
        this.lvl = lvl;
    }
            
    public TileSet getTileSet() {
        return tileSet;
    }      
    
    public void reset(Level lvl) {
        setPosition(initialPosition);
    }
    
    public abstract int getType();
    
}
