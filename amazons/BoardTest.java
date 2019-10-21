package amazons;

import org.junit.Test;

import static amazons.Piece.*;
import static amazons.Piece.SPEAR;
import static org.junit.Assert.*;
import static amazons.Utils.*;
import org.junit.Before;

/** Tests of the Model class.
 *  @author Vidhi Chander
 */
public class BoardTest {

    private Board b;

    @Before
    public void setupBoard() {
        b = new Board();
    }

    /**
     * Tests basic correctness of put and get on the initialized board.
     */
    @Test
    public void copyTest() {
        b.put(BLACK, Square.sq(3, 5));
        b.put(WHITE, Square.sq(9, 9));
        b.put(EMPTY, Square.sq(6, 9));
        b.put(EMPTY, Square.sq(9, 3));
        Board board1 = new Board(b);
        assertEquals(board1.get(3, 5), BLACK);
        assertEquals(board1.get(9, 9), WHITE);
        assertEquals(board1.get(6, 9), EMPTY);
        assertEquals(board1.get(9, 3), EMPTY);

        board1.put(EMPTY, Square.sq(3, 5));
        assertEquals(board1.get(3, 5), EMPTY);
    }


    @Test
    public void testBasicPutGet() {
        b.put(BLACK, Square.sq(3, 5));
        assertEquals(b.get(3, 5), BLACK);
        b.put(WHITE, Square.sq(9, 9));
        assertEquals(b.get(9, 9), WHITE);
        b.put(EMPTY, Square.sq(3, 5));
        assertEquals(b.get(3, 5), EMPTY);
    }

    @Test
    public void testToString() {
        assertEquals(INIT_BOARD_STATE, b.toString());
        makeSmile(b);
        assertEquals(SMILE, b.toString());
    }

    private void makeSmile(Board c) {
        b.put(EMPTY, Square.sq(0, 3));
        b.put(EMPTY, Square.sq(0, 6));
        b.put(EMPTY, Square.sq(9, 3));
        b.put(EMPTY, Square.sq(9, 6));
        b.put(EMPTY, Square.sq(3, 0));
        b.put(EMPTY, Square.sq(3, 9));
        b.put(EMPTY, Square.sq(6, 0));
        b.put(EMPTY, Square.sq(6, 9));
        for (int col = 1; col < 4; col += 1) {
            for (int row = 6; row < 9; row += 1) {
                b.put(SPEAR, Square.sq(col, row));
            }
        }
        b.put(EMPTY, Square.sq(2, 7));
        for (int col = 6; col < 9; col += 1) {
            for (int row = 6; row < 9; row += 1) {
                b.put(SPEAR, Square.sq(col, row));
            }
        }
        b.put(EMPTY, Square.sq(7, 7));
        for (int lip = 3; lip < 7; lip += 1) {
            b.put(WHITE, Square.sq(lip, 2));
        }
        b.put(WHITE, Square.sq(2, 3));
        b.put(WHITE, Square.sq(7, 3));
    }

    static final String INIT_BOARD_STATE =
            "   - - - B - - B - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   B - - - - - - - - B\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   W - - - - - - - - W\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - W - - W - - -\n";

    static final String SMILE =
            "   - - - - - - - - - -\n"
                    + "   - S S S - - S S S -\n"
                    + "   - S - S - - S - S -\n"
                    + "   - S S S - - S S S -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - W - - - - W - -\n"
                    + "   - - - W W W W - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n";

    @Test
    public void testisUnblockedMove() {
        b.put(WHITE, Square.sq("d7"));
        assertFalse(b.isUnblockedMove(Square.sq("d10"), Square.sq("d5"), null));
        assertTrue(b.isUnblockedMove(Square.sq("d1"), Square.sq("d5"), null));

        assertFalse(b.isUnblockedMove(Square.sq("j7"), Square.sq("c7"), null));
        assertTrue(b.isUnblockedMove(Square.sq("a7"), Square.sq("c7"), null));

        b.put(WHITE, Square.sq("d4"));
        assertFalse(b.isUnblockedMove(Square.sq("a7"), Square.sq("e3"), null));
        assertTrue(b.isUnblockedMove(Square.sq("a7"), Square.sq("c5"), null));

        assertFalse(b.isUnblockedMove(Square.sq("a4"), Square.sq("f9"), null));
        assertTrue(b.isUnblockedMove(Square.sq("d1"), Square.sq("g4"), null));

        b.put(WHITE, Square.sq("i8"));
        assertFalse(b.isUnblockedMove(Square.sq("j7"), Square.sq("h9"), null));
        assertTrue(b.isUnblockedMove(Square.sq("g1"), Square.sq("e3"), null));

        b.put(WHITE, Square.sq("g4"));
        assertFalse(b.isUnblockedMove(Square.sq("j7"), Square.sq("e2"), null));
        assertTrue(b.isUnblockedMove(Square.sq("d10"), Square.sq("b8"), null));

        b.put(EMPTY, Square.sq("d7"));
        b.put(EMPTY, Square.sq("d4"));
        assertTrue(b.isUnblockedMove(Square.sq("d7"),
                Square.sq("g7"), Square.sq("d1")));
        assertTrue(b.isUnblockedMove(Square.sq("e7"),
                Square.sq("i7"), Square.sq("g1")));
        assertTrue(b.isUnblockedMove(Square.sq("c9"),
                Square.sq("h4"), Square.sq("d10")));
        assertFalse(b.isUnblockedMove(Square.sq("d7"),
                Square.sq("i2"), Square.sq("j7")));

        b.put(WHITE, Square.sq("d7"));
        assertFalse(b.isUnblockedMove(Square.sq("a4"),
                Square.sq("a7"), Square.sq("g9")));
    }

    @Test
    public void testisLegal() {
        assertTrue(b.isLegal(Square.sq("a4"), Square.sq("d7")));

        b.put(WHITE, Square.sq("d7"));
        assertFalse(b.isLegal(Square.sq("d1"), Square.sq("d7")));
    }

    @Test
    public void testMakeMove() {
        b.makeMove(Square.sq("d1"), Square.sq("d7"), Square.sq("g7"));
        assertEquals(FIRST_MOVE_BOARD_STATE, b.toString());

        b.makeMove(Square.sq("d10"), Square.sq("c9"), Square.sq("h4"));
        assertEquals(SECOND_MOVE_BOARD_STATE, b.toString());

    }

    static final String FIRST_MOVE_BOARD_STATE =
            "   - - - B - - B - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   B - - W - - S - - B\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   W - - - - - - - - W\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - W - - -\n";

    static final String SECOND_MOVE_BOARD_STATE =
            "   - - - - - - B - - -\n"
                    + "   - - B - - - - - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   B - - W - - S - - B\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   W - - - - - - S - W\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - W - - -\n";

    @Test
    public void testUndo() {
        b.makeMove(Square.sq("d1"), Square.sq("d7"), Square.sq("g7"));
        b.makeMove(Square.sq("d10"), Square.sq("c9"), Square.sq("h4"));
        b.undo();
        assertEquals(FIRST_MOVE_BOARD_STATE, b.toString());

    }

}
