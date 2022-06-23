package sk.uniza.fri.projectiles;

import java.awt.image.BufferedImage;

/**
 * 5/14/2022 - 9:59 PM
 * Manages basic projectile movement, and management of projectile properties.
 * @author radoz
 */
public abstract class Projectile {
    private final int posX;
    private int posY;
    private int speed;
    private int direction;
    private int damage;

    public Projectile(int initPosX, int initPosY) {
        this.posX = initPosX;
        this.posY = initPosY;
    }

    public void move() {
        int newPosY = this.getPosY() + (this.getSpeed() * this.direction);
        this.setPosY(newPosY);
    }

    public int getPosX() {
        return this.posX;
    }

    public int getPosY() {
        return this.posY;
    }

    public int getDamage() {
        return this.damage;
    }

    protected int getSpeed() {
        return this.speed;
    }

    protected void setPosY(int newPosY) {
        this.posY = newPosY;
    }

    protected void setSpeed(int newSpeed) {
        this.speed = newSpeed;
    }

    protected void setDirection(int newDirection) {
        this.direction = newDirection;
    }

    public abstract BufferedImage getProjectileBody();

    protected void setDamage(int newDamage) {
        this.damage = newDamage;
    }
}
