package sk.uniza.fri;

import sk.uniza.fri.projectiles.enemyprojectile.EnemyProjectile;
import sk.uniza.fri.projectiles.PlayerProjectile;
import sk.uniza.fri.ships.Ship;
import sk.uniza.fri.ships.enemies.EnemyShip;
import sk.uniza.fri.ships.PlayerShip;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import java.awt.Image;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 5/14/2022 - 9:59 PM
 * Class, that takes care of drawing the entire map and everything on it. It uses JComponent.
 * @author radoz
 */
public class Panel extends JComponent {
    private static final int SCREEN_WIDTH = 600;
    private static final int SCREEN_HEIGHT = 800;
    private BufferedImage backgroundImage1;
    private BufferedImage backgroundImage2;
    private PlayerShip playerShip;
    private Image scaledBackgroundImage1;
    private Image scaledBackgroundImage2;
    private int backgroundScrollingSpeed = 25;
    private boolean slowed = false;
    private Image cyanBar;
    private int backgroundImage1YPos;
    private int backgroundImage2YPos;
    private ArrayList<PlayerProjectile> playerProjectileList;
    private ArrayList<EnemyShip> enemyShipList;
    private ArrayList<EnemyProjectile> enemyProjectileList;


    public Panel(PlayerShip playerShip) {
        this.playerProjectileList = new ArrayList<>();
        this.enemyShipList = new ArrayList<>();
        this.enemyProjectileList = new ArrayList<>();
        this.playerShip = playerShip;
        this.init();
    }

    public void init() {
        this.backgroundImage1YPos = 0;
        this.backgroundImage2YPos = -800;

        try {
            this.backgroundImage1 = ImageIO.read(new File("src\\sk\\uniza\\fri\\assets\\blue.png"));
            this.backgroundImage2 = ImageIO.read(new File("src\\sk\\uniza\\fri\\assets\\blue.png"));
            this.cyanBar = ImageIO.read(new File("src\\sk\\uniza\\fri\\assets\\cyanBar1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.scaledBackgroundImage1 = this.backgroundImage1.getScaledInstance(800, 1000, Image.SCALE_SMOOTH);
        this.scaledBackgroundImage2 = this.backgroundImage2.getScaledInstance(800, 1000, Image.SCALE_SMOOTH);
    }

    private void scrollBackground() {
        if (this.backgroundImage2YPos >= -25) {
            this.backgroundImage2YPos = -800;
            this.backgroundImage1YPos = 0;
        }
        this.backgroundImage1YPos += this.backgroundScrollingSpeed;
        this.backgroundImage2YPos += this.backgroundScrollingSpeed;
    }

    /**
     * This function is used in update() function. this.repaint() calls paintComponent.
     */
    public void draw() {
        this.repaint();
        this.scrollBackground();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.drawBackground(g);
        this.drawPlayerShip(g);
        this.drawPlayerProjectiles(g);
        this.drawEnemyShips(g);
        this.drawEnemyProjectiles(g);
        this.drawPlayerHealthBar(g);
        this.drawPlayerPowerBar(g);
    }

    private void drawBackground(Graphics g) {
        g.drawImage(this.scaledBackgroundImage1, 0, this.backgroundImage1YPos, 800, 1000, null);
        g.drawImage(this.scaledBackgroundImage2, 0, this.backgroundImage2YPos, 800, 1000, null);
    }

    private void drawPlayerShip(Graphics g) {
        g.drawImage(this.playerShip.getModel(), this.playerShip.getPosX(), this.playerShip.getPosY(),
                this.playerShip.getModel().getWidth(), this.playerShip.getModel().getHeight(), null);
    }

    private void drawPlayerProjectiles(Graphics g) {
        if (!this.playerProjectileList.isEmpty()) {
            for (PlayerProjectile projectile : this.playerProjectileList) {
                g.drawImage(projectile.getProjectileBody(), projectile.getPosX(), projectile.getPosY(),
                        projectile.getProjectileBody().getWidth(), projectile.getProjectileBody().getHeight(), null);
            }
        }
    }

    private void drawPlayerHealthBar(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(0, SCREEN_HEIGHT - 60, (int)(this.playerShip.getHealth() * ((double)SCREEN_WIDTH / this.playerShip.getInitialHealth())), 30);
    }

    private void drawPlayerPowerBar(Graphics g) {
        g.drawImage(this.cyanBar, 0, SCREEN_HEIGHT - 90, this.playerShip.getCurrentEnergy(), 30, null);
    }

    private void drawEnemyShips(Graphics g) {
        if (!this.enemyShipList.isEmpty()) {
            for (EnemyShip enemyShip : this.enemyShipList) {
                g.drawImage(enemyShip.getModel(), enemyShip.getPosX(), enemyShip.getPosY(),
                        enemyShip.getModel().getWidth(), enemyShip.getModel().getHeight(), null);
                this.drawShipHealthBar(g, enemyShip);
            }
        }
    }

    private void drawEnemyProjectiles(Graphics g) {
        if (!this.enemyProjectileList.isEmpty()) {
            for (EnemyProjectile projectile : this.enemyProjectileList) {
                g.drawImage(projectile.getProjectileBody(), projectile.getPosX(), projectile.getPosY(),
                        projectile.getProjectileBody().getWidth(), projectile.getProjectileBody().getHeight(), null);
            }
        }
    }


    private void drawShipHealthBar(Graphics g, Ship ship) {
        g.setColor(Color.GREEN);
        g.drawRect(ship.getPosX(), ship.getPosY() + 10, (int)(ship.getHealth() * ((double)ship.getModel().getWidth() / ship.getInitialHealth())), 5);
        g.fillRect(ship.getPosX(), ship.getPosY() + 10, (int)(ship.getHealth() * ((double)ship.getModel().getWidth() / ship.getInitialHealth())), 5);
    }

    public void slowScrollingSpeed () {
        if (!this.slowed) {
            this.backgroundScrollingSpeed /= 2;
            this.slowed = true;
        }

    }

    public void restoreScrollingSpeed () {
        if (this.slowed) {
            this.backgroundScrollingSpeed *= 2;
            this.slowed = false;
        }
    }

    public void updatePlayerShip(PlayerShip playerShip) {
        this.playerShip = playerShip;
    }

    public void updatePlayerProjectives(ArrayList<PlayerProjectile> projectileList) {
        this.playerProjectileList = projectileList;
    }

    public void updateEnemyProjectiles(ArrayList<EnemyProjectile> projectileList) {
        this.enemyProjectileList = projectileList;
    }

    public void updateEnemyShips(ArrayList<EnemyShip> enemyShipList) {
        this.enemyShipList = enemyShipList;
    }
}

