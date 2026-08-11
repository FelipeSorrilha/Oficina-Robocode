package estrategico;

import robocode.*;

public class Estrategico extends AdvancedRobot {

    static final double DISTANCIA_IDEAL = 180;
    static final double DISTANCIA_MINIMA = 80;
    static final double ENERGIA_CRITICA = 20;

    int direcao = 1;

    public void run() {
        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);

        while (true) {
            setTurnRadarRight(360);
            execute();
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {

        double distancia = e.getDistance();


        // Muito perto = recua
        if (distancia < DISTANCIA_MINIMA) {
            setBack(100);
            setTurnRight(45 * direcao);
        }

        // Muito longe - aproxima
        else if (distancia > DISTANCIA_IDEAL + 80) {
            setAhead(120);
            setTurnRight(e.getBearing());
        }

        // Se tiver entre os dois, fica rodeando
        else {
            setTurnRight(e.getBearing() + (90 * direcao));
            setAhead(80);
        }


        if (getEnergy() < ENERGIA_CRITICA) {

            // Se energia = baixa, prioriza sobreviver
            setBack(150);
            direcao *= -1;
        }


        if (getGunHeat() == 0 && Math.abs(e.getBearing()) < 12) {

            if (getEnergy() < ENERGIA_CRITICA) {
                // Poupa energia se a energia estiver critica
                setFire(1);
            } else if (distancia < 100) {
                setFire(3);
            } else if (distancia < 250) {
                setFire(2);
            } else {
                setFire(1);
            }
        }
    }

    public void onHitByBullet(HitByBulletEvent e) {

        // Alterna a direcao para evitar repeticao
        direcao *= -1;
        setTurnRight(60 * direcao);
        setAhead(100 * direcao);
    }

    public void onHitWall(HitWallEvent e) {
        setBack(80);
        setTurnRight(90);
        direcao *= -1;
    }

    public void onHitRobot(HitRobotEvent e) {

        // Evita permanecer colado no inimigo
        setBack(100);
        setTurnRight(60);
        direcao *= -1;
    }
}
