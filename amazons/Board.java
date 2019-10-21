package amazons;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Collections;


import static amazons.Piece.*;


/** The state of an Amazons Game.
 *  @author Vidhi Chander
 */
class Board {

    /** The number of squares on a side of the board. */
    static final int SIZE = 10;

    /** Initializes a game board with SIZE squares on a side in the
     *  initial position. */
    Board() {
        init();
    }

    /** Initializes a copy of MODEL. */
    Board(Board model) {
        copy(model);
    }

    /** Copies MODEL into me. */
    void copy(Board model) {
        Piece[][] board1 = new Piece[SIZE][SIZE];
        this.board = board1;
        for (int i = 0; i < this.SIZE; i++) {
            for (int j = 0; j < this.SIZE; j++) {
                this.board[i][j] = model.board[i][j];
            }
        }
    }

    /** Clears the board to the initial position. */
    void init() {
        board = new Piece[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = EMPTY;
            }
        }
        put(WHITE, Square.sq("d1"));
        put(WHITE, Square.sq("g1"));
        put(WHITE, Square.sq("a4"));
        put(WHITE, Square.sq("j4"));
        put(BLACK, Square.sq("a7"));
        put(BLACK, Square.sq("d10"));
        put(BLACK, Square.sq("g10"));
        put(BLACK, Square.sq("j7"));
        _turn = WHITE;
        _winner = null;
    }

    /** Return the Piece whose move it is (WHITE or BLACK). */
    Piece turn() {
        return _turn;
    }

    /** Return the number of moves (that have not been undone) for this
     *  board. */
    int numMoves() {
        return _numMoves;
    }

    /** Return the number of piece P moves (that have not been undone) for this
     *  board. */
    int numberColor(Piece p) {
        if (p == WHITE) {
            return _whiteMoves.size();
        } else if (p == BLACK) {
            return _blackMoves.size();
        }
        return 0;
    }

    /** Return the winner in the current position, or null if the game is
     *  not yet finished. */
    Piece winner() {
        return _winner;
    }

    /** Return the contents the square at S. */
    final Piece get(Square s) {
        if (s != null) {
            return get(s.col(), (9 - s.row()));
        }
        return null;
    }

    /** Return the contents of the square at (COL, ROW), where
     *  0 <= COL, ROW <= 9. */
    final Piece get(int col, int row) {
        return board[9 - row][col];
    }

    /** Return the contents of the square at COL ROW. */
    final Piece get(char col, char row) {
        return get(col - 'a', row - '1');
    }

    /** Set square S to P. */
    final void put(Piece p, Square s) {
        put(p, s.col(), s.row());
    }

    /** Set square (COL, ROW) to P. */
    final void put(Piece p, int col, int row) {
        board[row][col] = p;
        _winner = EMPTY;
    }

    /** Set square COL ROW to P. */
    final void put(Piece p, char col, char row) {
        put(p, col - 'a', row - '1');
    }

    /** Return true iff FROM - TO is an unblocked queen move on the current
     *  board, ignoring the contents of ASEMPTY, if it is encountered.
     *  For this to be true, FROM-TO must be a queen move and the
     *  squares along it, other than FROM and ASEMPTY, must be
     *  empty. ASEMPTY may be null, in which case it has no effect. */
    boolean isUnblockedMove(Square from, Square to, Square asEmpty) {

        if (from.isQueenMove(to)) {
            int direction = from.direction(to);
            int steps = 0;
            Square s;
            while (from.queenMove(direction, steps) != to) {
                steps += 1;
                s = from.queenMove(direction, steps);
                if (board[s.row()][s.col()] != EMPTY && s != asEmpty) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /** Return true iff FROM is a valid starting square for a move. */
    boolean isLegal(Square from) {
        return get(from).equals(turn());
    }

    /** Return true iff FROM-TO is a valid first part of move, ignoring
     *  spear throwing. */
    boolean isLegal(Square from, Square to) {
        return isLegal(from) && (to != null) && isUnblockedMove(from, to, null);
    }

    /** Return true iff FROM-TO is a valid first part of move for
     *  the spear, with ASEMPTY = original queen move. */
    boolean isLegal(Square from, Square to, Square asEmpty) {
        return (to != null) && isLegal(asEmpty)
                && isUnblockedMove(from, to, asEmpty);
    }

    /** Return true iff MOVE is a legal move in the current
     *  position. */
    boolean isLegal(Move move) {
        return isLegal(move.from(), move.to())
                && isUnblockedMove(move.to(), move.spear(), move.from());
    }

    /** Move FROM-TO(SPEAR), assuming this is a legal move. */
    void makeMove(Square from, Square to, Square spear) {
        turn();
        try {
            if (isLegal(from, to) && isUnblockedMove(to, spear, from)) {
                if (_turn == WHITE) {
                    _whiteMoves.add(from);
                    _whiteMoves.add(to);
                    _whiteMoves.add(spear);
                } else {
                    _blackMoves.add(from);
                    _blackMoves.add(to);
                    _blackMoves.add(spear);
                }
                put(get(from), to);
                put(EMPTY, from);
                put(SPEAR, spear);
                if (_turn == WHITE) {
                    _turn = BLACK;
                    _numMoves += 1;
                } else if (_turn == BLACK) {
                    _turn = WHITE;
                    _numMoves += 1;
                }
            } else {
                throw new NullPointerException();
            }
        } catch (NullPointerException error) {
            System.out.println("Bad Move.");
        }


    }

    /** Move according to MOVE, assuming it is a legal move. */
    void makeMove(Move move) {
        makeMove(move.from(), move.to(), move.spear());
    }

    /** Undo one move.  Has no effect on the initial board. */
    void undo() {
        if (numMoves() >= 1) {
            if (_turn == BLACK) {
                put(EMPTY, _whiteMoves.getLast());
                _whiteMoves.removeLast();
                put(EMPTY, _whiteMoves.getLast());
                _whiteMoves.removeLast();
                put(WHITE, _whiteMoves.getLast());
                _whiteMoves.removeLast();
                _turn = WHITE;
            } else if (_turn == WHITE) {
                put(EMPTY, _blackMoves.getLast());
                _blackMoves.removeLast();
                put(EMPTY, _blackMoves.getLast());
                _blackMoves.removeLast();
                put(BLACK, _blackMoves.getLast());
                _blackMoves.removeLast();
                _turn = BLACK;
            }
            _numMoves -= 1;
        }
    }

    /** Return an Iterator over the Squares that are reachable by an
     *  unblocked queen move from FROM. Does not pay attention to what
     *  piece (if any) is on FROM, nor to whether the game is finished.
     *  Treats square ASEMPTY (if non-null) as if it were EMPTY.  (This
     *  feature is useful when looking for Moves, because after moving a
     *  piece, one wants to treat the Square it came from as empty for
     *  purposes of spear throwing.) */
    Iterator<Square> reachableFrom(Square from, Square asEmpty) {
        return new ReachableFromIterator(from, asEmpty);
    }

    /** Return an Iterator over all legal moves on the current board. */
    Iterator<Move> legalMoves() {
        return new LegalMoveIterator(_turn);
    }

    /** Return an Iterator over all legal moves on the current board for
     *  SIDE (regardless of whose turn it is). */
    Iterator<Move> legalMoves(Piece side) {
        return new LegalMoveIterator(side);
    }

    /** An iterator used by reachableFrom. */
    private class ReachableFromIterator implements Iterator<Square> {

        /** Iterator of all squares reachable by queen move from FROM,
         *  treating ASEMPTY as empty. */
        ReachableFromIterator(Square from, Square asEmpty) {
            _from = from;
            _dir = -1;
            _steps = 0;
            _asEmpty = asEmpty;
            toNext();
        }

        @Override
        public boolean hasNext() {
            return _dir < 8;
        }

        @Override
        public Square next() {
            _steps += 1;
            if (_asEmpty == null) {
                if (isLegal(_from, _from.queenMove(_dir, _steps))) {
                    return _from.queenMove(_dir, _steps);
                }
            } else {
                if (isLegal(_from, _from.queenMove(_dir, _steps), _asEmpty)) {
                    return _from.queenMove(_dir, _steps);
                }
            }
            toNext();
            if (hasNext()) {
                return next();
            }
            return null;
        }

        /** Advance _dir and _steps, so that the next valid Square is
         *  _steps steps in direction _dir from _from. */
        private void toNext() {
            _dir += 1;
            _steps = 0;
        }

        /** Starting square. */
        private Square _from;
        /** Current direction. */
        private int _dir;
        /** Current distance. */
        private int _steps;
        /** Square treated as empty. */
        private Square _asEmpty;

    }

    /** An iterator used by legalMoves. */
    private class LegalMoveIterator implements Iterator<Move> {

        /** All legal moves for SIDE (WHITE or BLACK). */
        LegalMoveIterator(Piece side) {
            _startingSquares = Square.iterator();
            _spearThrows = NO_SQUARES;
            _pieceMoves = NO_SQUARES;
            _fromPiece = side;
            _turn = side;
            toNext();
        }

        @Override
        public boolean hasNext() {
            return _startingSquares.hasNext();
        }

        @Override
        public Move next() {
            if (get(_start) == _fromPiece && _nextSquare != null) {
                _nextSpear = _spearThrows.next();
                if (_nextSpear == null) {
                    toNext();
                    if (hasNext()) {
                        return next();
                    }
                }
                Move moved = Move.mv(_start, _nextSquare, _nextSpear);
                return moved;
            }
            toNext();
            if (hasNext()) {
                return next();
            }
            return null;
        }


        /** Advance so that the next valid Move is
         *  _start-_nextSquare(sp), where sp is the next value of
         *  _spearThrows. */
        private void toNext() {
            if (_nextSquare == null) {
                _start = _startingSquares.next();
                _pieceMoves = new ReachableFromIterator(_start, null);

            }
            _nextSquare = _pieceMoves.next();

            if (_nextSquare != null) {
                _spearThrows = new ReachableFromIterator(_nextSquare, _start);
            }


        }

        /** Color of side whose moves we are iterating. */
        private Piece _fromPiece;
        /** Current starting square. */
        private Square _start;
        /** Remaining starting squares to consider. */
        private Iterator<Square> _startingSquares;
        /** Current piece's new position. */
        private Square _nextSquare = null;
        /** Remaining moves from _start to consider. */
        private Iterator<Square> _pieceMoves;
        /** Remaining spear throws from _piece to consider. */
        private Iterator<Square> _spearThrows;
        /** Current spear's position. */
        private Square _nextSpear;
    }

    @Override
    public String toString() {
        String returned = "";
        for (int i = 0; i < 10; i++) {
            returned += "   ";
            for (int j = 0; j < 10; j++) {
                if (board[i][j] == EMPTY) {
                    returned += EMPTY.toString();
                }
                if (board[i][j] == WHITE) {
                    returned += WHITE.toString();
                }
                if (board[i][j] == BLACK) {
                    returned += BLACK.toString();
                }
                if (board[i][j] == SPEAR) {
                    returned += SPEAR.toString();
                }
                if (j < 9) {
                    returned += " ";
                }
            }
            returned += "\n";
        }
        return returned;
    }

    /** An empty iterator for initialization. */
    private static final Iterator<Square> NO_SQUARES =
            Collections.emptyIterator();

    /** Piece whose turn it is (BLACK or WHITE). */
    private Piece _turn;
    /** Cached value of winner on this board, or EMPTY if it has not been
     *  computed. */
    private Piece _winner;

    /** An empty array for initialization. */
    private Piece[][] board;

    /** Keep track of number of moves made. */
    private int _numMoves = 0;

    /** Keep track of all the white moves made. */
    private LinkedList<Square> _whiteMoves = new LinkedList<>();

    /** Keep track of all the black moves made. */
    private LinkedList<Square> _blackMoves = new LinkedList<>();
}
