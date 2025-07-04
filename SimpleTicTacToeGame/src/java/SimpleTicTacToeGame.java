import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class SimpleTicTacToeGame extends JFrame implements ActionListener {
    private final char[][] symbols = new char[3][3];
    private final JButton[][] buttons = new JButton[3][3];
    private final JLabel announcement = new JLabel();
    private final JLabel scoreBoard = new JLabel();
    private boolean turnX = true;
    private String playerXName = "Player X";
    private String playerOName = "Player O";
    private int playerXWins = 0;
    private int playerOWins = 0;
    private int tieCount   = 0;

    public SimpleTicTacToeGame() {
        super("Tic-Tac-Toe Game");
        setLayout(new BorderLayout());
        setSize(900, 850);
        setLocationRelativeTo(null);

        getPlayerNames();

        // Announcement at top
        announcement.setHorizontalAlignment(SwingConstants.CENTER);
        announcement.setFont(new Font("SansSerif", Font.BOLD, 18));
        updateAnnouncement();
        add(announcement, BorderLayout.NORTH);

        // Scoreboard on the left
        scoreBoard.setFont(new Font("SansSerif", Font.PLAIN, 16));
        updateScoreBoard();
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        leftPanel.add(scoreBoard);
        add(leftPanel, BorderLayout.WEST);

        // Tic-Tac-Toe board in center
        JPanel board = new JPanel(new GridLayout(3, 3));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JButton btn = new JButton(" ");
                btn.setFont(btn.getFont().deriveFont(100.0f));
                btn.setActionCommand(i + " " + j);
                btn.addActionListener(this);
                buttons[i][j] = btn;
                symbols[i][j] = '.';
                board.add(btn);
            }
        }
        add(board, BorderLayout.CENTER);
    }

    private void getPlayerNames() {
        String x = JOptionPane.showInputDialog(
            this, "Enter Player 1's name (X):", "Player Setup", JOptionPane.QUESTION_MESSAGE);
        if (x != null && !x.trim().isEmpty()) playerXName = x.trim();

        String o = JOptionPane.showInputDialog(
            this, "Enter Player 2's name (O):", "Player Setup", JOptionPane.QUESTION_MESSAGE);
        if (o != null && !o.trim().isEmpty()) playerOName = o.trim();
    }

    private void updateAnnouncement() {
        String current = turnX ? playerXName : playerOName;
        char mark = turnX ? 'X' : 'O';
        announcement.setText("▶︎ " + current + "'s turn (" + mark + ")");
    }

    private void updateScoreBoard() {
        scoreBoard.setText(
            "<html><div style='text-align:center;'>"
            + "🏆 Score<br><br>"
            + playerXName + ": " + playerXWins + "<br>"
            + playerOName + ": " + playerOWins + "<br>"
            + "Ties: "    + tieCount
            + "</div></html>"
        );
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String[] parts = e.getActionCommand().split(" ");
        int row = Integer.parseInt(parts[0]);
        int col = Integer.parseInt(parts[1]);

        if (!buttons[row][col].isEnabled()) return;

        char mark = turnX ? 'X' : 'O';
        buttons[row][col].setText(String.valueOf(mark));
        buttons[row][col].setEnabled(false);
        symbols[row][col] = mark;
        turnX = !turnX;

        updateAnnouncement();

        if (won(mark)) {
            String winner = (mark == 'X' ? playerXName : playerOName);
            announcement.setText("🎉 " + winner + " wins!");
            if (mark == 'X') playerXWins++; else playerOWins++;
            updateScoreBoard();
            endGame(winner + " has won the game!");
        }
        else if (tie()) {
            announcement.setText("🤝 It's a tie!");
            tieCount++;
            updateScoreBoard();
            endGame("It's a tie!");
        }
    }

    private boolean won(char m) {
        for (int i = 0; i < 3; i++) {
            if ((symbols[i][0]==m && symbols[i][1]==m && symbols[i][2]==m) ||
                (symbols[0][i]==m && symbols[1][i]==m && symbols[2][i]==m))
                return true;
        }
        return (symbols[0][0]==m && symbols[1][1]==m && symbols[2][2]==m) ||
               (symbols[0][2]==m && symbols[1][1]==m && symbols[2][0]==m);
    }

    private boolean tie() {
        for (char[] row : symbols)
            for (char c : row)
                if (c == '.') return false;
        return true;
    }

    private void endGame(String msg) {
        for (JButton[] row : buttons)
            for (JButton b : row)
                b.setEnabled(false);
        showEndGameDialog(msg);
    }

    private void showEndGameDialog(String msg) {
        int opt = JOptionPane.showConfirmDialog(
            this,
            msg + "\nPlay again?",
            "Game Over",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE
        );
        if (opt == JOptionPane.YES_OPTION) resetGame();
        else System.exit(0);
    }

    private void resetGame() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                symbols[i][j] = '.';
                buttons[i][j].setText(" ");
                buttons[i][j].setEnabled(true);
            }
        turnX = true;
        updateAnnouncement();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SimpleTicTacToeGame game = new SimpleTicTacToeGame();
            game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            game.setVisible(true);
        });
    }
}