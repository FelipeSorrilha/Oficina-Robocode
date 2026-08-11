package evasivo;

import robocode.*;

public class Evasivo extends AdvancedRobot {

    static final double DISTANCIA_MINIMA = 220;
    static final double ENERGIA_CRITICA = 15;

    int direcao = 1;

    public void run() {
        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);

        while (true) {

            // Movimento constante
            setAhead(120 * direcao);
            setTurnRight(35 * direcao);

            // Radar continua procurando
            setTurnRadarRight(360);

            execute();
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {

        // Se o inimigo estiver perto, aumenta a distancia 
        if (e.getDistance() < DISTANCIA_MINIMA) {
            direcao *= -1;
            setBack(120);
            setTurnRight(60 * direcao);
        }

        // Tiro fraco, a prioridade eh sobreviver
        if (getGunHeat() == 0 && Math.abs(e.getBearing()) < 15) {
            if (e.getDistance() < 150) {
                setFire(1.5);
            } else {
                setFire(1);
            }
        }
    }

    public void onHitByBullet(HitByBulletEvent e) {
        
        // Foi atingido: muda a direcao para dificultar o proximo tiro
        direcao *= -1;
        setTurnRight(70 * direcao);
        setAhead(120 * direcao);
    }

    public void onHitWall(HitWallEvent e) {

        // Parede tambem forca uma mudanca de direcao.
        direcao *= -1;
        setBack(100);
        setTurnRight(90);
    }
}
