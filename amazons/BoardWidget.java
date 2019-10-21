package amazons;

import ucb.gui2.Pad;

import java.io.IOException;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import static amazons.Piece.*;
import static amazons.Square.sq;


/** A widget that displays an Amazons game.
 *  @author Vidhi Chander
 */
class BoardWidget extends Pad {

    /* Parameters controlling sizes, speeds, colors, and fonts. */

    /** Colors of empty squares and grid lines. */
    static final Color
            SPEAR_COLOR = new Color(64, 64, 64),
            LIGHT_SQUARE_COLOR = new Color(239, 207, 161),
            DARK_SQUARE_COLOR = new Color(205, 133, 63),
            CLICKED_COLOR = new Color(145, 145, 130);

    /** Locations of images of white and black queens. */
    private static final String
            WHITE_QUEEN_IMAGE = "wq4.png",
            BLACK_QUEEN_IMAGE = "bq4.png",
            SPEAR_IMAGE = "spear4.png";

    /** Size parameters. */
    private static final int
            SQUARE_SIDE = 30,
            BOARD_SIDE = SQUARE_SIDE * 10;

    /** A graphical representation of an Amazons board that sends commands
     *  derived from mouse clicks to COMMANDS.  */
    BoardWidget(ArrayBlockingQueue<String> commands) {
        _commands = commands;
        setMouseHandler("click", this::mouseClicked);
        setPreferredSize(BOARD_SIDE, BOARD_SIDE);

        try {
            _whiteQueen = ImageIO.read(Utils.getResource(WHITE_QUEEN_IMAGE));
            _blackQueen = ImageIO.read(Utils.getResource(BLACK_QUEEN_IMAGE));
            _spear = ImageIO.read(Utils.getResource(SPEAR_IMAGE));
        } catch (IOException excp) {
            System.err.println("Could not read queen images.");
            System.exit(1);
        }
        _acceptingMoves = false;
    }

    /** Draw the bare board G.  */
    private void drawGrid(Graphics2D g) {
        g.setColor(LIGHT_SQUARE_COLOR);
        int i = SQUARE_SIDE;
        int j;
        while (i <= BOARD_SIDE) {
            j = i % (SQUARE_SIDE * 2);
            while (j <= BOARD_SIDE) {
                g.fillRect(j - SQUARE_SIDE,
                        i - SQUARE_SIDE, SQUARE_SIDE, SQUARE_SIDE);
                j += SQUARE_SIDE * 2;
            }
            i += SQUARE_SIDE;
        }

        g.setColor(DARK_SQUARE_COLOR);
        i = SQUARE_SIDE;
        while (i <= BOARD_SIDE) {
            j = i % (SQUARE_SIDE * 2);
            while (j <= BOARD_SIDE) {
                g.fillRect(j, i - SQUARE_SIDE, SQUARE_SIDE, SQUARE_SIDE);
                j += SQUARE_SIDE * 2;
            }
            i += SQUARE_SIDE;
        }
    }

    @Override
    public synchronized void paintComponent(Graphics2D g) {
        drawGrid(g);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (_board.get(i, j) == SPEAR) {
                    drawSpears(g, Square.sq(i, 9 - j), SPEAR);
                } else if (_board.get(i, j) == BLACK
                        || _board.get(i, j) == WHITE) {
                    drawQueen(g, Square.sq(i, 9 - j), _board.get(i, j));
                }

            }
        }
    }

    /** Draw a queen for side PIECE at square S on G.  */
    private void drawQueen(Graphics2D g, Square s, Piece piece) {
        g.drawImage(piece == WHITE ? _whiteQueen : _blackQueen,
                cx(s.col()) + 2, cy(s.row()) + 4, null);
    }

    /** Draw a spear for side PIECE at square S on G.  */
    private void drawSpears(Graphics2D g, Square s, Piece piece) {
        g.drawImage(piece == SPEAR ? _spear : null,
                null, cx(s.col()), cy(s.row()) - 1);
    }

    /** Handle a click on S. */
    private void click(Square s) {
        if (moves.size() == 0) {
            moves.add(s.toString());
            System.out.println(s.toString());
        } else if (moves.size() > 0 && moves.size() <= 3) {
            moves.add(s.toString());
            System.out.println(s.toString());
        }
        if (moves.size() == 3) {
            _commands.offer(moves.get(0) + " "
                    + moves.get(1) + " " + moves.get(2));
            moves = new ArrayList<>();
        }
        repaint();
    }



        /** Handle mouse click event E. */
    private synchronized void mouseClicked(String unused, MouseEvent e) {
        int xpos = e.getX(), ypos = e.getY();
        int x = xpos / SQUARE_SIDE,
                y = (BOARD_SIDE - ypos) / SQUARE_SIDE;
        if (_acceptingMoves
                && x >= 0 && x < Board.SIZE && y >= 0 && y < Board.SIZE) {
            click(sq(x, y));
        }
    }

    /** Revise the displayed board according to BOARD. */
    synchronized void update(Board board) {
        _board.copy(board);
        repaint();
    }

    /** Turn on move collection iff COLLECTING, and clear any current
     *  partial selection.   When move collection is off, ignore clicks on
     *  the board. */
    void setMoveCollection(boolean collecting) {
        _acceptingMoves = collecting;
        repaint();
    }

    /** Return x-pixel coordinate of the left corners of column X
     *  relative to the upper-left corner of the board. */
    private int cx(int x) {
        return x * SQUARE_SIDE;
    }

    /** Return y-pixel coordinate of the upper corners of row Y
     *  relative to the upper-left corner of the board. */
    private int cy(int y) {
        return (Board.SIZE - y - 1) * SQUARE_SIDE;
    }

    /** Return x-pixel coordinate of the left corner of S
     *  relative to the upper-left corner of the board. */
    private int cx(Square s) {
        return cx(s.col());
    }

    /** Return y-pixel coordinate of the upper corner of S
     *  relative to the upper-left corner of the board. */
    private int cy(Square s) {
        return cy(s.row());
    }

    /** Queue on which to post move commands (from mouse clicks). */
    private ArrayBlockingQueue<String> _commands;
    /** Board being displayed. */
    private final Board _board = new Board();

    /** Image of white queen. */
    private BufferedImage _whiteQueen;
    /** Image of black queen. */
    private BufferedImage _blackQueen;

    /** Image of spear. */
    private BufferedImage _spear;

    /** True iff accepting moves from user. */
    private boolean _acceptingMoves;

    /** Collecting clicked moves. */
    private ArrayList<String> moves = new ArrayList<>();

}
