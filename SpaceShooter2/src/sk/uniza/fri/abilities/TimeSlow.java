package sk.uniza.fri.abilities;

import sk.uniza.fri.Panel;
import sk.uniza.fri.projectiles.enemyprojectile.EnemyProjectile;
import sk.uniza.fri.ships.enemies.EnemyShip;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 5/14/2022 - 9:59 PM
 * Consists of three functions, which affect the speed of enemy projectiles, enemy ships, and the speed of a scrolling
 * background when in use. When not in use, it resets the said speeds to their previous value
 * @author radoz
 */
public class TimeSlow implements IAbilities {



    private final CopyOnWriteArrayList<EnemyShip> enemyShipsList;
    private final CopyOnWriteArrayList<EnemyProjectile> enemyProjectilesList;
    private final Panel panel;
    private boolean isInUse;

    public TimeSlow(CopyOnWriteArrayList<EnemyShip> enemyShipsList, CopyOnWriteArrayList<EnemyProjectile> enemyProjectilesList,
                    Panel panel) {
        this.enemyShipsList = enemyShipsList;
        this.enemyProjectilesList = enemyProjectilesList;
        this.panel = panel;
    }

    private void projectileManipulation() {
        for (EnemyProjectile projectile : this.enemyProjectilesList) {
            if (this.isInUse) {
                projectile.slowProjectile();
            } else {
                projectile.restoreProjectileSpeed();
            }
        }
    }

    private void slowShips() {
        for (EnemyShip ship : this.enemyShipsList) {
            if (this.isInUse) {
                ship.slowShip();
            } else {
                ship.restoreShipSpeed();
            }
        }
    }

    private void slowBackground() {
        if (this.isInUse) {
            this.panel.slowScrollingSpeed();
        } else {
            this.panel.restoreScrollingSpeed();
        }
    }

    public void startUsing() {
        this.isInUse = true;
    }


    /**
     *  Calls the speed controlling functions. Afterwards sets the isInUse to false, in the case of being in use, it is set
     *  to true by player controls.
     */
    public void use() {
        this.projectileManipulation();
        this.slowShips();
        this.slowBackground();

        this.isInUse = false;
    }

    public boolean isInUse() {
        return this.isInUse;
    }
}
