package amazons;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static java.lang.Math.*;

import static amazons.Piece.*;

/** A Player that automatically generates moves.
 *  @author Vidhi Chander
 */
class AI extends Player {

    /** A position magnitude indicating a win (for white if positive, black
     *  if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 1;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;

    /** A new AI with no piece or controller (intended to produce
     *  a template). */
    AI() {
        this(null, null);
    }

    /** A new AI playing PIECE under control of CONTROLLER. */
    AI(Piece piece, Controller controller) {
        super(piece, controller);
    }

    @Override
    Player create(Piece piece, Controller controller) {
        return new AI(piece, controller);
    }

    @Override
    String myMove() {
        Move move = findMove();
        _controller.reportMove(move);
        return move.toString();
    }

    /** Return a move for me from the current position, assuming there
     *  is a move. */
    private Move findMove() {
        Board b = new Board(board());
        if (_myPiece == WHITE) {
            findMove(b, maxDepth(b), true, 1, -INFTY, INFTY);
        } else {
            findMove(b, maxDepth(b), true, -1, -INFTY, INFTY);
        }
        return _lastFoundMove;
    }

    /** The move found by the last call to one of the ...FindMove methods
     *  below. */
    private Move _lastFoundMove;

    /** Find a move from position BOARD and return its value, recording
     *  the move found in _lastFoundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _lastMoveFound. */
    private int findMove(Board board, int depth, boolean saveMove, int sense,
                         int alpha, int beta) {
        if (depth == 0 || board.winner() != null) {
            return staticScore(board);
        }
        Iterator<Move> legalMoves = board.legalMoves(_myPiece);
        Set<Move> moves = new HashSet<>();
        while (legalMoves.hasNext()) {
            Move m = legalMoves.next();
            if (m != null) {
                moves.add(m);
            }
        }
        if (sense == 1) {
            int bestval = -INFTY;
            Move bestmove = null;
            for (Move m : moves) {
                if (m != null) {
                    board.makeMove(m);
                    int sc = findMove(board, depth - 1, false, -1, alpha, beta);
                    if (sc > bestval) {
                        bestval = sc;
                        bestmove = m;
                        alpha = max(alpha, bestval);
                        if (beta <= alpha) {
                            break;
                        }
                    }
                    board.undo();
                }
            }
            if (saveMove) {
                _lastFoundMove = bestmove;
            }
            return bestval;
        }
        if (sense == -1) {
            int bestval = INFTY;
            Move bestmove = null;
            for (Move m : moves) {
                if (m != null) {
                    board.makeMove(m);
                    int sc = findMove(board, depth - 1, false, 1, alpha, beta);
                    if (sc < bestval) {
                        bestval = sc;
                        bestmove = m;
                        alpha = min(alpha, bestval);
                        if (beta <= alpha) {
                            break;
                        }
                    }
                    board.undo();
                }
            }
            if (saveMove) {
                _lastFoundMove = bestmove;
            }
            return bestval;
        }
        return 0;
    }

    /** Return a heuristically determined maximum search depth
     *  based on characteristics of BOARD. */
    private int maxDepth(Board board) {
        int N = board.numMoves();
        return 10 - N / 10;
    }


    /** Return a heuristic value for BOARD. */
    private int staticScore(Board board) {
        Piece winner = board.winner();
        if (winner == BLACK) {
            System.out.println("Black wins.");
            return -WINNING_VALUE;
        } else if (winner == WHITE) {
            System.out.println("White wins.");
            return WINNING_VALUE;
        }
        int white = board.numberColor(WHITE);
        int black = board.numberColor(BLACK);
        return white - black;
    }


}
