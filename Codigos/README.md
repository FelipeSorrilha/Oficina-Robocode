<p align="center">

# Guia de Comandos — Robocode

**Referência para customizar os robôs-base da oficina**

</p>

---

Cada robô da oficina parte de uma das três bases prontas: **Agressivo**, **Estratégico** ou **Evasivo**. Esse guia reúne os comandos que você pode usar pra modificar qualquer uma delas: mover, girar, atirar, reagir a eventos, e por fim transformar uma ideia em uma linha de código nova.

Todos usam `AdvancedRobot`, que separa os comandos dos momentos em que eles de fato acontecem. Você enfileira os comandos e só quando chama `execute()` é que eles rodam, todos juntos, no mesmo instante — isso é o que deixa movimento, radar e canhão independentes entre si.

> Documentação oficial: **https://robocode.sourceforge.io/docs/robocode/**

<br>

## Índice

| | | |
|---|---|---|
| [Os robôs-base](#os-robos-base) | [Por onde começar](#por-onde-comecar) | [Estrutura básica](#estrutura-basica) |
| [Movimento](#movimento) | [Rotação do robô](#rotacao-do-robo) | [Radar](#radar) |
| [Canhão e tiro](#canhao-e-tiro) | [Quando enxerga um inimigo](#quando-enxerga-um-inimigo) | [Info do seu robô](#informacoes-do-seu-robo) |
| [Eventos importantes](#eventos-importantes) | [Condições](#condicoes) | [Combinações úteis](#combinacoes-uteis) |
| [Variáveis](#variaveis-memoria) | [Constantes](#constantes-da-estrategia) | [Meta → Regra → Código](#meta-regra-codigo) |

---

<a name="os-robos-base"></a>
## Os robôs-base

| Robô | Perfil | Constantes de partida |
|---|---|---|
| **Agressivo** | Avança sem parar e atira sempre que pode, sente a pressão neném! | `DISTANCIA_LONGE`, `DISTANCIA_PERTO`, `ENERGIA_CRITICA` |
| **Estratégico** | Mantém uma distância ideal e recua quando a energia fica crítica | `DISTANCIA_IDEAL`, `DISTANCIA_MINIMA`, `ENERGIA_CRITICA` |
| **Evasivo** | Prioriza sobreviver, mantém distância mínima grande e atira fofo | `DISTANCIA_MINIMA`, `ENERGIA_CRITICA` |

Escolha uma base e use o restante deste guia pra ajustar, adicionar ou substituir qualquer parte do comportamento dela.

---

<a name="por-onde-comecar"></a>
## Por onde começar

**Comece simples — mexa em um valor de cada vez:**

- Alterar distância ideal
- Alterar distância mínima
- Alterar energia crítica
- Alterar potência dos tiros
- Alterar velocidade
- Alterar ângulo de movimento
- Alterar reação a tiros
- Alterar reação à parede

**Depois, tente algo maior:**

- Criar uma nova regra
- Criar um novo estado
- Usar mais de uma informação do inimigo ao mesmo tempo
- Criar uma estratégia de emergência
- Fazer o robô adaptar seu comportamento sozinho

<br>

---

<a name="estrutura-basica"></a>
## Estrutura básica

Todo robô começa assim:

```java
package meu_robo;

import robocode.*;

public class MeuRobo extends AdvancedRobot {

    public void run() {
        while (true) {
            // comportamento principal
            execute();
        }
    }
}
```

---

<a name="movimento"></a>
## Movimento

| Ação | Bloqueante | Não-bloqueante |
|---|---|---|
| Mover pra frente | `ahead(100);` | `setAhead(100);` |
| Mover pra trás | `back(100);` | `setBack(100);` |

```java
execute();           // roda os comandos não-bloqueantes pendentes
setMaxVelocity(8);    // limita a velocidade máxima
```

> **Nota:** a versão bloqueante (`ahead`, `back`, `turnRight`...) trava o robô até o movimento terminar. A versão `set...` só enfileira, o robô continua livre pra também girar o canhão ou o radar no mesmo instante, até você chamar `execute()`.

---

<a name="rotacao-do-robo"></a>
## Rotação do robô

| Ação | Bloqueante | Não-bloqueante |
|---|---|---|
| Girar à direita | `turnRight(90);` | `setTurnRight(90);` |
| Girar à esquerda | `turnLeft(90);` | `setTurnLeft(90);` |

Combinando rotação e movimento no mesmo instante:

```java
setTurnRight(45);
setAhead(100);
execute();
```

---

<a name="radar"></a>
## Radar

| Ação | Bloqueante | Não-bloqueante |
|---|---|---|
| Girar o radar | `turnRadarRight(360);` | `setTurnRadarRight(360);` |

```java
setAdjustRadarForRobotTurn(true); // radar não gira junto com o corpo
setAdjustRadarForGunTurn(true);   // radar não gira junto com o canhão
```

> **Nota:** sem esses dois ajustes, o radar herda o giro do corpo e do canhão, ele vai sumir do alvo toda hora que seu robô se mexer.

---

<a name="canhao-e-tiro"></a>
## Canhão e tiro

**Girar o canhão**

| Ação | Bloqueante | Não-bloqueante |
|---|---|---|
| Girar o canhão | `turnGunRight(90);` | `setTurnGunRight(90);` |

```java
setAdjustGunForRobotTurn(true); // canhão não gira junto com o corpo
```

**Atirar**

| Bloqueante | Não-bloqueante |
|---|---|
| `fire(1);` `fire(2);` `fire(3);` | `setFire(1);` `setFire(2);` `setFire(3);` |

A potência vai de `0.1` até `3.0`. Quanto **maior** a potência:

| | Ao aumentar a potência |
|---|---|
| + | Causa mais dano |
| + | Devolve mais energia se acertar |
| − | Gasta mais energia sua pra atirar |
| − | A bala fica mais lenta |

---

<a name="quando-enxerga-um-inimigo"></a>
## Quando enxerga um inimigo

O método abaixo é chamado automaticamente toda vez que o radar detecta um robô:

```java
public void onScannedRobot(ScannedRobotEvent e) {
    out.println(e.getDistance());
}
```

**Informações disponíveis em `e`:**

| Informação | Comando |
|---|---|
| Distância | `e.getDistance()` |
| Ângulo em relação a você | `e.getBearing()` |
| Energia do inimigo | `e.getEnergy()` |
| Velocidade do inimigo | `e.getVelocity()` |
| Direção do inimigo | `e.getHeading()` |
| Nome | `e.getName()` |

---

<a name="informacoes-do-seu-robo"></a>
## Informações do seu robô

| Informação | Comando |
|---|---|
| Sua energia | `getEnergy()` |
| Sua posição | `getX()` · `getY()` |
| Sua direção | `getHeading()` |
| Sua velocidade | `getVelocity()` |
| Calor do canhão | `getGunHeat()` |
| Direção do canhão | `getGunHeading()` |
| Direção do radar | `getRadarHeading()` |
| Largura do campo | `getBattleFieldWidth()` |
| Altura do campo | `getBattleFieldHeight()` |

---

<a name="eventos-importantes"></a>
## Eventos importantes

| Evento | Dispara quando... |
|---|---|
| `onHitByBullet(HitByBulletEvent e)` | você é atingido por uma bala |
| `onHitWall(HitWallEvent e)` | você bate na parede |
| `onHitRobot(HitRobotEvent e)` | você bate em outro robô |
| `onRobotDeath(RobotDeathEvent e)` | algum robô morre |
| `onWin(WinEvent e)` | você ganha a rodada |

```java
public void onHitByBullet(HitByBulletEvent e) {
    // reação
}
```

---

<a name="condicoes"></a>
## Condições

```java
// Comparar distância
if (e.getDistance() < 100) {
    back(100);
}

// Comparar energia
if (getEnergy() < 20) {
    back(150);
}

// Comparar direção
if (e.getBearing() > 0) {
    turnRight(30);
}

// Verificar se está quase alinhado
if (Math.abs(e.getBearing()) < 10) {
    fire(2);
}
```

---

<a name="combinacoes-uteis"></a>
## Combinações úteis

| Situação | Código |
|---|---|
| Inimigo muito perto | `if (e.getDistance() < 80) back(100);` |
| Inimigo muito longe | `if (e.getDistance() > 300) ahead(150);` |
| Energia baixa | `if (getEnergy() < 20) back(150);` |
| Tiro forte perto | `if (e.getDistance() < 100) fire(3);` |
| Tiro fraco longe | `if (e.getDistance() > 300) fire(1);` |
| Mudar direção | `direction *= -1;` |

---

<a name="variaveis-memoria"></a>
## Variáveis (memória)

Variáveis guardam o estado do agente entre uma decisão e outra — é o que dá memória ao robô.

```java
int direction = 1;
// ...mais tarde...
direction *= -1;
```

```java
double enemyDistance;
// dentro do evento:
enemyDistance = e.getDistance();
```

---

<a name="constantes-da-estrategia"></a>
## Constantes da estratégia

Uma boa prática é colocar os valores importantes no topo do código — os 3 robôs-base já seguem essa ideia:

```java
static final double DISTANCIA_IDEAL = 180;
static final double ENERGIA_CRITICA = 20;
static final double POTENCIA_MAXIMA = 3;
```

Assim fica fácil testar variações — trocar

```java
DISTANCIA_IDEAL = 180
```

por

```java
DISTANCIA_IDEAL = 250
```

e comparar o resultado já é, sozinho, uma forma válida de testar estratégia.

---

<a name="meta-regra-codigo"></a>
## Meta → Regra → Código

O caminho pra transformar uma ideia em código sempre passa por esses três passos:

| Meta | Regra | Código |
|---|---|---|
| Sobreviver | Se o inimigo estiver muito perto, fugir | `if (e.getDistance() < 100) back(150);` |
| Causar dano rapidamente | Se estiver perto, usar tiro forte | `if (e.getDistance() < 120) fire(3);` |
| Economizar energia | Se a energia estiver baixa, usar tiros fracos | `if (getEnergy() < 20) fire(1);` |

---

## Recursos

- Documentação oficial do Robocode: https://robocode.sourceforge.io/docs/robocode/
- Download do Robocode Classic: https://robocode.sourceforge.io/

<br>

<p align="center">
ROBOAP · UTFPR
</p>
