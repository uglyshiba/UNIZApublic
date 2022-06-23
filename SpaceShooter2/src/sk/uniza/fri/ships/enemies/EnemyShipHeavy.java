package sk.uniza.fri.ships.enemies;

import sk.uniza.fri.projectiles.enemyprojectile.EnemyProjectile;
import sk.uniza.fri.projectiles.enemyprojectile.EnemyProjectileHeavy;

/**
 * 5/14/2022 - 9:59 PM
 * Instantiable enemy ship class. This one is slow, big, durable, and hard hitting. Harded projectile damage, much higher impact damage.
 * Has preprogrammed moving pattern, which highly cooperates with Spawner class, that instantiates this ship.
 * @author radoz
 */
public class EnemyShipHeavy extends EnemyShip {
    private static final int UP = -1;
    private static final int DOWN = 1;
    private static final int LEFT = -1;
    private static final int RIGHT = 1;
    private static final int NOWHERE = 0;

    private static final int HEALTH = 300;
    private static final int IMPACT_DAMAGE = 100;
    private static final int MOVE_SPEED = 2;
    private static final double MIN_SHOOT_COOLDOWN = 1;
    private static final double MAX_SHOOT_COOLDOWN = 2;

    private static final String SHIP_MODEL_URL = "src\\sk\\uniza\\fri\\assets\\enemyBlack4.png";

    public EnemyShipHeavy(int initPosX, int initPosY) {
        super(initPosX, initPosY, HEALTH, MOVE_SPEED, SHIP_MODEL_URL);
        super.setMinShootCooldown(MIN_SHOOT_COOLDOWN);
        super.setMaxShootCooldown(MAX_SHOOT_COOLDOWN);
        super.setImpactDamage(IMPACT_DAMAGE);
    }

    public EnemyProjectile shoot(double currentTime) {
        return super.shoot(currentTime, new EnemyProjectileHeavy(this.getPosX() + super.getModel().getWidth() / 2, this.getPosY()));
    }

    public void moveEnemy() {
        this.changeDirections();
        super.move();
    }

    /**
     * This ship type has only one preprogrammed path. It is predetermined to occupy the sides of the map.
     */
    private void changeDirections() {
        super.setDirectionX(NOWHERE);
        super.setDirectionY(DOWN);
    }
}
