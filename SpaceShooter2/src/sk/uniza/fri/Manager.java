package sk.uniza.fri;

import sk.uniza.fri.abilities.TimeSlow;
import sk.uniza.fri.projectiles.enemyprojectile.EnemyProjectile;
import sk.uniza.fri.projectiles.PlayerProjectile;
import sk.uniza.fri.projectiles.Projectile;
import sk.uniza.fri.ships.enemies.EnemyShip;
import sk.uniza.fri.ships.PlayerShip;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * 5/14/2022 - 9:59 PM
 *This class manages the program flow. It consists of the main running loop, and managing functions (or objects),
 * which manage the movement of ships, projectiles, hit detection, and enemy spawning.
 * @author radoz
 */
public class Manager extends JFrame {

    private Panel panel;
    private PlayerShip playerShip;
    private final CopyOnWriteArrayList<Integer> pressedKeysList;
    private boolean running;

    private static final int FPS = 60;
    private static final double SEC_NANO_CONVERSION = 1000000000;
    private static final double FPS_IN_NANO = SEC_NANO_CONVERSION / FPS;

    private final CopyOnWriteArrayList<PlayerProjectile> playerProjectileList;
    private final CopyOnWriteArrayList<EnemyProjectile> enemyProjectileList;
    private final CopyOnWriteArrayList<EnemyShip> enemyShipList;
    private final Spawner spawner;

    private static final int SCREEN_HEIGHT = 800;
    private static final int SCREEN_WIDTH = 600;

    private static final ArrayList<Integer> INPUT_KEYS = new ArrayList<>(Arrays.asList(
            KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_X, KeyEvent.VK_C
    ));


    public Manager() {
        this.running = true;
        this.enemyProjectileList = new CopyOnWriteArrayList<>();
        this.playerProjectileList = new CopyOnWriteArrayList<>();
        this.pressedKeysList = new CopyOnWriteArrayList<>();
        this.enemyShipList = new CopyOnWriteArrayList<>();
        this.spawner = new Spawner(this.enemyShipList);
        this.init();
    }

    private void init() {
        this.setTitle("Space Shooter");
        this.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        Manager.this.panel = new Panel(Manager.this.playerShip);
        this.playerShip = new PlayerShip(SCREEN_WIDTH / 2, SCREEN_HEIGHT - 200, new TimeSlow(
                this.enemyShipList, this.enemyProjectileList, this.panel
        ));
        this.add(this.panel);
        this.setVisible(true);

        this.addPlayerActions();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Manager.this.update();
            }
        });

        thread.start();
    }

    /**
     * When a reserved button is pressed, it is added to an array. The button's code is added into an array of pressed
     * buttons, and when the key is released, it is removed.
     */
    private void addPlayerActions() {
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (INPUT_KEYS.contains(e.getKeyCode()) && !Manager.this.pressedKeysList.contains(e.getKeyCode())) {
                    Manager.this.pressedKeysList.add(e.getKeyCode());
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                Manager.this.pressedKeysList.removeIf(keyEvent -> keyEvent == e.getKeyCode());
            }
        });
    }

    /**
     * This function checks every pressed button (of reserved buttons), from the pressed buttons array, and responds with
     * corresponding player action.
     */
    private void playerActions() {
        if (!this.pressedKeysList.isEmpty()) {
            for (Integer keyCode : this.pressedKeysList) {
                if (keyCode == KeyEvent.VK_RIGHT) {
                    this.playerShip.move(1, 0);
                }

                if (keyCode == KeyEvent.VK_LEFT) {
                    this.playerShip.move(-1, 0);
                }

                if (keyCode == KeyEvent.VK_UP) {
                    this.playerShip.move(0, -1);
                }

                if (keyCode == KeyEvent.VK_DOWN) {
                    this.playerShip.move(0, 1);
                }

                if (keyCode == KeyEvent.VK_X) {
                    PlayerProjectile newProjectile = this.playerShip.shoot(System.nanoTime());
                    if (newProjectile != null) {
                        this.playerProjectileList.add(newProjectile);
                    }
                }

                if (keyCode == KeyEvent.VK_C) {
                    this.playerShip.useTimeSlow();
                }
            }
        }
    }

    public void update() {
        long deltaTime = Long.MAX_VALUE;
        long endTime = 0;
        while (this.running) {
            if (deltaTime >= FPS_IN_NANO) {
                this.playerActions();
                this.actualizeAbilities();
                this.spawner.enemySpawner();
                this.enemyShipMovement();
                this.enemyShipsShoot();
                this.moveProjectiles(this.playerProjectileList);
                this.moveProjectiles(this.enemyProjectileList);
                this.hitDetection();
                this.collisionDetection();
                this.panel.updatePlayerProjectives(new ArrayList<>(this.playerProjectileList));
                this.panel.updateEnemyProjectiles(new ArrayList<>(this.enemyProjectileList));
                this.panel.updateEnemyShips(new ArrayList<>(this.enemyShipList));
                this.panel.updatePlayerShip(this.playerShip);
                this.panel.draw();
                endTime = System.nanoTime();
            }

            deltaTime = System.nanoTime() - endTime;
        }
    }


    private void actualizeAbilities() {
        this.playerShip.actualizeTimeSlow();
    }

    private void enemyShipMovement() {
        for (EnemyShip enemyShip : this.enemyShipList) {
            enemyShip.moveEnemy();
            if (enemyShip.isOutOfBounds()) {
                this.enemyShipList.remove(enemyShip);
            }
        }
    }

    private void enemyShipsShoot() {
        for (EnemyShip enemyShip : this.enemyShipList) {
            EnemyProjectile newProjectile = enemyShip.shoot(System.nanoTime());
            if (newProjectile != null) {
                this.enemyProjectileList.add(newProjectile);
            }
        }
    }

    private void moveProjectiles(CopyOnWriteArrayList<? extends Projectile> projectileList) {
        if (!projectileList.isEmpty()) {
            for (Projectile projectile : projectileList) {
                if ((projectile.getPosY() <= 0) || (projectile.getPosY() >= 800)) {
                    projectileList.remove(projectile);
                } else {
                    projectile.move();
                }
            }
        }
    }

    /**
     * For every exisiting ships on the board, checks every corresponding (enemy) projectile. And determines if the ship
     * is hit. In the case that player ship gets destroyed, it ends the game.
     */
    private void hitDetection() {
        this.playerShip.hitDetection(this.enemyProjectileList);
        if (this.playerShip.isDestroyed()) {
            this.running = false;
            this.gameOver();
        }
        for (EnemyShip enemyShip : this.enemyShipList) {
            enemyShip.hitDetection(this.playerProjectileList);
            if (enemyShip.isDestroyed()) {
                this.enemyShipList.remove(enemyShip);
            }
        }
    }

    /**
     * Checks if the player ship collides with enemy ships.
     */
    private void collisionDetection() {
        this.playerShip.collisionWithEnemyShip(this.enemyShipList);
    }


    private void gameOver() {
        System.out.println("Game over");
        System.exit(0);
    }
}



