package sk.uniza.fri.ships.enemies;


import sk.uniza.fri.projectiles.enemyprojectile.EnemyProjectile;
import sk.uniza.fri.projectiles.enemyprojectile.EnemyProjectileSmall;

/**
 * 5/14/2022 - 9:59 PM
 * Instantiable enemy ship class. This one is medium fast, medium durability, and smaller projectile damage.
 * Has preprogrammed moving patterns, which highly cooperates with Spawner class, that instantiates this ship.
 * @author radoz
 */
public class EnemyShipCorvette extends EnemyShip {

    private static final int UP = -1;
    private static final int DOWN = 1;
    private static final int LEFT = -1;
    private static final int RIGHT = 1;
    private static final int NOWHERE = 0;

    private static final int IMPACT_DAMAGE = 50;
    private static final int HEALTH = 70;
    private static final int MOVE_SPEED = 5;
    private static final double MIN_SHOOT_COOLDOWN = 0.5;
    private static final double MAX_SHOOT_COOLDOWN = 1.2;

    private static final String SHIP_MODEL_URL = "src\\sk\\uniza\\fri\\assets\\enemyRed2.png";
    private final int movementType;

    public EnemyShipCorvette(int initPosX, int initPosY, int movementType) {
        super(initPosX, initPosY, HEALTH, MOVE_SPEED, SHIP_MODEL_URL);
        this.movementType = movementType;

        super.setMinShootCooldown(MIN_SHOOT_COOLDOWN);
        super.setMaxShootCooldown(MAX_SHOOT_COOLDOWN);
        super.setImpactDamage(IMPACT_DAMAGE);
    }

    public EnemyProjectile shoot(double currentTime) {
        return super.shoot(currentTime, new EnemyProjectileSmall(super.getPosX(), super.getPosY()));
    }

    public void moveEnemy() {
        this.changeDirections();
        super.move();
    }

    /**
     * Direction switching based on the position on the map. Has 2 moving patterns. The first one after traversing approximately
     * half of the map's height flies off to the sides. The second one after traversing the same distance stops, and tries to form
     * a formation with other ships of the same type.
     */
    private void changeDirections() {
        int fourTenthsHeight = ((super.getScreenHeight() / 10) * 4);
        int halfWidth = super.getScreenWidth() / 2;
        switch (this.movementType) {
            case 1:
                if (super.getPosY() <= fourTenthsHeight) {
                    super.setDirectionX(NOWHERE);
                    super.setDirectionY(DOWN);
                } else if (super.getPosY() > fourTenthsHeight && (super.getPosX() - halfWidth) > 0) {
                    super.setDirectionX(RIGHT);
                    super.setDirectionY(DOWN);
                } else if (super.getPosY() > fourTenthsHeight && (super.getPosX() - halfWidth) < 0) {
                    super.setDirectionX(LEFT);
                    super.setDirectionY(DOWN);
                } else {
                    super.setDirectionX(LEFT);
                    super.setDirectionY(DOWN);
                }
                break;
            case 2:
                if (super.getPosY() <= fourTenthsHeight) {
                    super.setDirectionX(NOWHERE);
                    super.setDirectionY(DOWN);
                } else if (Math.abs(super.getPosX() - (super.getScreenWidth() / 2)) <= 90) {
                    //System.out.printf("SHIP: X:%d Y:%d  MIDDLE:%d%n", super.getPosX(), super.getPosY(), super.getScreenWidth() / 2);
                    super.setDirectionX(NOWHERE);
                    super.setDirectionY(NOWHERE);
                } else if ((Math.abs(super.getPosX() - (super.getScreenWidth() / 2)) <= 90 * 2) &&
                        (super.getPosY() >= fourTenthsHeight + 100)) {
                    //System.out.println("Sushi");
                    super.setDirectionX(NOWHERE);
                    super.setDirectionY(NOWHERE);
                } else if ((Math.abs(super.getPosX() - (super.getScreenWidth() / 2)) <= 90 * 3) &&
                        (super.getPosY() >= fourTenthsHeight + 200)) {
                    super.setDirectionX(NOWHERE);
                    super.setDirectionY(NOWHERE);
                }
                break;
        }
    }
}

