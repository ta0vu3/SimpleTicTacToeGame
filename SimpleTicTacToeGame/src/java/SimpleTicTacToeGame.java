import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class SimpleTicTacToeGame extends JFrame implements ActionListener {
    private final char[][] symbols = new char[3][3];
    private final JButton[][] buttons = new JButton[3][3];
    private final JLabel announcement = new JLabel();
    private boolean turnX = true;
    private String playerXName = "Player X";
    private String playerOName = "Player O";

    public SimpleTicTacToeGame() {
        super("Tic-Tac-Toe Game");
        setLayout(new BorderLayout());
        setSize(750, 800);

        getPlayerNames(); // Prompt player name input

        announcement.setHorizontalAlignment(SwingConstants.CENTER);
        announcement.setText("Final Program Project - Tic Tac Toe Game **** " + playerXName.toUpperCase() + " STARTS THE GAME FIRST!!!! ****");
        add(announcement, BorderLayout.NORTH);

        JPanel board = new JPanel(new GridLayout(3, 3));
        add(board, BorderLayout.CENTER);

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                JButton btn = new JButton(" ");
                btn.setFont(btn.getFont().deriveFont(100.0f));
                btn.addActionListener(this);
                btn.setActionCommand(i + " " + j);
                buttons[i][j] = btn;
                symbols[i][j] = '.';
                board.add(btn);
            }
    }

    private void getPlayerNames() {
        playerXName = JOptionPane.showInputDialog(this, "Enter Player 1's name (X):", "Player Setup", JOptionPane.QUESTION_MESSAGE);
        if (playerXName == null || playerXName.trim().isEmpty()) playerXName = "Player X";

        playerOName = JOptionPane.showInputDialog(this, "Enter Player 2's name (O):", "Player Setup", JOptionPane.QUESTION_MESSAGE);
        if (playerOName == null || playerOName.trim().isEmpty()) playerOName = "Player O";
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String[] coords = e.getActionCommand().split(" ");
        int row = Integer.parseInt(coords[0]);
        int col = Integer.parseInt(coords[1]);

        if (!buttons[row][col].isEnabled()) return;

        char currentMark = turnX ? 'X' : 'O';
        buttons[row][col].setText(String.valueOf(currentMark));
        buttons[row][col].setEnabled(false);
        symbols[row][col] = currentMark;
        turnX = !turnX;

        String nextPlayer = turnX ? playerXName : playerOName;
        announcement.setText("**** IT IS " + nextPlayer.toUpperCase() + "'S TURN!!! ****");

        if (won(currentMark)) {
            String winner = currentMark == 'X' ? playerXName : playerOName;
            announcement.setText("**** " + winner.toUpperCase() + " HAS WON THE GAME!!! ****");
            endGame(winner + " has won the game!");
        } else if (tie()) {
            announcement.setText("**** IT IS A TIE GAME!!! ****");
            endGame("It’s a tie!");
        }
    }

    private boolean won(char mark) {
        for (int i = 0; i < 3; i++)
            if ((symbols[i][0] == mark && symbols[i][1] == mark && symbols[i][2] == mark) ||
                (symbols[0][i] == mark && symbols[1][i] == mark && symbols[2][i] == mark))
                return true;

        return (symbols[0][0] == mark && symbols[1][1] == mark && symbols[2][2] == mark) ||
               (symbols[0][2] == mark && symbols[1][1] == mark && symbols[2][0] == mark);
    }

    private boolean tie() {
        for (char[] row : symbols)
            for (char cell : row)
                if (cell == '.')
                    return false;
        return true;
    }

    private void endGame(String message) {
        for (JButton[] row : buttons)
            for (JButton btn : row)
                btn.setEnabled(false);

        showEndGameDialog(message);
    }

    private void showEndGameDialog(String message) {
        int option = JOptionPane.showConfirmDialog(
            this,
            message + "\nWould you like to play again?",
            "Game Over",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE
        );

        if (option == JOptionPane.YES_OPTION)
            resetGame();
        else
            System.exit(0);
    }

    private void resetGame() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                symbols[i][j] = '.';
                buttons[i][j].setText(" ");
                buttons[i][j].setEnabled(true);
            }
        turnX = true;
        announcement.setText("**** " + playerXName.toUpperCase() + " STARTS THE GAME FIRST!!!! ****");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SimpleTicTacToeGame game = new SimpleTicTacToeGame();
            game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            game.setVisible(true);
        });
    }
}