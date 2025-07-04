
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

public class SimpleTicTacToeGame extends JFrame implements ActionListener {
    private char[][] symbols; // X AND O SYMBOLS
    private JPanel board; // BOARD LAYOUT
    private JLabel announcement; // DISPLAY MESSAGES TO THE NORTH BORDER LAYOUT
    private JButton[][] buttons; // ARRAY BUTTONS THAT PLAYERS CLICK ON
    private boolean turnX; // BOOLEAN TO TAKE TURNS AFTER A PLAYER CLICKS ON THEIR BOX

    public SimpleTicTacToeGame() {
        // SUPER IMPLEMENTED TO TIE EVERYTHING TOGETHER
        super("Tic-Tac-Toe Game"); // GAME NAME
        this.setLayout(new BorderLayout());
        this.setSize(750, 750);
        announcement =
            new JLabel("Final Program Project - Tic Tac Toe Game " + " **** PLAYER X STARTS THE GAME FIRST!!!! ****"); // BEGINNING
                                                                                                                       // HEADER
                                                                                                                       // MESSAGE
        announcement.setVisible(true); // SET TO TRUE TO BE ABLE TO LET THE PLAYER VIEW THE
                                       // ANNOUCEMENT.
        this.add(announcement, BorderLayout.NORTH);// DISPLAYS ON NORTH BORDER AS STATED IN THE
                                                   // PROJECT DETAIL.

        board = new JPanel(new GridLayout(3, 3)); // 3x3 GRIDLAYOUT FOR THE BOARD GAME. TOTAL OF 9
                                                  // BOXES
        board.setVisible(true);
        buttons = new JButton[3][3]; // ARRAYS TO SET FOR 3x3 BOX LAYOUT
        symbols = new char[3][3]; // CHAR SETTING OF X'S AND O'S AFTER CLICKING ON THE BOX.
        for (int i = 0; i < 3; i++ ) { // FOR LOOP TO LINK BUTTONS, EMPTY BOX, AND APPROPRIATE X AND
                                       // O SYMBOL DISPLAYS.
            for (int j = 0; j < 3; j++ ) {
                buttons[i][j] = new JButton(" ");
                buttons[i][j].setEnabled(true);
                buttons[i][j].addActionListener(this);
                buttons[i][j].setActionCommand(i + " " + j);
                buttons[i][j].setVisible(true);
                board.add(buttons[i][j]);
                symbols[i][j] = '.';
            }
        }
        this.add(board, BorderLayout.CENTER); // A CENTER BORDER LAYOUT AS DESCRIBED IN PROGRAMMING
                                              // DETAILED.
        turnX = true;
    }

    private boolean won(char playerMark, char[][] board) { // BOOLEAN TO KEEP TRACK OF 3 IN A ROW TO
                                                           // DISPLAY WINNER.
        for (int row = 0; row < 3; row++ ) {
            int count = 0;
            for (int col = 0; col < 3; col++ ) {
                if (board[row][col] == playerMark)
                    count++ ;
                else
                    count = 0;
            }

            if (count == 3)
                return true;
        }

        for (int col = 0; col < 3; col++ ) {
            int count = 0;
            for (int row = 0; row < 3; row++ ) {
                if (board[row][col] == playerMark)
                    count++ ;
                else
                    count = 0;
            }

            if (count == 3)
                return true;
        }
        if (board[1][1] == playerMark && board[0][0] == playerMark && board[2][2] == playerMark)
            return true;

        if (board[1][1] == playerMark && board[0][2] == playerMark && board[2][0] == playerMark)
            return true;
        return false;
    }

    private boolean tie(char[][] board) { // BOOLEAN TO KEEP TRACK IF THERE ARE NO 3 MATCHING
                                          // SYMBOLS AND DISPLAYS A TIE GAME.

        for (int i = 0; i < 3; i++ ) {
            for (int j = 0; j < 3; j++ ) {
                if (board[i][j] == '.')
                    return false;
            }
        }

        return true;
    }

    public void actionPerformed(ActionEvent e) { // ACTION PERFORMED METHOD

        String a = e.getActionCommand();
        if (a.length() == 3) {

            Scanner input = new Scanner(a);
            String rowString = input.next();
            String colString = input.next();

            int row = Integer.parseInt(rowString);
            int col = Integer.parseInt(colString);

            JButton button = buttons[row][col];

            if (button.isEnabled()) {
                if (turnX) {
                    announcement.setText("**** IT IS PLAYER O'S TURN!!! ****"); // DISPLAY TO SHOW
                                                                                // PLAYER'S O TURN
                                                                                // TO CLICK ON AN
                                                                                // EMPTY BOX
                    button.setText("X");
                    symbols[row][col] = 'X';
                    button.setFont(button.getFont().deriveFont(100.0f)); // INCREASING THE SIZE OF
                                                                         // THE CHAR
                    button.setEnabled(false);
                    turnX = false;
                } else {
                    announcement.setText("**** IT IS PLAYER X'S TURN!!! ****"); // DISPLAY TO SHOW
                                                                                // PLAYER'S X TURN
                                                                                // TO CLICK ON AN
                                                                                // EMPTY BOX
                    button.setText("O");
                    symbols[row][col] = 'O';
                    button.setFont(button.getFont().deriveFont(100.0f)); // INCREASING THE SIZE OF
                                                                         // THE CHAR
                    button.setEnabled(false);
                    turnX = true;
                }
            }
            if (tie(symbols)) { // DISPLAYS IF NOBODY GETS THE SAME SYMBOLS FOR 3 IN A ROW
                announcement
                    .setText("**** IT IS A TIE GAME!!! CLICK ON THE X ON THE TOP RIGHT TO EXIT THE PROGRAM ****");
                for (int i = 0; i < 3; i++ )
                    for (int j = 0; j < 3; j++ )
                        buttons[i][j].setEnabled(false);
            }
            if (won('X', symbols)) { // DISPLAY IF X HAS 3 SAME SYMBOLS IN A ROW
                announcement.setText(
                    "**** PLAYER X HAS WON THE GAME!!! CLICK ON THE X ON THE TOP RIGHT TO EXIT THE PROGRAM ****");
                for (int i = 0; i < 3; i++ )
                    for (int j = 0; j < 3; j++ )
                        buttons[i][j].setEnabled(false);
            }

            if (won('O', symbols)) { // DISPLAY IF O HAS 3 SAME SYMBOLS IN A ROW
                announcement.setText(
                    "**** PLAYER O HAS WON THE GAME!!! CLICK ON THE X ON THE TOP RIGHT TO EXIT THE PROGRAM ****");
                for (int i = 0; i < 3; i++ )
                    for (int j = 0; j < 3; j++ )
                        buttons[i][j].setEnabled(false);
            }
            input.close(); // CLOSING THE INPUT STREAM
        }
    }
}
