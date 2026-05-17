package de.hsbi.lockgame.logic;

import de.hsbi.lockgame.model.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameStateTest {

    @Test
    void tickMovesSnake() {

        CellType[][] cells = {
            {CellType.EMPTY, CellType.EMPTY},
            {CellType.EMPTY, CellType.EMPTY}
        };

        Level level =
            new Level(
                2,
                2,
                cells,
                List.of(),
                new Position(0, 0));

        Snake snake =
            new Snake(List.of(new Position(0, 0)));

        GameState state =
            new GameState(
                level,
                snake,
                List.of(),
                GameState.Status.RUNNING,
                Direction.RIGHT);

        GameState next = state.tick();

        assertEquals(1, next.snake().head().x());
        assertEquals(0, next.snake().head().y());
    }

    @Test
    void wallBlocksMovement() {

        CellType[][] cells = {
            {CellType.EMPTY},
            {CellType.WALL}
        };

        Level level =
            new Level(
                2,
                1,
                cells,
                List.of(),
                new Position(0, 0));

        Snake snake =
            new Snake(List.of(new Position(0, 0)));

        GameState state =
            new GameState(
                level,
                snake,
                List.of(),
                GameState.Status.RUNNING,
                Direction.DOWN);

        GameState next = state.tick();

        assertEquals(0, next.snake().head().x());
        assertEquals(0, next.snake().head().y());
    }

    @Test
    void outOfBoundsLosesGame() {

        CellType[][] cells = {
            {CellType.EMPTY}
        };

        Level level =
            new Level(
                1,
                1,
                cells,
                List.of(),
                new Position(0, 0));

        Snake snake =
            new Snake(List.of(new Position(0, 0)));

        GameState state =
            new GameState(
                level,
                snake,
                List.of(),
                GameState.Status.RUNNING,
                Direction.RIGHT);

        GameState next = state.tick();

        assertEquals(
            GameState.Status.LOST_OUT_OF_BOUNDS,
            next.status());
    }

    @Test
    void selfCollisionLosesGame() {

        CellType[][] cells = {
            {CellType.EMPTY, CellType.EMPTY},
            {CellType.EMPTY, CellType.EMPTY}
        };

        Snake snake =
            new Snake(List.of(
                new Position(1, 0),
                new Position(0, 0),
                new Position(0, 1),
                new Position(1, 1)
            ));

        Level level =
            new Level(
                2,
                2,
                cells,
                List.of(),
                new Position(1, 0));

        GameState state =
            new GameState(
                level,
                snake,
                List.of(),
                GameState.Status.RUNNING,
                Direction.DOWN);

        GameState next = state.tick();

        assertEquals(
            GameState.Status.LOST_SELF_COLLISION,
            next.status());
    }

    @Test
    void wrongPinDirectionBlocks() {

        CellType[][] cells = {
            {CellType.EMPTY},
            {CellType.PIN_SLOT}
        };

        Pin pin =
            new Pin(
                new Position(0, 1),
                Pin.State.LOW,
                Direction.LEFT);

        Level level =
            new Level(
                2,
                1,
                cells,
                List.of(pin),
                new Position(0, 0));

        Snake snake =
            new Snake(List.of(new Position(0, 0)));

        GameState state =
            new GameState(
                level,
                snake,
                List.of(pin),
                GameState.Status.RUNNING,
                Direction.DOWN);

        GameState next = state.tick();

        assertEquals(0, next.snake().head().x());
        assertEquals(0, next.snake().head().y());
    }

    @Test
    void correctPinDirectionActivatesPin() {

        CellType[][] cells = {
            {CellType.EMPTY, CellType.PIN_SLOT}
        };

        Pin pin =
            new Pin(
                new Position(0, 1),
                Pin.State.LOW,
                Direction.DOWN);

        Level level =
            new Level(
                1,
                2,
                cells,
                List.of(pin),
                new Position(0, 0));

        Snake snake =
            new Snake(List.of(new Position(0, 0)));

        GameState state =
            new GameState(
                level,
                snake,
                List.of(pin),
                GameState.Status.RUNNING,
                Direction.DOWN);

        GameState next = state.tick();

        assertTrue(next.pins().getFirst().state().isSet());
    }
    @Test
    void highPinBlocksMovement() {

        CellType[][] cells = {
            {CellType.EMPTY},
            {CellType.PIN_SLOT}
        };

        Pin pin =
            new Pin(
                new Position(0, 1),
                Pin.State.HIGH,
                Direction.DOWN);

        Level level =
            new Level(
                2,
                1,
                cells,
                List.of(pin),
                new Position(0, 0));

        Snake snake =
            new Snake(List.of(new Position(0, 0)));

        GameState state =
            new GameState(
                level,
                snake,
                List.of(pin),
                GameState.Status.RUNNING,
                Direction.DOWN);

        GameState next = state.tick();

        assertEquals(0, next.snake().head().x());
        assertEquals(0, next.snake().head().y());
    }

    @Test
    void winningLastPinWinsGame() {

        CellType[][] cells = {
            {CellType.EMPTY, CellType.PIN_SLOT}
        };
        Pin pin =
            new Pin(
                new Position(0, 1),
                Pin.State.LOW,
                Direction.DOWN);

        Level level =
            new Level(
                1,
                2,
                cells,
                List.of(pin),
                new Position(0, 0));

        Snake snake =
            new Snake(List.of(new Position(0, 0)));

        GameState state =
            new GameState(
                level,
                snake,
                List.of(pin),
                GameState.Status.RUNNING,
                Direction.DOWN);

        GameState next = state.tick();

        assertEquals(GameState.Status.WON, next.status());
    }

    @Test
    void noneDirectionDoesNothing() {

        CellType[][] cells = {
            {CellType.EMPTY}
        };

        Level level =
            new Level(
                1,
                1,
                cells,
                List.of(),
                new Position(0, 0));

        Snake snake =
            new Snake(List.of(new Position(0, 0)));

        GameState state =
            new GameState(
                level,
                snake,
                List.of(),
                GameState.Status.RUNNING,
                Direction.NONE);

        GameState next = state.tick();

        assertEquals(0, next.snake().head().x());
        assertEquals(0, next.snake().head().y());
    }

    @Test
    void nonRunningStateDoesNothing() {

        CellType[][] cells = {
            {CellType.EMPTY}
        };

        Level level =
            new Level(
                1,
                1,
                cells,
                List.of(),
                new Position(0, 0));

        Snake snake =
            new Snake(List.of(new Position(0, 0)));

        GameState state =
            new GameState(
                level,
                snake,
                List.of(),
                GameState.Status.WON,
                Direction.RIGHT);

        GameState next = state.tick();

        assertEquals(GameState.Status.WON, next.status());
    }
}
