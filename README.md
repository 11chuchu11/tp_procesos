# tp_procesos


# TPO Final — eScrims
**Plataforma de organización de scrims y partidas amistosas de eSports**  
**533549 - 2025-2C - NOCHE - LUNES**

---

## 1. Objetivos del Sistema

- Facilitar que jugadores creen, encuentren y se unan a scrims en su región.
- Emparejar jugadores por rango/MMR, rol y latencia utilizando algoritmos intercambiables.
- Gestionar el ciclo de vida completo del scrim: creación, confirmaciones, inicio, finalización y estadísticas.
- Enviar notificaciones a través de múltiples canales (push, email, Discord/Slack).

---

## 2. Alcance

### ✅ Interfaces

- **App móvil** → Jugadores.
- **Panel web** → Organizadores / capitanes de equipo.

### ✅ Tipos de scrim

- Formatos: 1v1, 3v3, 5v5 u otros según el juego.
- Juegos soportados: Valorant, League of Legends, CS2, etc.

### ✅ Integraciones opcionales

- Autenticación OAuth (Steam, Riot, Discord).
- Bots de Discord.
- Proveedores de mensajería (Firebase, SendGrid).
- Email / notificaciones push.

---

## 3. Requerimientos Funcionales

### 3.1 Registro y autenticación de usuarios

- Alta con usuario, email y contraseña.
- Opcional OAuth (Steam, Riot, Discord).
- Perfiles editables:
    - Juego principal
    - Rango
    - Roles preferidos
    - Región/servidor
    - Disponibilidad horaria
- Estado de email: **Pendiente → Verificado**

---

### 3.2 Búsqueda de scrims

- Filtros por:
    - Juego
    - Formato (1v1, 3v3, 5v5)
    - Rango mínimo/máximo
    - Región / servidor
    - Ping máximo permitido
    - Fecha / hora
- Posibilidad de guardar filtros como favoritos.
- Alertas cuando aparece un scrim compatible (**Observer Pattern**).

---

### 3.3 Creación de scrim

Al crear un scrim, el usuario define:

| Parámetro | Descripción |
|-----------|-------------|
| Juego y formato | Ej: Valorant 5v5 |
| Cupos | Cantidad total de jugadores |
| Roles por equipo | Opcional: Duelist, Support, etc. |
| Región / servidor | Ej: LATAM-SUR |
| Rango permitido | Mínimo y máximo |
| Latencia máxima | Ping en ms |
| Fecha y duración | Fecha/hora + duración estimada |
| Estado inicial | **“Buscando jugadores”** |

---

### 3.4 Estados del scrim (Patrón State)

| Estado | Descripción | Transición a |
|--------|-------------|--------------|
| Buscando jugadores | Faltan participantes | Lobby Armado |
| Lobby Armado | Cupos completos, esperando confirmaciones | Confirmado |
| Confirmado | Todos confirmaron, esperando hora | En juego |
| En juego | Scrim iniciado | Finalizado |
| Finalizado | Se cargan estadísticas | — |
| Cancelado | Cancelado antes de iniciar | — |

Transiciones automáticas posibles (scheduler: tiempo/cron).

---

### 3.5 Estrategias de emparejamiento (Patrón Strategy)

- Implementaciones intercambiables:
    - **Por MMR/rango**
    - **Por latencia / ping**
    - **Por historial / sinergia / fair play**
- Configurable por scrim (al crearse o editarse).

### 3.6 Gestión de equipos y roles

- El organizador puede:
    - Asignar roles a los jugadores.
    - Intercambiar roles entre jugadores (swap).
    - Definir suplentes o lista de espera.
- Si un jugador abandona antes de iniciar:
    - Se notifica automáticamente a los suplentes.

---

### 3.7 Notificaciones (Observer + Abstract Factory + Adapter)

**Eventos que generan notificaciones:**
| Evento | Descripción |
|--------|-------------|
| Scrim creado | Coincide con preferencias de un usuario |
| Lobby armado | Se llenan los cupos |
| Scrim confirmado | Todos los jugadores aceptan |
| Cambio de estado | En juego / Finalizado / Cancelado |

