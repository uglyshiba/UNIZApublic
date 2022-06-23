package sk.uniza.fri.ships.enemies;

import sk.uniza.fri.projectiles.enemyprojectile.EnemyProjectile;
import sk.uniza.fri.projectiles.PlayerProjectile;
import sk.uniza.fri.ships.Ship;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * 5/14/2022 - 9:59 PM
 * Abstract class which handles shooting, direction switching, and slowing down and restoring during SlowTime ability use.
 * @author radoz
 */
public abstract class EnemyShip extends Ship<PlayerProjectile> {

    private double minShootCooldown = 0.7;
    private double maxShootCooldown = 1.5;
    private final double minShootCooldownNanos = this.minShootCooldown * SEC_TO_NANO;
    private final double maxShootCooldownNanos = this.maxShootCooldown * SEC_TO_NANO;
    private static final double SEC_TO_NANO = 1000000000;
    private int impactDamage; //Damage player takes when he collides with enemy ship.
    private double shootingCooldown;
    private double previousShootTime;//Control variable for the time of ship's last firing.
    private final Random rand;
    private boolean isSlowed = false;
    private int directionX;
    private int directionY;

    public EnemyShip(int initPosX, int initPosY, int health, int moveSpeed, String modelURL) {
        super(initPosX, initPosY, health, moveSpeed);
        this.rand = new Random();
        this.shootingCooldown = this.rand.nextDouble(this.minShootCooldownNanos, this.maxShootCooldownNanos);
        this.previousShootTime = 0;

        try {
            BufferedImage newModel = ImageIO.read(new File(modelURL));
            super.setModel(newModel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public EnemyProjectile shoot(double currentTime, EnemyProjectile projectile) {
        if (currentTime - this.previousShootTime >= this.shootingCooldown) {
            this.previousShootTime = currentTime;
            this.shootingCooldown = this.rand.nextDouble(this.minShootCooldownNanos, this.maxShootCooldownNanos);
            return projectile;
        }
        return null;
    }

    public void move() {
        super.move(this.directionX, this.directionY);
    }

    public boolean isOutOfBounds() {
        if ((this.getPosX() < -500 || this.getPosX() > (super.getScreenWidth() + 500))) {
            return true;
        }

        return (this.getPosY() > (super.getScreenHeight()) + 200) || this.getPosY() < -400;
    }

    public abstract void moveEnemy();
    public abstract EnemyProjectile shoot(double currentTime);

    /**
     * Prolongs the minimum and maximum shoot cooldown, so that the enemy ship takes longer to fire. Also slows down ship's speed.
     */
    public void slowShip() {
        if (!this.isSlowed) {
            this.setMinShootCooldown((this.minShootCooldownNanos / SEC_TO_NANO) * 2);
            this.setMaxShootCooldown((this.maxShootCooldownNanos / SEC_TO_NANO) * 2);
            super.setMoveSpeed(super.getMoveSpeed() / 2);
            this.isSlowed = true;
        }
    }

    /**
     * Restores the slowed down variables to the previous values.
     */
    public void restoreShipSpeed() {
        if (this.isSlowed) {
            this.setMinShootCooldown((this.minShootCooldownNanos / SEC_TO_NANO) / 2);
            this.setMaxShootCooldown((this.maxShootCooldownNanos / SEC_TO_NANO) / 2);
            super.setMoveSpeed(super.getMoveSpeed() * 2);
            this.isSlowed = false;
        }
    }

    public void setMinShootCooldown(double newCoolodown) {
        this.minShootCooldown = newCoolodown;
    }

    public void setMaxShootCooldown(double newCoolodown) {
        this.maxShootCooldown = newCoolodown;
    }

    protected void setImpactDamage(int newImpactDamage) {
        this.impactDamage = newImpactDamage;
    }

    public int getImpactDamage() {
        return this.impactDamage;
    }

    protected void setDirectionX(int newDirection) {
        this.directionX = newDirection;
    }

    protected  void setDirectionY(int newDirection) {
        this.directionY = newDirection;
    }
}
