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
            // Radar sempre girando pra achar um inimigo
            setTurnRadarRight(360);

            // Continua avancando
            setAhead(150);

            execute();
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {

        double distancia = e.getDistance();

        // Mira o canhao direto no inimigo (independente de pra onde o corpo vira)
        double anguloCanhao = normalizeBearing(getHeading() - getGunHeading() + e.getBearing());
        setTurnGunRight(anguloCanhao);

        // Tenta ficar mais perto do inimigo
        if (distancia > DISTANCIA_PERTO) {
            setTurnRight(e.getBearing());
            setAhead(Math.min(distancia - DISTANCIA_PERTO, 150));
        }
        // Se tiver muito perto, nao fica soh parado
        else {
            setTurnRight(e.getBearing() + 90);
            setAhead(50);
        }

        // So atira quando o canhao ja esta apontado pro inimigo
        if (getGunHeat() == 0 && Math.abs(anguloCanhao) < 15) {
            if (distancia < 100) {
                setFire(3);
            } else if (distancia < DISTANCIA_LONGE) {
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

    // Deixa o angulo sempre entre -180 e 180, pro canhao girar pelo caminho mais curto
    double normalizeBearing(double angulo) {
        while (angulo > 180) angulo -= 360;
        while (angulo < -180) angulo += 360;
        return angulo;
    }
}
