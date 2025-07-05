public class ScoreBoard {
    private final Users xPlayer;
    private final Users oPlayer;
    private int ties;

    public ScoreBoard(Users xPlayer, Users oPlayer) {
        this.xPlayer = xPlayer;
        this.oPlayer = oPlayer;
        this.ties = 0;
    }

    public void recordWin(Users whoWon) {
        whoWon.incrementWins();
    }

    public void recordTie() {
        ties++;
    }

    public int getTies() {
        return ties;
    }

    public void reset() {
        xPlayer.incrementWins();   // undo increments
        oPlayer.incrementWins();
        xPlayer.wins = 0;          // reset directly
        oPlayer.wins = 0;
        ties = 0;
    }

    /** Returns a formatted HTML string for display. */
    public String getFormattedScores() {
        return "<html><div style='text-align:center;'>"
             + "🏆 Scores<br><br>"
             + xPlayer.getName() + ": " + xPlayer.getWins() + "<br>"
             + oPlayer.getName() + ": " + oPlayer.getWins() + "<br>"
             + "Ties: " + ties
             + "</div></html>";
    }
}