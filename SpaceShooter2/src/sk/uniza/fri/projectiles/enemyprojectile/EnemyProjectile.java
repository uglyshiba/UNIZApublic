package sk.uniza.fri.projectiles.enemyprojectile;

import sk.uniza.fri.projectiles.Projectile;
import java.awt.image.BufferedImage;

/**
 * 5/14/2022 - 9:59 PM
 * Superclass for various enemy projectile types. Apart from the basic identifying manages the movement slow during using
 * the TimeSloe ability.
 * @author radoz
 */
public abstract class EnemyProjectile extends Projectile {

    private BufferedImage projectileBody;
    private boolean isSlowed = false;

    public EnemyProjectile(int initPosX, int initPosY) {
        super(initPosX, initPosY);
    }

    public BufferedImage getProjectileBody() {
        return this.projectileBody;
    }

    protected void setProjectileBody(BufferedImage newProjectileBody) {
        this.projectileBody = newProjectileBody;
    }

    public void slowProjectile() {
        if (!this.isSlowed) {
            super.setSpeed(super.getSpeed() / 2);
            this.isSlowed = true;
        }
    }

    public void restoreProjectileSpeed() {
        if (this.isSlowed) {
            super.setSpeed(super.getSpeed() * 2);
            this.isSlowed = false;
        }
    }
}
