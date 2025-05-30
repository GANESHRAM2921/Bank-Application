package GUIs;

import db_objs.MyJDBC;
import db_objs.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginGui extends BaseFrame{
    public LoginGui() {
        super("Banking App Login");
    }

    @Override
    protected void addGuiComponents() {
        //title label
        JLabel bankingAppLabel = new JLabel("Bank Login Page");
        bankingAppLabel.setBounds( 0,20, super.getWidth(),40);
        bankingAppLabel.setFont(new Font( "Dialog", Font. BOLD,32));
        bankingAppLabel.setHorizontalAlignment (SwingConstants.CENTER);
        add (bankingAppLabel);
        JLabel usernameLabel = new JLabel ("Username:");

        //username label
        usernameLabel.setBounds(20,120,getWidth()- 30,24);
        usernameLabel.setFont(new Font("Dialog", Font. PLAIN, 20));
        add (usernameLabel);

        //username text field
        JTextField usernameField = new JTextField();
        usernameField.setBounds(20,160, getWidth() - 50, 40);
        usernameField.setFont(new Font("Dialog", Font. PLAIN,28));
        add (usernameField);

        //password lable
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(20,280,getWidth() - 50, 24);
        passwordLabel.setFont(new Font("Dialog", Font. PLAIN, 20 ));
        add (passwordLabel);

        //password textfield
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(20, 320,getWidth() - 50, 40);
        passwordField.setFont(new Font("Dialog", Font. PLAIN,28));
        add(passwordField);

        //login button
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(20, 460,getWidth() - 50, 40);
        loginButton.setFont(new Font("Dialog", Font. BOLD,20));
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName = usernameField.getText();
                String password = String.valueOf(passwordField.getPassword());
                User user = MyJDBC.validateLogin(userName,password);
                if(user != null){
                    LoginGui.this.dispose();
                    BankingGui bankingGui = new BankingGui(user);
                    bankingGui.setVisible(true);
                    JOptionPane.showMessageDialog(bankingGui,"Login Success!");
                }else{
                    JOptionPane.showMessageDialog(LoginGui.this,"Login failed!");
                }
            }
        });
        add(loginButton);

        //regiter link
        JLabel registerLabel = new JLabel("<html><a href=\"#\">Don't have an account?Click here!!!</a></html>");
        registerLabel.setBounds(0, 510,getWidth() - 10, 30);
        registerLabel.setFont(new Font("Dialog", Font.PLAIN,20));
        registerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                LoginGui.this.dispose();
                RegisterGui registerGui = new RegisterGui();
                registerGui.setVisible(true);
            }
        });
        add(registerLabel);
    }
}