**Canales de envío:**
- Notificaciones Push (Firebase)
- Email (SendGrid, JavaMail)
- Discord / Slack (webhooks / bots)

**Patrones utilizados:**
- **Observer:** Publicación de eventos del dominio.
- **Abstract Factory:** Creación de notifiers según canal y entorno (dev/prod).
- **Adapter:** Integración con APIs externas (Discord, Email, iCal).

---

### 3.8 Estadísticas y feedback del scrim

- Al finalizar el scrim:
    - Se registra resultado y estadísticas de los jugadores (kills, assists, MVP, etc.).
    - Los usuarios pueden calificar su experiencia.
- Comentarios sujetos a moderación:
    - Estados: Pendiente / Aprobado / Rechazado.

---

### 3.9 Moderación y penalidades

- Registro de abandono o no-show.
- Sistema de **strikes** y **cooldowns**.
- Reportes de conducta inapropiada:
    - Procesados con **Chain of Responsibility**:
        - Auto-resolución
        - Bot moderador
        - Moderador humano

---

### 3.10 Calendario y recordatorios

- Envío automático de recordatorios antes del scrim.
- Compatibilidad con calendarios externos (**Adapter iCal**).
- Recordatorios configurables (ej: 1h antes, 24h antes).

---

### 3.11 Multijuego y multirregión

- Cada scrim pertenece a un juego y región específica.
- Las reglas de emparejamiento pueden variar según:
    - Juego
    - Modo de juego
    - Servidor/región

---

## 4. Requerimientos No Funcionales

| Categoría | Descripción |
|-----------|-------------|
| Arquitectura | Basada en MVC. Capa de dominio separada. |
| Patrones | Mínimo 4 obligatorios: State, Strategy, Observer, Abstract Factory. |
| Persistencia | Uso de ORM/JPA o similar. |
| Escalabilidad | Colas de mensajes para notificaciones (RabbitMQ / Kafka o simulado). |
| Disponibilidad | Reintentos automáticos ante fallos de proveedores (exponential backoff). |
| Seguridad | Hashing de contraseñas, roles (USER, MOD, ADMIN), rate limiting básico. |
| Rendimiento | Emparejamiento debe ejecutarse en < 2s con 500 candidatos. |
| Trazabilidad | Logging de auditoría para cambios de estado, penalidades, etc. |
| Testing | Tests unitarios, de integración y de estados del scrim. |
## 5. Patrones de Diseño (Mapa sugerido)

| Patrón | Aplicación en el sistema |
|--------|----------------------------|
| **State** | Ciclo de vida del Scrim (Buscando, LobbyArmado, Confirmado, EnJuego, Finalizado, Cancelado). |
| **Strategy** | Algoritmos de emparejamiento (MMR, latencia, historial/compatibilidad). |
| **Observer** | Suscripción a eventos del dominio → envío de notificaciones automáticas. |
| **Abstract Factory** | Creación de canales de notificación (Push, Email, Discord) dependiendo del entorno (dev/prod). |
| **Command** (opcional) | Asignar roles, invitar jugadores, intercambiar roles (con posibilidad de undo antes de confirmar). |
| **Builder** (opcional) | Construcción gradual y validada de un Scrim. |
| **Adapter** (opcional) | Integración con APIs externas (Discord, Email, iCal). |
| **Chain of Responsibility** (opcional) | Flujo escalonado de resolución de reportes de conducta. |
| **Template Method** (opcional) | Validación por juego: reglas diferentes según el título (Valorant, LoL, CS2). |

---

## 6. Modelo de Dominio (Sugerido)

### ✅ Entidades principales

