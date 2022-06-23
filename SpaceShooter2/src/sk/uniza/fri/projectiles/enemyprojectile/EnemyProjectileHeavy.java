package sk.uniza.fri.projectiles.enemyprojectile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 5/14/2022 - 9:59 PM
 * Slower, bigger, more hard-hitting projectile. Fired by heavier enemy ships.
 * @author radoz
 */
public class EnemyProjectileHeavy extends EnemyProjectile {
    private static final int PROJECTILE_DAMAGE = 60;
    private static final int PROJECTILE_SPEED = 4;
    private static final int PROJECTILE_DIRECTION = 1;
    private static final String PROJECTILE_BODY_URL = "src\\sk\\uniza\\fri\\assets\\laserRed04.png";

    public EnemyProjectileHeavy(int initPosX, int initPosY) {
        super(initPosX, initPosY);

        try {
            BufferedImage projectileBody = ImageIO.read(new File(PROJECTILE_BODY_URL));
            super.setProjectileBody(projectileBody);
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.setSpeed(PROJECTILE_SPEED);
        super.setDamage(PROJECTILE_DAMAGE);
        super.setDirection(PROJECTILE_DIRECTION);
    }
}
