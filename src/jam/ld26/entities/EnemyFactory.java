package jam.ld26.entities;

import jam.ld26.game.C;
import jam.ld26.game.C.Enemies;
import jam.ld26.levels.Level;
import org.newdawn.slick.geom.Vector2f;

public class EnemyFactory {
    public static Enemy createEnemy(Integer id, Vector2f position, Level lvl) {
        Enemy enemy = null;
        
        if(id == Enemies.SQUARE_ENEMY.id) {
            enemy = new SquareEnemy(position, lvl);
        } else if(id == Enemies.LAZY_CIRCLE_ENEMY.id) {
            enemy = new LazyCircleEnemy(position, lvl);
        } else if(id == Enemies.ASSHOLE_TRIANGLE_ENEMY.id) {
            enemy = new AssholeTriangleEnemy(position, lvl);
        }
        
        return enemy;
    }
}
