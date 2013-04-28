/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jam.ld26.entities;

import infinitedog.frisky.entities.Entity;
import jam.ld26.game.C;
import jam.ld26.tiles.TileSet;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Reik Val
 */
public abstract class Enemy extends Entity {
    
    private static int number = 0;
    
    protected TileSet tileSet = new TileSet(C.Textures.TILE_SET.name, 
            (Integer) C.Logic.TILE_SIZE.data);
    
    public Enemy(float x, float y) {
        name = C.Entities.ENEMY.name + ++number;
        group = C.Groups.ENEMIES.name;
        setPosition(new Vector2f(x, y));
        setWidth((Integer) C.Logic.TILE_SIZE.data);
        setHeight((Integer) C.Logic.TILE_SIZE.data);
    }
    
    public boolean hitPlayer(Player p) {
        if(this.r.intersects(p.getR())) {
            System.out.println("Colisiona");
        } else {
            System.out.println();
        }
        return this.r.intersects(p.getR());
    }
    
}