| Entidad | Atributos clave |
|----------|------------------|
| **Usuario** | id, username, email, passwordHash, rangoPorJuego, rolesPreferidos, region, preferencias |
| **Scrim** | id, juego, formato, region, rangoMin, rangoMax, latenciaMax, fechaHora, duracion, estado, cupos, reglasRoles |
| **Equipo** | id, lado (A/B), jugadores[] |
| **Postulacion** | id, usuario, scrim, rolDeseado, estado (Pendiente/Aceptada/Rechazada) |
| **Confirmacion** | id, usuario, scrim, confirmado (bool) |
| **Notificacion** | id, tipo, canal, payload, estado |
| **Estadistica** | id, scrim, usuario, mvp, kda, observaciones |
| **ReporteConducta** | id, scrim, reportado, motivo, estado, sancion |

---

## 7. Casos de Uso (Resumen)

| Código | Nombre |
|--------|--------|
| CU1 | Registrar usuario |
| CU2 | Autenticar usuario |
| CU3 | Crear scrim |
| CU4 | Postularse a scrim |
| CU5 | Emparejar y armar lobby |
| CU6 | Confirmar participación |
| CU7 | Iniciar scrim (scheduler) |
| CU8 | Finalizar y cargar estadísticas |
| CU9 | Cancelar scrim |
| CU10 | Notificar eventos |
| CU11 | Moderar reportes de conducta |

Cada caso de uso debe incluir:
- Actores
- Precondiciones
- Flujo principal
- Flujos alternativos
- Reglas de negocio
- Postcondiciones

---

## 8. Diagrama de Estados (Texto)


Buscando ── (cupo completo) ──> LobbyArmado
LobbyArmado ── (todos confirman) ──> Confirmado
Confirmado ── (llega fechaHora) ──> EnJuego
EnJuego ── (fin manual o por cron) ──> Finalizado

Cualquier estado antes de EnJuego ── (cancelar) ──> Cancelado

## 9. Diagrama de Clases UML (Guía de componentes)

- **State**
  - `ScrimContext` (contexto)
  - `ScrimState` (interface)
  - Concreciones: `BuscandoState`, `LobbyArmadoState`, `ConfirmadoState`, `EnJuegoState`, `FinalizadoState`, `CanceladoState`

- **Strategy**
  - `MatchmakingStrategy` (interface)
  - Estrategias: `ByMMRStrategy`, `ByLatencyStrategy`, `ByHistoryStrategy`

- **Observer**
  - `DomainEventBus` (Subject)
  - `Subscriber` (Observer)
  - Notifiers concretos: `PushNotifier`, `EmailNotifier`, `DiscordNotifier`

- **Abstract Factory**
  - `NotifierFactory` → crea notifiers según canal/entorno (dev/prod)
  - Implementaciones: `DevNotifierFactory`, `ProdNotifierFactory`

- **Command**
  - `ScrimCommand` (interface)
  - Comandos: `AsignarRolCommand`, `InvitarJugadorCommand`, `SwapJugadoresCommand`

- **Builder**
  - `ScrimBuilder` (validaciones encadenadas)

- **Adapter**
  - `DiscordAdapter`, `SendGridAdapter`, `ICalAdapter`

---

## 10. API (Sugerida)

- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET  /api/scrims?juego=&region=&rangoMin=&rangoMax=&fecha=&latenciaMax=`
- `POST /api/scrims` (crear)
- `POST /api/scrims/{id}/postulaciones` (postularse)
- `POST /api/scrims/{id}/confirmaciones` (confirmar)
- `POST /api/scrims/{id}/acciones/{command}` (Command pattern)
- `POST /api/scrims/{id}/cancelar`
- `POST /api/scrims/{id}/finalizar`
- `POST /api/scrims/{id}/estadisticas`

---
## 11. Esqueleto de Código (Java)

A continuación, se presentan los fragmentos de código sugeridos para implementar los patrones de diseño requeridos en el sistema eScrims. Estos ejemplos no representan la implementación final, sino una guía base para estructurar el proyecto con buenas prácticas y bajo acoplamiento.

---

### 🧩 11.1 Strategy – Estrategias de Emparejamiento

```java
public interface MatchmakingStrategy {
    List<Usuario> seleccionar(List<Usuario> candidatos, Scrim scrim);
}

public class ByMMRStrategy implements MatchmakingStrategy {
    @Override
    public List<Usuario> seleccionar(List<Usuario> candidatos, Scrim scrim) {
        // Lógica por MMR
        return List.of();
    }
}

