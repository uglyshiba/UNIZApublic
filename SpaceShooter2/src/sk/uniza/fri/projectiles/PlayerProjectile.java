package sk.uniza.fri.projectiles;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 5/14/2022 - 9:59 PM
 * Represents the player projectile, and sets its properties. Superclass takes care of actual movement. Serves more as an
 * identifier.
 * @author radoz
 */
public class PlayerProjectile extends Projectile {

    private BufferedImage projectileBody;
    private static final int PROJECTILE_DAMAGE = 50;
    private static final int PROJECTILE_SPEED = 30;
    private static final int PROJECTILE_DIRECTION = -1;

    public PlayerProjectile(int initPosX, int initPosY) {
        super(initPosX, initPosY);
        try {
            this.projectileBody = ImageIO.read(new File("src\\sk\\uniza\\fri\\assets\\laserBlue07.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.setDamage(PROJECTILE_DAMAGE);
        super.setSpeed(PROJECTILE_SPEED);
        super.setDirection(PROJECTILE_DIRECTION);
    }

    public BufferedImage getProjectileBody() {
        return this.projectileBody;
    }
}
