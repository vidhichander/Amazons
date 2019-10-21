package amazons;
import org.junit.Test;
import static org.junit.Assert.*;
import ucb.junit.textui;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/** Junit tests for our Board iterators.
 *  @author Vidhi Chander
 */
public class IteratorTests {

    /** Run the JUnit tests in this package. */
    public static void main(String[] ignored) {
        textui.runClasses(IteratorTests.class);
    }

    /** Tests reachableFromIterator to make sure it returns all reachable
     *  Squares. This method may need to be changed based on
     *   your implementation. */
    @Test
    public void testReachableFrom() {
        Board b = new Board();
        buildBoard(b, REACHABLEFROMTESTBOARD);
        int numSquares = 0;
        Set<Square> squares = new HashSet<>();
        Iterator<Square> reachableFrom = b.reachableFrom(Square.sq(5, 4), null);
        while (reachableFrom.hasNext()) {
            Square s = reachableFrom.next();
            assertTrue(REACHABLEFROMTESTSQUARES.contains(s) || s == null);
            if (s != null) {
                numSquares += 1;
                squares.add(s);
            }
        }
        assertEquals(REACHABLEFROMTESTSQUARES.size(), numSquares);
        assertEquals(REACHABLEFROMTESTSQUARES.size(), squares.size());

        Iterator<Square> reachableFrom1 = b.reachableFrom
                (Square.sq(5, 3), Square.sq(5, 4));
        numSquares = 0;
        Set<Square> squares1 = new HashSet<>();
        while (reachableFrom1.hasNext()) {
            Square s = reachableFrom1.next();
            assertTrue(REACHABLEFROMTESTSQUARES1.contains(s) || s == null);
            if (s != null) {
                numSquares += 1;
                squares1.add(s);
            }
        }
        assertEquals(REACHABLEFROMTESTSQUARES1.size(), numSquares);
        assertEquals(REACHABLEFROMTESTSQUARES1.size(), squares1.size());
        Board c = new Board();
        buildBoard(c, REACHABLEFROMTESTBOARD);
        assertEquals(b.toString(), c.toString());

    }

    /** Tests legalMovesIterator to make sure it returns all legal Moves.
     *  This method needs to be finished and may need to be changed
     *  based on your implementation. */
    @Test
    public void testLegalMoves() {
        Board b = new Board();
        buildBoard(b, LEGALTESTBOARD);
        int numMoves = 0;
        Set<Move> moves = new HashSet<>();
        Iterator<Move> legalMoves = b.legalMoves(Piece.WHITE);
        while (legalMoves.hasNext()) {
            Move m = legalMoves.next();
            assertTrue(LEGALMOVESTEST.contains(m) || m == null);
            if (m != null) {
                numMoves += 1;
                moves.add(m);
            }
        }
        assertEquals(LEGALMOVESTEST.size(), numMoves);
        assertEquals(LEGALMOVESTEST.size(), moves.size());

        b = new Board();
        numMoves = 0;
        moves = new HashSet<>();
        legalMoves = b.legalMoves(Piece.BLACK);
        while (legalMoves.hasNext()) {
            Move m = legalMoves.next();
            if (m != null) {
                numMoves += 1;
                moves.add(m);
            }
        }
        assertEquals(2176, numMoves);
    }


    private void buildBoard(Board b, Piece[][] target) {
        for (int col = 0; col < Board.SIZE; col++) {
            for (int row = Board.SIZE - 1; row >= 0; row--) {
                Piece piece = target[Board.SIZE - row - 1][col];
                b.put(piece, Square.sq(col, row));
            }
        }
        System.out.println(b);
    }

    static final Piece E = Piece.EMPTY;

    static final Piece W = Piece.WHITE;

    static final Piece B = Piece.BLACK;

    static final Piece S = Piece.SPEAR;

    static final Piece[][] REACHABLEFROMTESTBOARD = {
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, W, W },
            { E, E, E, E, E, E, E, S, E, S },
            { E, E, E, S, S, S, S, E, E, S },
            { E, E, E, S, E, E, E, E, B, E },
            { E, E, E, S, E, W, E, E, B, E },
            { E, E, E, S, S, S, B, W, B, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
    };

    static final Piece[][] LEGALTESTBOARD = {
            { S, S, S, S, E, E, E, B, S, S },
            { S, S, S, E, E, E, E, S, S, W },
            { W, S, S, E, S, S, S, S, S, S },
            { E, S, S, E, S, B, S, E, S, S },
            { E, S, S, W, E, E, S, S, S, S },
            { S, S, E, S, S, S, E, S, S, B },
            { B, S, E, E, E, E, E, S, S, W },
            { E, E, E, E, E, E, E, S, S, S },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
    };

    static final Set<Square> REACHABLEFROMTESTSQUARES =
            new HashSet<>(Arrays.asList(
                    Square.sq(5, 5),
                    Square.sq(4, 5),
                    Square.sq(4, 4),
                    Square.sq(6, 4),
                    Square.sq(7, 4),
                    Square.sq(6, 5),
                    Square.sq(7, 6),
                    Square.sq(8, 7)));

    static final Set<Square> REACHABLEFROMTESTSQUARES1 =
            new HashSet<>(Arrays.asList(
                    Square.sq(5, 4),
                    Square.sq(5, 5),
                    Square.sq(6, 4),
                    Square.sq(7, 5),
                    Square.sq(8, 6),
                    Square.sq(5, 2),
                    Square.sq(5, 1),
                    Square.sq(5, 0),
                    Square.sq(6, 2),
                    Square.sq(7, 1),
                    Square.sq(8, 0),
                    Square.sq(4, 2),
                    Square.sq(3, 1),
                    Square.sq(2, 0),
                    Square.sq(4, 4)));

    static final Set<Move> LEGALMOVESTEST =
            new HashSet<>(Arrays.asList(
                    (Move.mv("d6-d7(d8)")),
                    (Move.mv("d6-d7(d6)")),
                    (Move.mv("d6-d7(e6)")),
                    (Move.mv("d6-d7(d9)")),
                    (Move.mv("d6-d8(d9)")),
                    (Move.mv("d6-d8(e9)")),
                    (Move.mv("d6-d8(f10)")),
                    (Move.mv("d6-d8(d7)")),
                    (Move.mv("d6-d8(d6)")),
                    (Move.mv("d6-d9(e10)")),
                    (Move.mv("d6-d9(e9)")),
                    (Move.mv("d6-d9(f9)")),
                    (Move.mv("d6-d9(g9)")),
                    (Move.mv("d6-d9(d8)")),
                    (Move.mv("d6-d9(d7)")),
                    (Move.mv("d6-d9(d6)")),
                    (Move.mv("d6-e6(f6)")),
                    (Move.mv("d6-e6(d6)")),
                    (Move.mv("d6-e6(d7)")),
                    (Move.mv("d6-f6(g5)")),
                    (Move.mv("d6-f6(e6)")),
                    (Move.mv("d6-f6(d6)")),
                    (Move.mv("d6-c5(d6)")),
                    (Move.mv("d6-c5(d4)")),
                    (Move.mv("d6-c5(e3)")),
                    (Move.mv("d6-c5(f2)")),
                    (Move.mv("d6-c5(g1)")),
                    (Move.mv("d6-c5(c4)")),
                    (Move.mv("d6-c5(c3)")),
                    (Move.mv("d6-c5(c2)")),
                    (Move.mv("d6-c5(c1)")),
                    (Move.mv("a8-a7(a8)")),
                    (Move.mv("a8-a7(a6)")),
                    (Move.mv("a8-a6(a7)")),
                    (Move.mv("a8-a6(a8)"))));
}
