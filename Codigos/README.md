<p align="center">

# 🤖 Guia Robocode


</p>

---

## Como usar este guia

Pra começar, a ideia é simples:

1. **Escolha** um robô-base (3 opções logo abaixo).
2. **Leia** a explicação dele: cada trecho do código tem uma descrição em português.
3. **Mude um número** e veja o que acontece.
4. **Escolha funções novas** pra deixar o robô mais esperto (você não precisa usar todas).
5. **Teste** numa batalha e compare com a versão anterior.

Pense em cada comando como um **bloco**: você encaixa, tira, troca de lugar e testa.

> 📖 Documentação oficial (pra quando quiser ir além): https://robocode.sourceforge.io/docs/robocode/

---

## 👉 Qual robô você escolheu?

| Se você quer um robô que... | Clique aqui |
|---|---|
| Parte pra cima, não para de avançar e atira sempre que pode | [**🔴 Agressivo**](#robo-agressivo) |
| Mantém distância ideal, recua quando está fraco e muda de comportamento | [**🟡 Estratégico**](#robo-estrategico) |
| Foge, dá voltas e prioriza sobreviver | [**🔵 Evasivo**](#robo-evasivo) |

Quer consultar um comando ou um limite de valor? Vá direto pra [**Tabela de referência**](#tabela-de-referencia) logo abaixo. Ela vale para os três robôs.

---

<a name="tabela-de-referencia"></a>
# 📋 Tabela de referência (vale pra qualquer robô)

## 1. Limites do jogo

Esses são os valores máximos e mínimos que o Robocode aceita.

| O quê | Mínimo | Máximo | Observação |
|---|---|---|---|
| Energia | 0 | 100 (no início) | Chegou a 0, seu robô é destruído. Acertar tiros pode fazer passar de 100. |
| Velocidade do robô | -8 | 8 | Negativo = andando de ré. |
| Potência do tiro | 0.1 | 3.0 | Veja a tabela de potência logo abaixo. |
| Giro do corpo por instante | 0 | 10° (cai com a velocidade) | Quanto mais rápido você anda, mais devagar vira. |
| Giro do canhão por instante | 0 | 20° | Independente do corpo se você usar `setAdjustGunForRobotTurn(true)`. |
| Giro do radar por instante | 0 | 45° | Independente do canhão se você usar `setAdjustRadarForGunTurn(true)`. |
| Alcance do radar | 0 | 1200 pixels | Inimigos mais longe que isso não são vistos. |
| Ângulo `getBearing()` | -180 | 180 | Positivo = inimigo à sua direita. Negativo = à esquerda. |
| Direção `getHeading()` | 0 | 360 | 0 = norte, 90 = leste, 180 = sul, 270 = oeste. |
| Tamanho do campo | 400 × 400 | 5000 × 5000 | O padrão é 800 × 600. Use `getBattleFieldWidth()` e `getBattleFieldHeight()` pra saber. |
| Tamanho do robô | 36 × 36 | 36 × 36 | Fixo. |

## 2. Tabela de potência do tiro (a decisão mais importante)

Escolher a potência é sempre uma troca: **dano e energia de volta** contra **gasto de energia e demora**.

| Potência | Dano | Energia devolvida se acertar | Energia gasta pra atirar | Velocidade da bala | Espera até atirar de novo |
|---|---|---|---|---|---|
| 0.1 | 0.4 | 0.3 | 0.1 | 19.7 | ~10 instantes |
| 1 | 4 | 3 | 1 | 17 | 12 instantes |
| 2 | 10 | 6 | 2 | 14 | 14 instantes |
| 3 | 16 | 9 | 3 | 11 | 16 instantes |

Em resumo: tiro **forte** machuca mais, mas é lento (mais fácil de desviar) e custa caro. Tiro **fraco** é rápido e barato, mas machuca pouco.

## 3. Blocos de comando

Quase todo comando tem duas versões:

- **Bloqueante** (`ahead`, `turnRight`...): o robô faz **só aquilo** e espera terminar.
- **Não-bloqueante** (`setAhead`, `setTurnRight`...): só **anota** o comando. Tudo que foi anotado acontece junto quando você chama `execute()`.

Os robôs-base usam as versões `set...` porque assim o robô anda, gira e atira **ao mesmo tempo**.

### 🟦 Movimento do corpo

| Comando | O que faz | Valores aceitos |
|---|---|---|
| `setAhead(d)` / `ahead(d)` | Anda pra frente `d` pixels | Qualquer número. Negativo anda pra trás. |
| `setBack(d)` / `back(d)` | Anda de ré `d` pixels | Qualquer número. Negativo anda pra frente. |
| `setTurnRight(g)` / `turnRight(g)` | Gira `g` graus pra direita | Qualquer número. Negativo gira pra esquerda. |
| `setTurnLeft(g)` / `turnLeft(g)` | Gira `g` graus pra esquerda | Qualquer número. Negativo gira pra direita. |
| `setMaxVelocity(v)` | Limita a velocidade máxima | 0 a 8 |
| `setMaxTurnRate(g)` | Limita o giro do corpo por instante | 0 a 10 |
| `stop()` | Para tudo (guarda o que faltava fazer) | n/a |
| `resume()` | Continua o que o `stop()` parou | n/a |

### 🟨 Canhão e tiro

| Comando | O que faz | Valores aceitos |
|---|---|---|
| `setFire(p)` / `fire(p)` | Atira com potência `p` | 0.1 a 3.0 |
| `setTurnGunRight(g)` / `turnGunRight(g)` | Gira o canhão `g` graus pra direita | Qualquer número. Negativo gira pra esquerda. |
| `setTurnGunLeft(g)` / `turnGunLeft(g)` | Gira o canhão `g` graus pra esquerda | Qualquer número. Negativo gira pra direita. |
| `setAdjustGunForRobotTurn(true)` | Canhão **não** gira junto com o corpo | `true` ou `false` |

### 🟩 Radar

| Comando | O que faz | Valores aceitos |
|---|---|---|
| `setTurnRadarRight(g)` / `turnRadarRight(g)` | Gira o radar `g` graus pra direita | Qualquer número. Negativo gira pra esquerda. |
| `setTurnRadarLeft(g)` / `turnRadarLeft(g)` | Gira o radar `g` graus pra esquerda | Qualquer número. Negativo gira pra direita. |
| `setAdjustRadarForGunTurn(true)` | Radar **não** gira junto com o canhão | `true` ou `false` |
| `setAdjustRadarForRobotTurn(true)` | Radar **não** gira junto com o corpo | `true` ou `false` |
| `scan()` | Força uma leitura do radar agora | n/a |

### ⏱️ Controle

| Comando | O que faz |
|---|---|
| `execute()` | Executa tudo que foi anotado com `set...` |
| `getTime()` | Quantos instantes se passaram na rodada |
| `getRoundNum()` | Número da rodada atual (começa em 0) |
| `getOthers()` | Quantos inimigos ainda estão vivos |

### 🎨 Visual (só estética, mas deixa o robô com a sua cara)

| Comando | O que faz |
|---|---|
| `setColors(corpo, canhao, radar)` | Pinta o robô. Precisa de `import java.awt.Color;` no topo. Exemplo: `setColors(Color.RED, Color.BLACK, Color.ORANGE);` |
| `setBulletColor(cor)` | Pinta as balas |

## 4. Perguntas que o robô pode fazer (informações)

### Sobre você

| Comando | Devolve | Faixa de valores |
|---|---|---|
| `getEnergy()` | Sua energia | 0 a 100+ |
| `getX()` · `getY()` | Sua posição | 18 até (tamanho do campo - 18) |
| `getHeading()` | Pra onde seu corpo aponta | 0 a 360 |
| `getVelocity()` | Sua velocidade atual | -8 a 8 |
| `getGunHeat()` | Calor do canhão. **Só atira com 0.** | 0 a ~3 |
| `getGunHeading()` | Pra onde seu canhão aponta | 0 a 360 |
| `getRadarHeading()` | Pra onde seu radar aponta | 0 a 360 |
| `getBattleFieldWidth()` | Largura do campo | 400 a 5000 |
| `getBattleFieldHeight()` | Altura do campo | 400 a 5000 |

### Sobre o inimigo (dentro de `onScannedRobot(ScannedRobotEvent e)`)

| Comando | Devolve | Faixa de valores |
|---|---|---|
| `e.getDistance()` | Distância até ele | 0 a 1200 |
| `e.getBearing()` | Ângulo dele em relação à frente do seu corpo | -180 a 180 |
| `e.getEnergy()` | Energia dele | 0 a 100+ |
| `e.getVelocity()` | Velocidade dele | -8 a 8 |
| `e.getHeading()` | Pra onde ele está indo | 0 a 360 |
| `e.getName()` | Nome dele | texto |

## 5. Eventos (o robô reage sozinho quando algo acontece)

| Evento | Dispara quando... | O que você pode perguntar |
|---|---|---|
| `onScannedRobot(ScannedRobotEvent e)` | o radar vê um inimigo | `e.getDistance()`, `e.getBearing()`, `e.getEnergy()`... |
| `onHitByBullet(HitByBulletEvent e)` | uma bala te acerta | `e.getBearing()` (de onde veio), `e.getPower()` (força do tiro) |
| `onBulletHit(BulletHitEvent e)` | **seu** tiro acerta alguém | `e.getEnergy()` (energia que sobrou nele) |
| `onBulletMissed(BulletMissedEvent e)` | **seu** tiro erra (sai do campo) | n/a |
| `onHitWall(HitWallEvent e)` | você bate na parede | `e.getBearing()` |
| `onHitRobot(HitRobotEvent e)` | você bate em outro robô | `e.getBearing()`, `e.getEnergy()` |
| `onRobotDeath(RobotDeathEvent e)` | um robô morre | `e.getName()` |
| `onDeath(DeathEvent e)` | **você** morre | n/a |
| `onWin(WinEvent e)` | você vence a rodada | n/a |

## 6. Ferramentas de decisão e de memória

### Decisões (`if`)

| Quero... | Código |
|---|---|
| Fazer algo **se** uma condição for verdadeira | `if (e.getDistance() < 100) { ... }` |
| ...e outra coisa **senão** | `if (...) { ... } else { ... }` |
| Testar uma segunda condição | `else if (...) { ... }` |
| Exigir duas condições ao mesmo tempo (**e**) | `if (a && b)` |
| Aceitar qualquer uma das duas (**ou**) | `if (a \|\| b)` |
| Ignorar o sinal de um ângulo | `Math.abs(angulo)` |

### Matemática útil

| Código | O que faz |
|---|---|
| `Math.min(a, b)` | O menor dos dois |
| `Math.max(a, b)` | O maior dos dois |
| `Math.random()` | Um número aleatório entre 0 e 1 (use pra ser imprevisível) |
| `Math.abs(x)` | Valor sem sinal (-5 vira 5) |

### Memória (variáveis)

Uma variável guarda um valor **entre uma decisão e outra**. É assim que o robô "lembra" de algo.

```java
int direcao = 1;          // guarda 1 ou -1
direcao *= -1;            // inverte: 1 vira -1, -1 vira 1
double energiaAntes;      // guarda um número com vírgula
boolean modoFuga = false; // guarda verdadeiro ou falso
```

Coloque a variável **fora** dos métodos (logo abaixo das constantes) pra ela não ser esquecida.

## 7. Palavras que aparecem muito

| Palavra | Significa |
|---|---|
| Instante (tick) | O "relógio" do jogo. A cada instante o robô executa o que foi anotado. |
| Bearing | Ângulo **relativo ao seu robô** (0 = bem na sua frente). |
| Heading | Ângulo **absoluto no campo** (0 = norte). |
| Constante | Número guardado no topo do código com nome, pra ser fácil de trocar. |
| Calor do canhão | Depois de atirar o canhão esquenta e precisa esfriar até 0. |

---

# 🧩 Monte seu robô: Meta → Regra → Código

Toda ideia vira código em 3 passos. Preencha como uma receita:

| Meta (o que eu quero) | Regra (quando eu faço o quê) | Código |
|---|---|---|
| Sobreviver | Se o inimigo estiver muito perto, fugir | `if (e.getDistance() < 100) setBack(150);` |
| Causar dano rápido | Se estiver perto, usar tiro forte | `if (e.getDistance() < 120) setFire(3);` |
| Economizar energia | Se a energia estiver baixa, usar tiro fraco | `if (getEnergy() < 20) setFire(1);` |
| Ser imprevisível | De vez em quando, inverter a direção | `if (Math.random() < 0.1) direcao *= -1;` |

Trocar **só um número** e comparar o resultado já é uma estratégia válida. Mude uma coisa por vez, assim você sabe o que causou a diferença.

---

<a name="robo-agressivo"></a>
# 🔴 Robô Agressivo

> **Perfil:** avança sem parar e atira sempre que pode. Quer pressão total.

[⬆ Voltar ao início](#qual-robo-voce-escolheu) · [Ir para a tabela de referência](#tabela-de-referencia)

### Constantes (os "botões" do robô, ficam no topo do código)

| Constante | Valor inicial | Pra que serve |
|---|---|---|
| `DISTANCIA_LONGE` | 250 | Acima disso o inimigo é considerado longe (tiro fraco). |
| `DISTANCIA_PERTO` | 100 | Abaixo disso o robô para de se aproximar e começa a circular o inimigo. |
| `ENERGIA_CRITICA` | 10 | Está declarada, mas **ainda não é usada** em nenhuma regra. Está esperando você. |

### Como o código funciona

**1. Preparação (`run()`)**

```java
setAdjustGunForRobotTurn(true);
setAdjustRadarForGunTurn(true);
```

Faz o canhão e o radar girarem **independentes** do corpo. O robô pode virar pra um lado e mirar pro outro.

```java
while (true) {
    setTurnRadarRight(360);
    setAhead(150);
    execute();
}
```

O laço roda pra sempre: o radar gira procurando inimigos, o corpo anda pra frente, e `execute()` faz tudo acontecer junto.

**2. Quando vê um inimigo (`onScannedRobot`)**

| Trecho | O que faz |
|---|---|
| `double distancia = e.getDistance();` | Guarda a distância pra usar nas regras. |
| `anguloCanhao = normalizeBearing(getHeading() - getGunHeading() + e.getBearing());` | Calcula **quanto o canhão precisa girar** pra apontar pro inimigo. |
| `setTurnGunRight(anguloCanhao);` | Gira o canhão essa quantidade. |
| `if (distancia > DISTANCIA_PERTO) { ... }` | Se o inimigo está longe: vira na direção dele e avança (no máximo 150) até chegar perto. |
| `else { ... }` | Se está perto: vira de lado (`+ 90`) e circula, em vez de ficar parado. |
| `if (getGunHeat() == 0 && Math.abs(anguloCanhao) < 15)` | Só atira se o canhão já esfriou **e** já está quase apontado (erro menor que 15°). |
| `fire 3 / 2 / 1` | Perto: tiro forte. Médio: tiro médio. Longe: tiro fraco. |

**3. Quando sofre algo**

| Evento | Reação do robô |
|---|---|
| `onHitByBullet` | Vira um pouco (25°) e continua avançando. |
| `onHitWall` | Dá ré e vira 90° pra não ficar preso. |

**4. `normalizeBearing`:** uma função ajudante que mantém o ângulo entre -180 e 180, pro canhão girar sempre pelo **caminho mais curto**. Você não precisa mexer nela.

### 🔧 Ajustes simples (mexa em um valor e teste)

- A distância em que ele considera o inimigo "perto" ou "longe" muda o quanto ele se aproxima.
- A potência de cada faixa de distância define se ele troca dano por economia de energia.
- A tolerância de mira (o 15) controla se ele atira mais vezes (errando mais) ou só quando está bem alinhado.
- O tamanho do avanço (150, 50, 100) muda o quão agressivo é o movimento.
- O giro ao ser atingido muda o quanto ele desvia.
- A reação à parede pode ser mais curta ou mais longa.

### 🧱 Funções novas que podem ajudar

Aqui estão várias ideias de "blocos" pra encaixar no robô. **Você não precisa usar todas, escolha só as que fizerem sentido pra sua estratégia.** Cada uma diz o que faz e onde faz sentido usar. A sintaxe exata de cada comando está na [tabela de referência](#tabela-de-referencia).

| Função | Como funciona | Onde encaixar |
|---|---|---|
| `getEnergy()` + `ENERGIA_CRITICA` | Compara a sua energia com a constante que já existe no topo (e que ainda ninguém usa). Quando estiver abaixo dela, o robô pode atirar mais fraco ou recuar. | Dentro de `onScannedRobot`, antes de escolher a potência. |
| `e.getEnergy()` | Lê a energia do **inimigo**. Se ele está quase morrendo, um tiro forte é desperdício. | Dentro de `onScannedRobot`, na escolha da potência. |
| `e.getVelocity()` | Diz se o inimigo está parado ou correndo. Alvo parado é fácil de acertar, alvo rápido pede tiro mais veloz (mais fraco). | Dentro de `onScannedRobot`, na escolha da potência. |
| `onHitRobot` | Evento que dispara quando você **esbarra** em alguém. Boa hora pra mirar e atirar à queima-roupa. | Método novo, fora dos outros. |
| `getX()` · `getY()` + `getBattleFieldWidth()` · `getBattleFieldHeight()` | Descobrem se você está perto de uma parede. Daí o robô pode virar em direção ao centro do campo antes de bater. | Dentro de `onScannedRobot` ou do laço do `run()`. |
| `e.getBearing()` dentro de `onHitByBullet` | Mostra **de que lado veio a bala**. O robô pode se mover de lado em relação a ela em vez de seguir reto. | Dentro de `onHitByBullet`. |
| Variável contadora | Uma variável que sobe a cada vez que você é atingido. Passando de certo ponto, o robô muda de tática (é "aprender" na prática). | Variável no topo, e a conta dentro de `onHitByBullet`. |
| `onBulletHit` · `onBulletMissed` | Eventos que avisam quando **seu** tiro acertou ou errou. Dá pra contar acertos e erros e ajustar a mira ou a potência. | Métodos novos, fora dos outros. |
| `Math.random()` | Sorteia um número. Serve pra, de vez em quando, mudar de rumo e ficar menos previsível. | Dentro de `onScannedRobot` ou do `run()`. |
| `setMaxVelocity(...)` | Limita a velocidade. Pode ir mais devagar pra mirar melhor ou liberar tudo pra avançar. | `run()` ou qualquer evento. |
| `onRobotDeath` | Avisa quando alguém morre. Útil pra mudar de alvo ou de estilo. | Método novo, fora dos outros. |
| `getOthers()` | Diz quantos inimigos restam. O robô pode ser mais ousado quando sobra só um. | Dentro de `onScannedRobot`. |
| `onWin` | Evento da vitória. Pode ser uma comemoração com giros. | Método novo, fora dos outros. |
| `setColors(...)` | Pinta o robô. Só estética, mas ajuda a reconhecer o seu na arena. | Início do `run()`. |

---

<a name="robo-estrategico"></a>
# 🟡 Robô Estratégico

> **Perfil:** mantém uma distância ideal do inimigo, roda em volta dele e recua quando a energia fica crítica.

[⬆ Voltar ao início](#qual-robo-voce-escolheu) · [Ir para a tabela de referência](#tabela-de-referencia)

### Constantes e variável

| Nome | Valor inicial | Pra que serve |
|---|---|---|
| `DISTANCIA_IDEAL` | 180 | A distância em que ele se sente confortável. |
| `DISTANCIA_MINIMA` | 80 | Abaixo disso ele recua. |
| `ENERGIA_CRITICA` | 20 | Abaixo disso entra em modo sobrevivência. |
| `direcao` (variável) | 1 | Guarda o **lado** pra onde ele gira: `1` ou `-1`. É a "memória" do robô. |

### Como o código funciona

**1. Preparação (`run()`)**

```java
setAdjustGunForRobotTurn(true);
setAdjustRadarForGunTurn(true);
while (true) {
    setTurnRadarRight(360);
    execute();
}
```

O canhão e o radar ficam independentes do corpo. O laço só mantém o radar girando: **todo o resto é decidido quando ele vê o inimigo.**

**2. Quando vê um inimigo (`onScannedRobot`)**

| Trecho | O que faz |
|---|---|
| `anguloCanhao = normalizeBearing(...)` + `setTurnGunRight(anguloCanhao)` | Calcula quanto girar o canhão e aponta pro inimigo. |
| `if (distancia < DISTANCIA_MINIMA)` | **Muito perto:** dá ré e gira. |
| `else if (distancia > DISTANCIA_IDEAL + 80)` | **Muito longe:** vira pro inimigo e avança. |
| `else { ... }` | **Na faixa boa:** gira 90° em relação ao inimigo e anda, ou seja, fica **rodeando** ele. |
| `if (getEnergy() < ENERGIA_CRITICA)` | **Modo sobrevivência:** dá ré forte e inverte o lado de giro. |
| `if (getGunHeat() == 0 && Math.abs(anguloCanhao) < 12)` | Só atira com canhão frio e bem alinhado. |
| `setFire(1/3/2/1)` | Energia crítica: tiro fraco (poupa). Senão: perto = forte, médio = médio, longe = fraco. |

> 💡 **Detalhe importante:** os comandos `set...` só guardam o **último** pedido. Por isso o `setBack(150)` do modo sobrevivência **substitui** o `setBack(100)` ou o `setAhead(...)` que vieram antes na mesma leitura.

**3. Quando sofre algo**

| Evento | Reação do robô |
|---|---|
| `onHitByBullet` | Inverte a `direcao` e gira/anda pro novo lado (fica imprevisível). |
| `onHitWall` | Dá ré, vira 90° e inverte a `direcao`. |
| `onHitRobot` | Dá ré, vira 60° e inverte a `direcao` (não fica colado no inimigo). |

**4. `normalizeBearing`:** função ajudante pro canhão girar pelo caminho mais curto. Não precisa mexer.

### 🔧 Ajustes simples (mexa em um valor e teste)

- A distância ideal e a mínima definem quão perto ou longe ele luta.
- O "+ 80" que define "muito longe" pode ser maior ou menor.
- A energia crítica decide **quando** ele passa a se proteger.
- A potência dos tiros em cada faixa muda o equilíbrio entre dano e economia.
- A intensidade da ré no modo sobrevivência (150) muda o quanto ele foge.
- A tolerância de mira (12) muda a frequência dos tiros.
- Os ângulos de giro ao rodear o inimigo mudam o formato da órbita.

### 🧱 Funções novas que podem ajudar

Aqui estão várias ideias de "blocos" pra encaixar no robô. **Você não precisa usar todas, escolha só as que fizerem sentido pra sua estratégia.** Cada uma diz o que faz e onde faz sentido usar. A sintaxe exata de cada comando está na [tabela de referência](#tabela-de-referencia).

| Função | Como funciona | Onde encaixar |
|---|---|---|
| Radar "grudado" no alvo | Em vez de girar o radar sem parar, calcule a direção do inimigo e gire o radar só até ele (usando `getRadarHeading()` e `e.getBearing()`). O robô não perde o alvo de vista. Funciona melhor em duelos 1 contra 1. | `onScannedRobot`, e ajuste a linha do radar no `run()` pra não brigar com esse comando. |
| Mira preditiva | Usa `e.getVelocity()`, `e.getHeading()` e a velocidade da bala pra estimar **onde o inimigo vai estar** quando a bala chegar, e mira ali. É o item mais avançado, mas o que mais aumenta o acerto. | `onScannedRobot`, no lugar do cálculo do `anguloCanhao`. |
| `Math.random()` | Sorteia quando inverter a `direcao`. Quem tenta prever seus movimentos erra mais. | Dentro de `onScannedRobot`. |
| `e.getEnergy()` | Lê a energia do **inimigo** pra decidir a potência. Tiro forte quando ele está forte, só o necessário quando está quase caindo. | Dentro de `onScannedRobot`, na escolha da potência. |
| Variável de modo (`true`/`false`) | Um interruptor "modo ataque" e "modo defesa", decidido comparando a sua energia com a do inimigo. Cada modo pode ter movimento e tiro diferentes. | Variável no topo, decisão em `onScannedRobot`. |
| `getOthers()` | Quantos inimigos restam. Muda o comportamento em batalha com vários robôs e em duelo final. | `onScannedRobot` ou `run()`. |
| Escolher o alvo mais fraco | Guarda numa variável a menor energia vista e só atira em quem estiver perto dela. Bom em batalhas com vários robôs. | Variável no topo e filtro em `onScannedRobot`. |
| `onRobotDeath` | Quando alguém morre, a "memória" do alvo é zerada pra escolher outro. | Método novo, fora dos outros. |
| `onBulletHit` · `onBulletMissed` | Contam seus acertos e erros. Se está errando muito, o robô pode se aproximar ou mudar a potência. | Métodos novos, fora dos outros. |
| `getX()` · `getY()` + `getBattleFieldWidth()` · `getBattleFieldHeight()` | Detectam proximidade da parede. O robô pode voltar pro centro antes de bater e inverter a `direcao`. | `onScannedRobot`. |
| `setMaxVelocity(...)` | Controla a velocidade: devagar pra rodear com precisão ou rápido pra escapar. | `run()` ou qualquer evento. |
| `getEnergy()` com mais de um nível | Hoje existe só um limite (`ENERGIA_CRITICA`). Dá pra criar outros níveis (energia boa, média e crítica) com comportamentos diferentes. | Nova constante no topo e regras em `onScannedRobot`. |
| `setColors(...)` | Pinta o robô. Só estética. | Início do `run()`. |

---

<a name="robo-evasivo"></a>
# 🔵 Robô Evasivo

> **Perfil:** prioriza sobreviver. Está sempre em movimento, mantém distância grande e atira pouco.

[⬆ Voltar ao início](#qual-robo-voce-escolheu) · [Ir para a tabela de referência](#tabela-de-referencia)

### Constantes e variável

| Nome | Valor inicial | Pra que serve |
|---|---|---|
| `DISTANCIA_MINIMA` | 220 | Abaixo disso o inimigo está perto demais e o robô foge. |
| `ENERGIA_CRITICA` | 15 | Está declarada, mas **ainda não é usada** em nenhuma regra. Está esperando você. |
| `direcao` (variável) | 1 | Guarda pra qual lado ele anda e gira: `1` ou `-1`. |

### Como o código funciona

**1. Preparação e movimento constante (`run()`)**

```java
setAdjustGunForRobotTurn(true);
setAdjustRadarForGunTurn(true);
```

Canhão e radar independentes do corpo.

```java
while (true) {
    setAhead(120 * direcao);
    setTurnRight(35 * direcao);
    setTurnRadarRight(360);
    execute();
}
```

Ele **nunca fica parado**: anda e gira o tempo todo, desenhando curvas, o que dificulta a vida de quem mira nele. Se `direcao` vira `-1`, ele inverte o lado.

**2. Quando vê um inimigo (`onScannedRobot`)**

| Trecho | O que faz |
|---|---|
| `anguloCanhao = normalizeBearing(...)` + `setTurnGunRight(...)` | Mira o canhão no inimigo. |
| `if (e.getDistance() < DISTANCIA_MINIMA)` | Inimigo perto demais: **inverte a direção**, dá ré e gira. |
| `if (getGunHeat() == 0 && Math.abs(anguloCanhao) < 15)` | Só atira com canhão frio e mirado. |
| `setFire(1.5)` ou `setFire(1)` | Tiros fracos: perto = 1.5, longe = 1. Gasta pouco e protege a energia. |

**3. Quando sofre algo**

| Evento | Reação do robô |
|---|---|
| `onHitByBullet` | Inverte a `direcao` e anda/gira pro novo lado pra errar o próximo tiro. |
| `onHitWall` | Inverte a `direcao`, dá ré e vira 90°. |

**4. `normalizeBearing`:** função ajudante pro canhão girar pelo caminho mais curto. Não precisa mexer.

### 🔧 Ajustes simples (mexa em um valor e teste)

- A distância mínima muda o quanto ele tolera de proximidade.
- A potência dos tiros decide se ele joga só pra sobreviver ou se arrisca mais pra causar dano.
- O tamanho do deslocamento (120) e o giro (35) mudam o desenho das curvas.
- A tolerância de mira (15) muda a frequência dos tiros.
- Os ângulos de reação a tiro e parede mudam a imprevisibilidade.
- A velocidade máxima pode ser limitada ou liberada.

### 🧱 Funções novas que podem ajudar

Aqui estão várias ideias de "blocos" pra encaixar no robô. **Você não precisa usar todas, escolha só as que fizerem sentido pra sua estratégia.** Cada uma diz o que faz e onde faz sentido usar. A sintaxe exata de cada comando está na [tabela de referência](#tabela-de-referencia).

| Função | Como funciona | Onde encaixar |
|---|---|---|
| Detectar o tiro do inimigo | Guarde numa variável a energia que o inimigo tinha na leitura anterior. Se a energia caiu um pouco (e não por outro motivo), ele provavelmente **acabou de atirar**: dá pra mudar de direção na hora. | Variável no topo e a comparação em `onScannedRobot`. |
| `getEnergy()` + `ENERGIA_CRITICA` | A constante já existe no topo e ainda ninguém usa. Quando a energia ficar abaixo dela, o robô pode parar de atirar e só fugir. | Dentro de `onScannedRobot`, na regra de tiro. |
| `Math.random()` | Sorteia quando inverter a `direcao`. O movimento deixa de ter padrão. | Dentro do laço do `run()`. |
| `getTime()` | Conta o tempo da rodada. Dá pra inverter a direção a cada intervalo de tempo, como um relógio de zigue-zague. | Dentro do laço do `run()`. |
| `getX()` · `getY()` + `getBattleFieldWidth()` · `getBattleFieldHeight()` | Detectam proximidade da parede. O robô vira pro centro antes de ficar encurralado. | Laço do `run()`. |
| `setMaxVelocity(...)` | Anda mais devagar normalmente e libera a velocidade total só em emergência (por exemplo, quando é atingido). | `run()` e `onHitByBullet`. |
| `e.getBearing()` dentro de `onHitByBullet` | Mostra de que lado veio a bala. O robô pode escapar de lado em relação a ela. | Dentro de `onHitByBullet`. |
| `e.getVelocity()` | Se o inimigo está parado, é alvo fácil e vale atirar. Se está correndo, talvez nem valha o gasto. | Dentro de `onScannedRobot`, na regra de tiro. |
| `e.getEnergy()` | Lê a energia do inimigo. Se está quase morrendo, um tiro bem pequeno resolve. | Dentro de `onScannedRobot`, na regra de tiro. |
| `onBulletMissed` | Avisa quando seu tiro erra. Se erra muito, o robô pode parar de atirar e poupar energia. | Método novo, fora dos outros. |
| `onBulletHit` | Avisa quando seu tiro acerta. Pode ser usado pra atirar mais enquanto está dando certo. | Método novo, fora dos outros. |
| `getOthers()` | Quantos inimigos restam. Quando sobra um só, o robô pode mudar de postura. | `onScannedRobot` ou `run()`. |
| `onHitRobot` | Reação ao esbarrar: afastar-se rápido e mudar de direção. | Método novo, fora dos outros. |
| `setColors(...)` | Pinta o robô. Só estética. | Início do `run()`. |

---

## ✅ Como testar o que você fez

1. Salve o arquivo e **compile** o robô no editor do Robocode.
2. Crie uma **batalha** com o seu robô e um dos outros robôs-base.
3. Rode várias rodadas (uma só pode ser sorte).
4. Mudou algo? Compare o resultado com a versão anterior. **Uma mudança por vez!**

Se der erro ao compilar, confira se: cada `{` tem seu `}`, cada linha termina com `;`, e o nome do comando está escrito exatamente igual ao do guia.

---

## Recursos

- Documentação oficial do Robocode: https://robocode.sourceforge.io/docs/robocode/
- Download do Robocode Classic: https://robocode.sourceforge.io/

<br>

<p align="center">
ROBOAP · UTFPR
</p>
