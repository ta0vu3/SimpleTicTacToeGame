public class Users {
    private final char mark;    // 'X' or 'O'
    private final String name;
    static int wins;

    public Users(String name, char mark) {
        this.name = name;
        this.mark = mark;
        this.wins = 0;
    }

    public String getName() {
        return name;
    }

    public char getMark() {
        return mark;
    }

    public int getWins() {
        return wins;
    }

    public void incrementWins() {
        wins++;
    }
}