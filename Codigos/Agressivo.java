package agressivo;

import robocode.*;

public class Agressivo extends AdvancedRobot {

    static final double DISTANCIA_LONGE = 250;
    static final double DISTANCIA_PERTO = 100;
    static final double ENERGIA_CRITICA = 10;

    public void run() {
        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);

        while (true) {
            // Mantem o radar e o canho procurando um inimigo
            setTurnRadarRight(360);
            setTurnGunRight(360);

            // Continua avancando
            setAhead(150);

            execute();
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {

        // Tenta ficar mais perto do inimigo
        if (e.getDistance() > DISTANCIA_PERTO) {
            setTurnRight(e.getBearing());
            setAhead(Math.min(e.getDistance() - DISTANCIA_PERTO, 150));
        }

        // Se tiver muito perto, nao fica soh parado
        else {
            setTurnRight(e.getBearing() + 90);
            setAhead(50);
        }

        // Atira mais forte quando esta perto
        if (getGunHeat() == 0) {
            if (e.getDistance() < 100) {
                setFire(3);
            } else if (e.getDistance() < DISTANCIA_LONGE) {
                setFire(2);
            } else {
                setFire(1);
            }
        }
    }

    public void onHitByBullet(HitByBulletEvent e) {

        // continua avancando e muda levemente a direcao.
        setTurnRight(25);
        setAhead(100);
    }

    public void onHitWall(HitWallEvent e) {
        
        // Evita ficar preso na parede.
        setBack(80);
        setTurnRight(90);
    }
}
