package de.hsbi.lockgame.logic;
import de.hsbi.lockgame.model.*;
import de.hsbi.lockgame.ui.GamePanel;
import java.util.List;

// TODO: Die GameEngine verwaltet den GameState.

// TODO: Die GameEngine wird durch den Timer im main() getriggert ("tick") und lässt den GameState
// daraufhin einen Schritt ausführen. Dann müssen alle für den GameState registrierten Observer
// benachrichtigt werden, damit das Spielfeld neu gezeichnet werden kann o.ä.

// TODO: Die GameEngine beobachtet die Tastatureingaben (gesetzt in GamePanel.setupKeyBindings()),
// die in Direction übersetzt und an GameEngine.update() übergeben werden. Wenn es eine neue Eingabe
// gibt, wird die "update"-Methode von GameEngine aufgerufen, und die GameEngine muss die
// Blickrichtung der Schlange aktualisieren und diese GameState-Änderung den für den GameState
// registrierten Observer mitteilen.

// TODO: Die GameEngine ist ein Observer für Direction: GameEngine.update(Direction)
// TODO: Die GameEngine ist ein Observable für GameState: GamePanel.update(GameState)

    public final class GameEngine {
        private GameState state;
        private GamePanel panel;


  public GameEngine(Level level) {
    // TODO: lege eine neue GameEngine mit den übergebenen Informationen an
      Snake snake = new Snake(List.of(level.snakeStart()));

      this.state =
          new GameState(
              level,
              snake,
              level.pins(),
              GameState.Status.RUNNING,
              Direction.NONE);
  }


  public GameState state() {
    // TODO: gebe den aktuellen Spielzustand zurück
    return state;
  }

  public void setGamePanel(GamePanel panel) {
    // TODO: Setter
    this.panel = panel;
  }

  public void update(Direction d) {
    // TODO: aktualisiere den Blickwinkel der Schlange (GameState)
    // TODO: benachrichtige alle Observer und gibt den neuen Spielzustand mit (Neuzeichnen der
    // Spielfläche)
      state =
          new GameState(
              state.level(),
              state.snake(),
              state.pins(),
              state.status(),
              d);

      panel.update(state);
  }

  public void tick() {
    // TODO: lass das Spiel (den GameState) einen Schritt ("tick") machen
    // TODO: benachrichtige alle Observer und gibt den neuen Spielzustand mit (Neuzeichnen der
    // Spielfläche)

      state = state.tick();

      panel.update(state);
  }
}
