package sk.uniza.fri.ships.enemies;

import sk.uniza.fri.projectiles.enemyprojectile.EnemyProjectile;

/**
 * 5/14/2022 - 9:59 PM
 * Instantiable enemy ship. This one does not shoot any projectiles, instead it's focused on colliding with the player.
 * Fastest ship, least durable, purposed to overwhelm the player from the sides of the map in great numbers.
 * @author radoz
 */
public class EnemyShipDrone extends EnemyShip {
    private static final int UP = -1;
    private static final int DOWN = 1;
    private static final int LEFT = -1;
    private static final int RIGHT = 1;
    private static final int NOWHERE = 0;

    private static final int IMPACT_DAMAGE = 30;
    private static final int HEALTH = 50;
    private static final int MOVE_SPEED = 8;
    private static final String SHIP_MODEL_URL = "src\\sk\\uniza\\fri\\assets\\enemyGreen3.png";

    private final int movementType;

    public EnemyShipDrone(int initPosX, int initPosY, int movementType) {
        super(initPosX, initPosY, HEALTH, MOVE_SPEED, SHIP_MODEL_URL);
        super.setImpactDamage(IMPACT_DAMAGE);
        this.movementType = movementType;
    }

    public EnemyProjectile shoot(double currentTime) {
        return null;
    }

    public void moveEnemy() {
        this.changeDirections();
        super.move();
    }

    /**
     * Depending on the side of the map, it either can head diagonally to the right or left.
     */
    private void changeDirections() {
        if (this.movementType == 1) {
            super.setDirectionX(NOWHERE);
            super.setDirectionY(DOWN);
        } else {
            if (super.getPosX() < super.getScreenWidth() / 6) {
                super.setDirectionX(RIGHT);
                super.setDirectionY(DOWN);
            } else if (super.getPosX() > super.getScreenWidth() - super.getScreenWidth() / 6) {
                super.setDirectionX(LEFT);
                super.setDirectionY(DOWN);
            }
        }
    }
}