public class ByLatencyStrategy implements MatchmakingStrategy {
    @Override
    public List<Usuario> seleccionar(List<Usuario> candidatos, Scrim scrim) {
        // Lógica por latencia
        return List.of();
    }
}

public class ByHistoryStrategy implements MatchmakingStrategy {
    @Override
    public List<Usuario> seleccionar(List<Usuario> candidatos, Scrim scrim) {
        // Lógica basada en historial o fair-play
        return List.of();
    }
}
```
### 🧩 11.2 State – Ciclo de Vida del Scrim

```java 
    public interface ScrimState {
    void postular(ScrimContext ctx, Usuario u, Rol rol);
    void confirmar(ScrimContext ctx, Usuario u);
    void iniciar(ScrimContext ctx);
    void finalizar(ScrimContext ctx);
    void cancelar(ScrimContext ctx);
}

public class ScrimContext {
    private ScrimState state;

    public void setState(ScrimState s) { this.state = s; }

    public void postular(Usuario u, Rol r) { state.postular(this, u, r); }
    public void confirmar(Usuario u) { state.confirmar(this, u); }
    public void iniciar() { state.iniciar(this); }
    public void finalizar() { state.finalizar(this); }
    public void cancelar() { state.cancelar(this); }
}
```

### 🧩 11.3 Observer – Publicación de Eventos y Notificaciones

```java
public interface DomainEvent {}

public record ScrimStateChanged(UUID scrimId, String nuevoEstado) implements DomainEvent {}

public interface Subscriber {
    void onEvent(DomainEvent e);
}

public class DomainEventBus {
    private final List<Subscriber> subs = new ArrayList<>();

    public void subscribe(Subscriber s) { subs.add(s); }
    public void publish(DomainEvent e) { subs.forEach(s -> s.onEvent(e)); }
}
```

### 🧩 11.4 Abstract Factory – Notificadores por Canal
```java 
public interface Notifier {
    void send(Notificacion n);
}

public interface NotifierFactory {
    Notifier createPush();
    Notifier createEmail();
    Notifier createChat(); // Discord / Slack
}

public class DevNotifierFactory implements NotifierFactory {
    public Notifier createPush() { return n -> System.out.println("Push DEV: " + n); }
    public Notifier createEmail() { return n -> System.out.println("Email DEV: " + n); }
    public Notifier createChat() { return n -> System.out.println("Chat DEV: " + n); }
}

public class ProdNotifierFactory implements NotifierFactory {
    public Notifier createPush() { return new FirebasePushNotifier(); }
    public Notifier createEmail() { return new SendGridNotifier(); }
    public Notifier createChat() { return new DiscordNotifier(); }
}
```
### 🧩 11.5 Builder – Construcción de un Scrim

```java
public class ScrimBuilder {
    private Scrim s = new Scrim();

    public ScrimBuilder juego(String j) { s.setJuego(j); return this; }
    public ScrimBuilder formato(String f) { s.setFormato(f); return this; }
    public ScrimBuilder rango(int min, int max) {
        s.setRangoMin(min);
        s.setRangoMax(max);
        return this;
    }
    public ScrimBuilder fecha(LocalDateTime dt) { s.setFechaHora(dt); return this; }

    public Scrim build() {
        // Validaciones antes de crear el Scrim
        return s;
    }
}
```
 ### 🧩 11.6 Command – Acciones dentro del Scrim (Asignar Roles, Swap, etc.)

```java
public interface ScrimCommand {
    void execute(ScrimContext ctx);
    void undo(ScrimContext ctx);
}

public class AsignarRolCommand implements ScrimCommand {
    private Usuario usuario;
    private Rol rolPrevio;
    private Rol rolNuevo;

    public AsignarRolCommand(Usuario usuario, Rol nuevo) {
        this.usuario = usuario;
        this.rolNuevo = nuevo;
    }

    @Override
    public void execute(ScrimContext ctx) {
        // lógica para asignar rolNuevo
    }

