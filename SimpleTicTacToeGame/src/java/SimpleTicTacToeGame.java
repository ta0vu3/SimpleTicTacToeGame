import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

public class SimpleTicTacToeGame extends JFrame implements ActionListener {
    // Game state
    private final char[][] symbols = new char[3][3];
    private final JButton[][] buttons = new JButton[3][3];
    private boolean turnX = true;
    private boolean vsComputer = false;

    // Players & scores
    private String playerXName = "Player X";
    private String playerOName = "Player O";
    private int playerXWins = 0;
    private int playerOWins = 0;
    private int tieCount    = 0;

    // UI components
    private final JLabel announcement = new JLabel();
    private final JLabel scoreBoard   = new JLabel();
    private final JButton resetScoresButton = new JButton("Reset Scores");

    public SimpleTicTacToeGame() {
        super("Tic-Tac-Toe");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 850);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initUI();         // build panels & buttons
        askGameMode();    // choose mode, get names, reset scores & board
    }

    private void initUI() {
        // Top announcement
        announcement.setHorizontalAlignment(SwingConstants.CENTER);
        announcement.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(announcement, BorderLayout.NORTH);

        // Left scoreboard + reset scores button
        scoreBoard.setFont(new Font("SansSerif", Font.PLAIN, 16));
        resetScoresButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        resetScoresButton.addActionListener(e -> {
            playerXWins = 0;
            playerOWins = 0;
            tieCount    = 0;
            updateScoreBoard();
        });

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        left.add(scoreBoard);
        left.add(Box.createRigidArea(new Dimension(0, 10)));
        left.add(resetScoresButton);
        add(left, BorderLayout.WEST);

        // Center board
        JPanel board = new JPanel(new GridLayout(3,3));
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                JButton btn = new JButton(" ");
                btn.setFont(btn.getFont().deriveFont(100f));
                btn.setActionCommand(i + " " + j);
                btn.addActionListener(this);
                buttons[i][j] = btn;
                board.add(btn);
            }
        add(board, BorderLayout.CENTER);
    }

    private void askGameMode() {
        String[] opts = {"Player vs Player", "Player vs Computer"};
        int choice = JOptionPane.showOptionDialog(
            this,
            "Select opponent type:",
            "Game Mode",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            opts,
            opts[0]
        );

        vsComputer = (choice == 1);
        getPlayerNames();
        playerXWins = 0;
        playerOWins = 0;
        tieCount    = 0;
        updateScoreBoard();
        resetBoard();
    }

    private void getPlayerNames() {
        String x = JOptionPane.showInputDialog(this, "Enter Player 1's name (X):");
        if (x != null && !x.trim().isEmpty()) playerXName = x.trim();

        if (vsComputer) {
            playerOName = "Computer";
        } else {
            String o = JOptionPane.showInputDialog(this, "Enter Player 2's name (O):");
            if (o != null && !o.trim().isEmpty()) playerOName = o.trim();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String[] parts = e.getActionCommand().split(" ");
        int r = Integer.parseInt(parts[0]), c = Integer.parseInt(parts[1]);
        if (!buttons[r][c].isEnabled()) return;

        makeMove(r, c, turnX ? 'X' : 'O');
        if (checkEndOfRound(turnX ? 'O' : 'X')) return;

        if (vsComputer && !turnX) {
            doComputerMove();
            checkEndOfRound('O');
        }
    }

    private void makeMove(int r, int c, char mark) {
        buttons[r][c].setText(String.valueOf(mark));
        buttons[r][c].setEnabled(false);
        symbols[r][c] = mark;
        turnX = (mark == 'O');
        updateAnnouncement();
    }

    private void doComputerMove() {
        List<Point> empties = new ArrayList<>();
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (symbols[i][j] == '.') empties.add(new Point(i, j));

        if (empties.isEmpty()) return;
        Point p = empties.get(new Random().nextInt(empties.size()));
        makeMove(p.x, p.y, 'O');
    }

    private boolean checkEndOfRound(char lastMark) {
        if (won(lastMark)) {
            String winner = lastMark == 'X' ? playerXName : playerOName;
            announcement.setText("🎉 " + winner + " wins!");
            if (lastMark == 'X') playerXWins++; else playerOWins++;
            updateScoreBoard();
            endGameDialog(winner + " has won!");
            return true;
        }
        if (tie()) {
            tieCount++;
            announcement.setText("🤝 It's a tie!");
            updateScoreBoard();
            endGameDialog("It's a tie!");
            return true;
        }
        return false;
    }

    private boolean won(char m) {
        for (int i = 0; i < 3; i++)
            if ((symbols[i][0] == m && symbols[i][1] == m && symbols[i][2] == m) ||
                (symbols[0][i] == m && symbols[1][i] == m && symbols[2][i] == m))
                return true;

        return (symbols[0][0] == m && symbols[1][1] == m && symbols[2][2] == m) ||
               (symbols[0][2] == m && symbols[1][1] == m && symbols[2][0] == m);
    }

    private boolean tie() {
        for (char[] row : symbols)
            for (char cell : row)
                if (cell == '.') return false;
        return true;
    }

    private void endGameDialog(String msg) {
        disableAllButtons();
        int opt = JOptionPane.showConfirmDialog(
            this,
            msg + "\nPlay again?",
            "Game Over",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE
        );
        if (opt == JOptionPane.YES_OPTION) {
            resetBoard();
        } else {
            System.exit(0);
        }
    }

    private void disableAllButtons() {
        for (var row : buttons)
            for (var b : row)
                b.setEnabled(false);
    }

    private void resetBoard() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                symbols[i][j] = '.';
                buttons[i][j].setText(" ");
                buttons[i][j].setEnabled(true);
            }
        turnX = true;
        updateAnnouncement();
    }

    private void updateAnnouncement() {
        String who = turnX ? playerXName : playerOName;
        char mark = turnX ? 'X' : 'O';
        announcement.setText("▶︎ " + who + "'s turn (" + mark + ")");
    }

    private void updateScoreBoard() {
        scoreBoard.setText(
            "<html><div style='text-align:center;'>"
          + "🏆 Score<br><br>"
          + playerXName + ": " + playerXWins + "<br>"
          + playerOName + ": " + playerOWins + "<br>"
          + "Ties: "   + tieCount
          + "</div></html>"
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SimpleTicTacToeGame().setVisible(true);
        });
    }
}