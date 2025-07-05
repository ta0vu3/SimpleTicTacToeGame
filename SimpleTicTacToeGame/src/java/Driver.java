// ********************************** Driver Class **********************
//package fp;

import javax.swing.*;

public class Driver {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SimpleTicTacToeGame().setVisible(true);
        });
    }
}