import GUIs.BankingGui;
import GUIs.LoginGui;
import GUIs.RegisterGui;
import db_objs.User;

import javax.swing.*;
import java.math.BigDecimal;

public class AppLauncher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginGui().setVisible(true);
            }
        });
    }
}