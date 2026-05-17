package de.hsbi.lockgame.logic;
import de.hsbi.lockgame.model.*;
import java.util.ArrayList;
import java.util.List;

public final class GameState {

    private final Level level;
    private final Snake snake;
    private final List<Pin> pins;
    private final Status status;
    private final Direction pendingDirection;

    public GameState(
      Level level, Snake snake, List<Pin> pins, Status status, Direction pendingDirection) {
    // TODO: lege einen neuen GameState mit den übergebenen Informationen an
        this.level = level;
        this.snake = snake;
        this.pins = pins;
        this.status = status;
        this.pendingDirection = pendingDirection;
  }

  public Level level() {
    // TODO: Getter
    return level;
  }

  public Snake snake() {
    // TODO: Getter
   return snake;
  }

  public List<Pin> pins() {
    // TODO: Getter
    return pins;
  }

  public Status status() {
    // TODO: Getter
   return status;
  }

  public Direction pendingDirection() {
    // TODO: Getter
    return pendingDirection;
  }

  public GameState tick() {
    // TODO: diese Methode lässt das Spiel einen Schritt laufen (berechnet den Spielzustand im
    // nächsten Schritt)

    // TODO: early exit: wenn das Spiel nicht läuft oder keine Blickrichtung gesetzt ist: keine
    // Änderung

    // TODO: prüfe die folgenden Bedingungen:
    // (a) Schlange würde das Spielfeld verlassen: Spiel verloren
    // (b) Schlange würde in ein Wandelement gehen: Blockiert (keine Bewegung, Blickrichtung "none")
    // (c) Schlange beisst sich: Spiel verloren
    // (d) Schlange würde auf einen Pin gehen (Pin bereits gesetzt oder Schlange kommt nicht in der
    // Aktivierungsrichtung): Blockiert (keine Bewegung, Blickrichtung "none")

    // TODO: aktiviere einen noch nicht gesetzten Pin, wenn die Schlange in der richtigen Richtung
    // auf den Pin gehen würde (die Schlange darf dabei aber nicht auf den Pin gehen)

    // TODO: anderenfalls: bewege die Schlange um einen Schritt in Blickrichtung (falls gesetzt)
      if (!status.isRunning() || pendingDirection == Direction.NONE) {
          return this;
      }

      Position next = snake.nextHead(pendingDirection);

if (!level.isInside(next)) {

        return new GameState(
            level,
            snake,
            pins,
            Status.LOST_OUT_OF_BOUNDS,
            pendingDirection);
    }
      if (level.cellAt(next) == CellType.WALL) {
          return this;
      }
      boolean selfCollision =
          snake.body().stream().anyMatch(
              bodyPart ->
                  bodyPart.x() == next.x()
                      && bodyPart.y() == next.y());
      snake.body().forEach(System.out::println);

      if (selfCollision) {

          return new GameState(
              level,
              snake,
              pins,
              Status.LOST_SELF_COLLISION,
              pendingDirection);
      }

      for (Pin pin : pins) {


          if (pin.position().x() == next.x()
              && pin.position().y() == next.y()) {


              if (pin.state().isSet()) {
                  return this;
              }


              if (pin.activationDirection() != pendingDirection) {
                  return this;
              }


              Pin updatedPin = pin.withState(Pin.State.HIGH);

              List<Pin> updatedPins = new ArrayList<>(pins);

              updatedPins.set(updatedPins.indexOf(pin), updatedPin);


              boolean allPinsSet =
                  updatedPins.stream().allMatch(
                      p -> p.state().isSet());

              return new GameState(
                  level,
                  snake,
                  updatedPins,
                  allPinsSet ? Status.WON : Status.RUNNING,
                  pendingDirection);
          }
      }


      Snake newSnake = snake.grow(pendingDirection);

      return new GameState(
          level,
          newSnake,
          pins,
          Status.RUNNING,
          pendingDirection);
  }

  public enum Status {
    RUNNING,
    WON,
    LOST_SELF_COLLISION,
    LOST_OUT_OF_BOUNDS;

    public boolean isRunning() {
      return this == RUNNING;
    }
  }
}
