package sk.uniza.fri;

import sk.uniza.fri.ships.enemies.EnemyShip;
import sk.uniza.fri.ships.enemies.EnemyShipCorvette;
import sk.uniza.fri.ships.enemies.EnemyShipDrone;
import sk.uniza.fri.ships.enemies.EnemyShipHeavy;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 5/14/2022 - 9:59 PM
 * Class which takes care of spawning enemies in random numbers (in range) and in random (predefined) places.
 * Also randomly rotates predefined enemy movement patterns.
 * @author radoz
 */
public class Spawner {
    private long cooldownSpawnTimer;
    private double enemySpawnTimeNanos;
    private int randomEnemyType;
    private int spawnNumber;
    private double betweenSpawnTimeNanos;
    private final Random rand;
    private int randomEnemyMovement;
    private int initSpawnNumber;
    private long betweenCooldownTimer;

    private static final int SCREEN_HEIGHT = 800;
    private static final int SCREEN_WIDTH = 600;
    private static final int SEC_NANO_CONVERSION = 1000000000;
    private final CopyOnWriteArrayList<EnemyShip> enemyShipList;

    public Spawner(CopyOnWriteArrayList<EnemyShip> enemyShipList) {
        this.rand = new Random();
        this.enemyShipList = enemyShipList;
        this.spawnNumber = 5;
        this.initSpawnNumber = this.spawnNumber;
        this.randomEnemyType = 1;
        this.randomEnemyMovement = 1;

        this.enemySpawnTimeNanos = 7.0 * SEC_NANO_CONVERSION;
        this.betweenSpawnTimeNanos = SEC_NANO_CONVERSION;
        this.betweenCooldownTimer = 0;
        this.cooldownSpawnTimer = 0;
    }

    /**
     * Randomly rotates the spawned enemy types, their predefined paths and spawning speed.
     */
    public void enemySpawner() {
        double currentTime = System.nanoTime();
        if ((currentTime - this.cooldownSpawnTimer) > this.enemySpawnTimeNanos) {
            if (this.randomEnemyType == 1) {
                this.enemySpawnerCorvette(currentTime);
            } else if (this.randomEnemyType == 2) {
                this.enemySpawnerHeavy(currentTime);
            } else if (this.randomEnemyType == 3) {
                this.enemySpawnerDrone(currentTime);
            }
            if (this.spawnNumber == 0) {
                if (this.randomEnemyType == 3) {
                    this.betweenSpawnTimeNanos = this.rand.nextDouble(0.2, 0.3) * SEC_NANO_CONVERSION;
                    this.enemySpawnTimeNanos = this.rand.nextDouble(0.5, 1.5) * SEC_NANO_CONVERSION;
                } else {
                    this.betweenSpawnTimeNanos = this.rand.nextDouble(0.3, 0.7) * SEC_NANO_CONVERSION;
                    this.enemySpawnTimeNanos = this.rand.nextDouble(1.0, 3.0) * SEC_NANO_CONVERSION;
                }

                this.randomEnemyMovement = this.rand.nextInt(1, 3);
                this.randomEnemyType = this.rand.nextInt(1, 4);

                if (this.randomEnemyType == 2) {
                    this.spawnNumber = 2;
                } else if (this.randomEnemyType == 3) {
                    this.spawnNumber = this.rand.nextInt(6, 10);
                    this.betweenSpawnTimeNanos = this.rand.nextDouble(0.1, 0.3) * SEC_NANO_CONVERSION;
                } else {
                    this.spawnNumber = this.rand.nextInt(3, 7);
                }
                this.initSpawnNumber = this.spawnNumber;
                this.cooldownSpawnTimer = System.nanoTime();
            }
        }
    }

    private void enemySpawnerCorvette(double currentTime) {
        if (this.spawnNumber != 0) {
            if ((currentTime - this.betweenCooldownTimer) >= this.betweenSpawnTimeNanos) {
                int initXPos = (int)(SCREEN_WIDTH / 2 + ((this.initSpawnNumber - this.spawnNumber) * 60) * Math.pow(-1, this.spawnNumber));
                int initYPos = -200 + (this.initSpawnNumber - this.spawnNumber) * 40;
                this.enemyShipList.add(new EnemyShipCorvette(initXPos, initYPos, this.randomEnemyMovement));
                this.betweenCooldownTimer = System.nanoTime();
                this.spawnNumber--;
            }
        }
    }

    private void enemySpawnerHeavy(double currentTime) {
        if (this.spawnNumber != 0) {
            if ((currentTime - this.betweenCooldownTimer) >= this.betweenSpawnTimeNanos) {
                int initXPos = (int)((SCREEN_WIDTH / 2) - 50 + (200 * Math.pow(-1, this.spawnNumber)));
                int initYPos = -100;
                this.enemyShipList.add(new EnemyShipHeavy(initXPos, initYPos));
                this.betweenCooldownTimer = System.nanoTime();
                this.spawnNumber--;
            }
        }
    }

    private void enemySpawnerDrone(double currentTime) {
        if (this.spawnNumber != 0) {
            if ((currentTime - this.betweenCooldownTimer) >= this.betweenSpawnTimeNanos) {
                int initXPos;
                int initYPos;
                if (this.randomEnemyMovement == 1) {
                    initXPos = (SCREEN_WIDTH / this.initSpawnNumber) * this.spawnNumber;
                    initYPos = -100;

                } else {
                    initXPos = (int)((SCREEN_WIDTH / 2) + (SCREEN_WIDTH / 2 + 100) * Math.pow(-1, this.spawnNumber));
                    initYPos = (SCREEN_HEIGHT / this.initSpawnNumber) * ((this.initSpawnNumber - this.spawnNumber) / 2);

                }
                this.enemyShipList.add(new EnemyShipDrone(initXPos, initYPos, this.randomEnemyMovement));
                this.betweenCooldownTimer = System.nanoTime();
                this.spawnNumber--;
            }
        }
    }
}