    @Override
    public void undo(ScrimContext ctx) {
        // restablecer rolPrevio
    }
}
```
## 12. Historias de Usuario

### 📌 Historias de Usuario (HU)

- **HU1**:  
  *Como jugador, quiero buscar scrims por rango y región para unirme a partidas con buen ping.*

- **HU2**:  
  *Como organizador, quiero crear un scrim 5v5 con límites de rango para equilibrar el lobby.*

- **HU3**:  
  *Como participante, quiero recibir notificaciones cuando el lobby se complete para no perder mi lugar.*

- **HU4**:  
  *Como moderador, quiero procesar reportes de conducta con un flujo escalonado para mantener el orden en la plataforma.*

---

### ✅ Criterios de Aceptación

| Caso | Dado (Given) | Cuando (When) | Entonces (Then) |
|------|--------------|---------------|------------------|
| CA1 | Un scrim con rango permitido **[Gold–Plat]** | Un jugador **Silver** se postula | El sistema **rechaza la postulación automáticamente** |
| CA2 | Un scrim con latencia máxima **80 ms** | Un jugador tiene latencia promedio de **120 ms** | El sistema **no lo admite** según la estrategia de latencia |
| CA3 | Lobby **completo** | Todos los jugadores confirman su participación | El estado del scrim pasa a **Confirmado** y se envía una **notificación a todos** |
| CA4 | Scrim confirmado | Llega la fecha y hora programada del scrim | El estado cambia a **"En Juego" automáticamente** |
| CA5 | Scrim en estado "Confirmado" | El organizador decide cancelar antes de iniciar | El sistema lo marca como **"Cancelado"** y notifica a todos los jugadores |
| CA6 | Scrim finaliza correctamente | El organizador carga estadísticas | Se habilita la **carga de KDA, MVP, comentarios y feedback** |

---

### 🛠 Relación con Patrones de Diseño

| Historia | Patrones involucrados |
|----------|-------------------------|
| HU1 | Strategy (emparejamiento), Observer (alertas de scrims nuevos) |
| HU2 | Builder (creación de scrim), Factory |
| HU3 | Observer (notificaciones), Abstract Factory (canales de envío) |
| HU4 | Chain of Responsibility (moderación de reportes) |
| CA3 y CA4 | State (transición: LobbyArmado → Confirmado → EnJuego) |


## 13. Plan de Pruebas (Resumen)

- **Unitarias**:
    - `ByMMRStrategyTest`
    - `ScrimStateTransitionsTest`
    - `NotifierFactoryTest`
- **Integración**:
    - Flujo `crear → postular → armar lobby → confirmar → iniciar → finalizar`
- **E2E**:
    - Desde UI móvil: búsqueda, postulación, confirmación
- **Carga**:
    - Emparejamiento con **500 candidatos** en **< 2 s**

---

## 14. Entregables

1. **Diagrama de clases UML** (con estereotipos de patrones).
2. **Diagrama de estados** del scrim.
3. **Modelo de dominio** y **casos de uso** documentados.
4. **Código fuente** (mínimo capas: Controller–Service–Domain–Infra) y **README**.
5. **Suite de tests** y evidencias (reportes).
6. **Video demo** (≤ 5 min) mostrando patrones en ejecución.

---

## 15. Rúbrica de Evaluación (Sugerida)

- Correctitud del modelo y justificación de patrones — **10%**
- Calidad del diseño UML y trazabilidad a código — **10%**
- Completitud del ciclo de vida (estados y transiciones) — **10%**
- Notificaciones y desacoplo con Abstract Factory/Adapter — **10%**
- Tests y calidad de código — **10%**
- Documentación y demo (funcionalidad) — **10%**
- **Presentación oral** — **40%**

---

## 16. Extensiones Opcionales (Bonus)

- **Matchmaking híbrido** (ponderar MMR + latencia + historial).
- **Rank decay** y **recalculo de MMR** por desempeño en scrims.
- **Colas** para notificaciones y **retries**.
- Sistema de **reputación** con antifraude (detección básica de **smurfing**).

---

