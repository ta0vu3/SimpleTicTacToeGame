
// ****** Stats Class **********


import java.util.*;

// KEEPS TRACK OF THE SCORES

public class Stats {
    int currentUsersOnRecord;
    int highScore;
    int userScore;
    Users scoreBoard[];

    // Constructor to build the array
    public Stats(int usersOnBoard) {
        this.currentUsersOnRecord = usersOnBoard;
        scoreBoard = new Users[usersOnBoard];
    }

    public Stats(Users[] scoreBoard) {
        super();
        this.scoreBoard = scoreBoard;
    }

    // CREATE USER IN ARRAY OR ADD NEW SCORE TO CURRENT USER
    public boolean addUser(Users newUser) {
        checkSpaceOnBoard();
        for (int i = 0; i < currentUsersOnRecord; i++) {
            if (newUser.getFirstName().compareToIgnoreCase(scoreBoard[i].getFirstName()) == 0) {
                scoreBoard[i] = newUser;
                return true;
            } else {
                scoreBoard[currentUsersOnRecord] = newUser;
                currentUsersOnRecord++;
            }
        }
        return false;

    }

    // INCREASE BOARD SIZE AS NEEDED
    public void checkSpaceOnBoard() {
        if (currentUsersOnRecord >= scoreBoard.length) {
            Users[] biggerBoard = new Users[scoreBoard.length + 2];
            for (int i = 0; i < currentUsersOnRecord; currentUsersOnRecord++) {
                biggerBoard[i] = scoreBoard[i];
            }
            scoreBoard = biggerBoard;
        }
    }

    // SORT BOARD AND DISPLAY HIGHEST SCORE
    public String highestScore() {
        Arrays.sort(scoreBoard, Collections.reverseOrder());
        return scoreBoard[0].getFirstName() + " has the highest score of "
                + scoreBoard[0].getScore();
    }

    // DISPLAY FULL LEADERBOARD
    @Override
    public String toString() {
        for (int i = 0; i < currentUsersOnRecord; i++) {
            // replace system.out.print with the board in the game
            return (scoreBoard[i].getFirstName() + " "
            + scoreBoard[i].getLastInitial() + "\t "
                    + scoreBoard[i].getScore() + "\n");
        }

        return "Leader Board";
    }

}