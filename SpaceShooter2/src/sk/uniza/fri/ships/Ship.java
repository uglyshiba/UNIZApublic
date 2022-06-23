package sk.uniza.fri.ships;
import sk.uniza.fri.projectiles.Projectile;

import java.awt.image.BufferedImage;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 5/14/2022 - 9:59 PM
 * Abstract class. Since the shooting functions of Player and Enemy ships are different (mostly in the terms of return values),
 * this class takes care of similiar functions, which are movement and hit detection.
 * @author radoz
 */
public abstract class Ship<T extends Projectile> {

    private int posX;
    private int posY;
    private int moveSpeed;
    private final int initialHealth;
    private int health;
    private BufferedImage shipModel;
    private static final int SCREEN_WIDTH = 600;
    private static final int SCREEN_HEIGHT = 800;

    protected Ship(int initPosX, int initPosY, int health, int moveSpeed) {
        this.posX = initPosX;
        this.posY = initPosY;
        this.health = health;
        this.initialHealth = health;
        this.moveSpeed = moveSpeed;
    }

    public void move(int directionX, int directionY) {
        this.posX = this.posX + (this.moveSpeed * directionX);
        this.posY = this.posY + (this.moveSpeed * directionY);
    }

    /**
     * Generic function, which accepts an array of opposite projectiles (Player projectiles for enemies, Enemy projectiles
     * for Player). Checks every projectile in an array, and then compares its value to the instance of ship. If the projectile
     * is close enough, it detects the hit, detracts the health of the ship based on projectile damage, and destroys said
     * projectile.
     * @param projectileArray generic type projectile array.
     */
    public void hitDetection(CopyOnWriteArrayList<T> projectileArray) {
        for (Projectile hitProjectile : projectileArray) {
            if ((((hitProjectile.getPosX() - this.getPosX()) <= this.shipModel.getWidth()) &&
                    ((hitProjectile.getPosX() - this.getPosX()) >= 0))
                    &&
                    (((hitProjectile.getPosY() - this.getPosY()) <= this.shipModel.getHeight()) &&
                    (hitProjectile.getPosY() - this.getPosY()) >= 0)) {
                this.takeDamage(hitProjectile.getDamage());
                projectileArray.remove(hitProjectile);
            }
        }
    }

    public boolean isDestroyed() {
        return this.health <= 0;
    }

    public int getPosX() {
        return this.posX;
    }

    public int getPosY() {
        return this.posY;
    }

    protected void setPosX(int newPos) {
        this.posX = newPos;
    }

    protected void setPosY(int newPos) {
        this.posY = newPos;
    }

    protected void takeDamage(int dmg) {
        this.health -= dmg;
    }

    protected void setMoveSpeed(int newSpeed) {
        this.moveSpeed = newSpeed;
    }

    protected int getMoveSpeed() {
        return this.moveSpeed;
    }

    public BufferedImage getModel() {
        return this.shipModel;
    }

    protected void setModel(BufferedImage newModel) {
        this.shipModel = newModel;
    }

    protected int getScreenWidth() {
        return SCREEN_WIDTH;
    }

    protected int getScreenHeight() {
        return SCREEN_HEIGHT;
    }

    public int getHealth() {
        return this.health;
    }

    public int getInitialHealth() {
        return this.initialHealth;
    }
}
