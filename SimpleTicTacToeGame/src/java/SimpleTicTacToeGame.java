import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

public class SimpleTicTacToeGame extends JFrame implements ActionListener {
    private final char[][] boardState = new char[3][3];
    private final JButton[][] buttons  = new JButton[3][3];
    private final JLabel announcement  = new JLabel();
    private final JLabel scoreLabel    = new JLabel();
    private final JButton resetScores  = new JButton("Reset Scores");

    private Users xPlayer, oPlayer;
    private ScoreBoard scoreBoard;
    private boolean vsComputer;
    private boolean xTurn = true;

    public SimpleTicTacToeGame() {
        super("Tic-Tac-Toe");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 850);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        setupModeAndPlayers();
        initUI();
        resetBoard();
    }

    private void setupModeAndPlayers() {
        String[] options = {"Player vs Player", "Player vs Computer"};
        int choice = JOptionPane.showOptionDialog(
            this,
            "Select mode:",
            "Game Mode",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );
        vsComputer = (choice == 1);

        String nameX = askName("Player 1 (X)");
        xPlayer = new Users(nameX, 'X');

        String nameO = vsComputer
            ? "Computer"
            : askName("Player 2 (O)");
        oPlayer = new Users(nameO, 'O');

        scoreBoard = new ScoreBoard(xPlayer, oPlayer);
    }

    private String askName(String prompt) {
        String s = JOptionPane.showInputDialog(this, "Enter " + prompt + " name:");
        return (s != null && !s.trim().isEmpty()) ? s.trim() : prompt;
    }

    private void initUI() {
        // --- Announcement (top) ---
        announcement.setFont(new Font("SansSerif", Font.BOLD, 18));
        announcement.setHorizontalAlignment(SwingConstants.CENTER);
        add(announcement, BorderLayout.NORTH);

        // --- Score panel (left) ---
        scoreLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        resetScores.setAlignmentX(Component.CENTER_ALIGNMENT);
        resetScores.addActionListener(e -> {
            scoreBoard.reset();
            refreshScoreDisplay();
        });

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        left.add(scoreLabel);
        left.add(Box.createVerticalStrut(10));
        left.add(resetScores);
        add(left, BorderLayout.WEST);

        // --- Tic-Tac-Toe grid (center) ---
        JPanel grid = new JPanel(new GridLayout(3,3));
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                JButton b = new JButton(" ");
                b.setFont(b.getFont().deriveFont(100f));
                b.setActionCommand(r + ":" + c);
                b.addActionListener(this);
                buttons[r][c] = b;
                grid.add(b);
            }
        }
        add(grid, BorderLayout.CENTER);

        refreshScoreDisplay();
        updateAnnouncement();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String[] rc = e.getActionCommand().split(":");
        int r = Integer.parseInt(rc[0]),
            c = Integer.parseInt(rc[1]);
        if (!buttons[r][c].isEnabled()) return;

        // 1) Human move
        Users mover = xTurn ? xPlayer : oPlayer;
        markCell(r, c, mover.getMark());

        if (resolveRound(mover)) return;

        // 2) Flip turn & announce
        xTurn = !xTurn;
        updateAnnouncement();

        // 3) If vsComputer and now O's turn, let CPU move
        if (vsComputer && !xTurn) {
            Point p = computeComputerMove();
            markCell(p.x, p.y, oPlayer.getMark());
            if (resolveRound(oPlayer)) return;
            xTurn = !xTurn;
            updateAnnouncement();
        }
    }

    private Point computeComputerMove() {
        List<Point> free = new ArrayList<>();
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (boardState[i][j] == '.') free.add(new Point(i, j));
        return free.get(new Random().nextInt(free.size()));
    }

    private void markCell(int r, int c, char m) {
        boardState[r][c] = m;
        JButton b = buttons[r][c];
        b.setText(String.valueOf(m));
        b.setEnabled(false);
    }

    /**
     * Returns true if this round ended (win or tie).
     * In that case, it records the result, pops up the dialog,
     * and resets or exits.
     */
    private boolean resolveRound(Users u) {
        char m = u.getMark();
        if (isWin(m)) {
            announcement.setText("🎉 " + u.getName() + " wins!");
            scoreBoard.recordWin(u);
            refreshScoreDisplay();
            endRound(u.getName() + " has won!");
            return true;
        }
        if (isTie()) {
            announcement.setText("🤝 It's a tie!");
            scoreBoard.recordTie();
            refreshScoreDisplay();
            endRound("It's a tie!");
            return true;
        }
        return false;
    }

    private boolean isWin(char m) {
        for (int i = 0; i < 3; i++) {
            if ((boardState[i][0]==m && boardState[i][1]==m && boardState[i][2]==m) ||
                (boardState[0][i]==m && boardState[1][i]==m && boardState[2][i]==m))
                return true;
        }
        return (boardState[0][0]==m && boardState[1][1]==m && boardState[2][2]==m) ||
               (boardState[0][2]==m && boardState[1][1]==m && boardState[2][0]==m);
    }

    private boolean isTie() {
        for (char[] row : boardState)
            for (char cell : row)
                if (cell == '.') return false;
        return true;
    }

    private void endRound(String msg) {
        disableAllCells();
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

    private void disableAllCells() {
        for (var row : buttons)
            for (var b : row)
                b.setEnabled(false);
    }

    private void resetBoard() {
        for (int r = 0; r < 3; r++)
            for (int c = 0; c < 3; c++) {
                boardState[r][c] = '.';
                buttons[r][c].setText(" ");
                buttons[r][c].setEnabled(true);
            }
        xTurn = true;
        updateAnnouncement();
    }

    private void refreshScoreDisplay() {
        scoreLabel.setText(scoreBoard.getFormattedScores());
    }

    private void updateAnnouncement() {
        Users who = xTurn ? xPlayer : oPlayer;
        announcement.setText("▶︎ It is " + who.getName() + "'s turn (" + who.getMark() + ")");
    }
}