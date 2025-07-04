// ******** Users Class ************

public class Users {
    private String firstName;
    private char lastInitial;
    private int score;

    public Users(String userFirstName, char userLastInitial, int userScore) {
        firstName = userFirstName;
        lastInitial = userLastInitial;
        score = userScore;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public char getLastInitial() {
        return lastInitial;
    }

    public void setLastInitial(char lastInitial) {
        this.lastInitial = lastInitial;
    }
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "User: " + firstName + lastInitial + " Current Score: "+ score;
    }

}

