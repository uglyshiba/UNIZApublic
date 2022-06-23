package sk.uniza.fri.ships;

import sk.uniza.fri.abilities.TimeSlow;
import sk.uniza.fri.projectiles.enemyprojectile.EnemyProjectile;
import sk.uniza.fri.projectiles.PlayerProjectile;
import sk.uniza.fri.ships.enemies.EnemyShip;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 5/14/2022 - 9:59 PM
 * This class implements actions the player can make (shoot, move, slow time), also sets basic properties for the PlayerShip
 * and serves as an identifier. Also implements player collision with other enemies, and slowTime management.
 * @author radoz
 */
public class PlayerShip extends Ship<EnemyProjectile> {

    private static final double SHOOTING_COOLDOWN = 0.3 * 1000000000;
    private static final String PLAYER_SHIP_MODEL_URL = "src\\sk\\uniza\\fri\\assets\\playerShip2_blue.png";
    private static final int MOVE_SPEED = 8;
    private static final int HEALTH = 1000;
    private static final int ENERGY = 600;
    private int currentEnergy = ENERGY;

    private final TimeSlow timeSlow;

    private double previousShootingTime;

    public PlayerShip(int initPosX, int initPosY, TimeSlow timeSlow) {
        super(initPosX, initPosY, HEALTH, MOVE_SPEED);

        try {
            BufferedImage shipModel = ImageIO.read(new File(PLAYER_SHIP_MODEL_URL));
            super.setModel(shipModel);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.previousShootingTime = 0;
        this.timeSlow = timeSlow;
    }

    public void collisionWithEnemyShip (CopyOnWriteArrayList<EnemyShip> enemyShipList) {
        for (EnemyShip enemyShip : enemyShipList) {
            int xShipCollision = Math.abs((super.getPosX() + (super.getModel().getWidth() / 2)) - (enemyShip.getPosX() + (enemyShip.getModel().getWidth() / 2)));
            int yShipCollision = Math.abs((super.getPosY() + (super.getModel().getHeight() / 2)) - (enemyShip.getPosY() + (enemyShip.getModel().getHeight() / 2)));
            if (xShipCollision <= super.getModel().getWidth() && yShipCollision <= super.getModel().getHeight()) {
                super.takeDamage(enemyShip.getImpactDamage());
                enemyShipList.remove(enemyShip);
            }
        }
    }

    /**
     * Implementation of player fire. The player can shoow only in high enough intervals.
     * @param currentTime current program time for comapartion of shooting cooldown.
     * @return new PlayerProjectile, in the case the player is allowed to shoot, otherwise null.
     */
    public PlayerProjectile shoot(double currentTime) {
        if (currentTime - this.previousShootingTime >= SHOOTING_COOLDOWN) {
            this.previousShootingTime = currentTime;
            return new PlayerProjectile(this.getPosX() + (this.getModel().getWidth() / 2),
                    (this.getPosY() + this.getModel().getHeight()));
        }
        return null;
    }

    public void move(int directionX, int directionY) {
        int newPosX = super.getPosX() + (directionX * super.getMoveSpeed());
        int newPosY = super.getPosY() + (directionY * super.getMoveSpeed());

        if ((newPosX > 0) && (newPosX < super.getScreenWidth() - super.getModel().getWidth()) &&
                ((newPosY > 0) && (newPosY < 680))) {
            super.move(directionX, directionY);
        }
    }

    /**
     * Sets the TimeSlow status to inUse. It depletes the player's energy. If the energy is low enough,
     * player can't use the ability.
     */
    public void useTimeSlow() {
        if (this.currentEnergy - 5 >= 0) {
            this.currentEnergy -= 5;
            if (this.currentEnergy >= 25) {
                this.timeSlow.startUsing();
            }
        }
    }

    /**
     * Calls the TimeSlow use function, which depending if the ability is currently in use, either slows the enemies,
     * projectiles and scroll background speed, or restores their previous speeds.
     * In the case the ability is not in use, it replenishes player's energy.
     */
    public void actualizeTimeSlow() {
        if (!this.timeSlow.isInUse()) {
            if (this.currentEnergy + 5 <= 600) {
                this.currentEnergy += 5;
            }
        }
        this.timeSlow.use();
    }

    public int getCurrentEnergy() {
        return this.currentEnergy;
    }
}

