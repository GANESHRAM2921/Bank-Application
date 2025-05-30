package GUIs;

import db_objs.MyJDBC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegisterGui extends BaseFrame{
    public RegisterGui() {
        super("Account Register Page");
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
        passwordLabel.setBounds(20,220,getWidth() - 50, 24);
        passwordLabel.setFont(new Font("Dialog", Font. PLAIN, 20 ));
        add (passwordLabel);

        //password textfield
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(20, 260,getWidth() - 50, 40);
        passwordField.setFont(new Font("Dialog", Font. PLAIN,28));
        add(passwordField);

        //rePassword lable
        JLabel rePassword = new JLabel("Retype Password:");
        rePassword.setBounds(20,320,getWidth() - 50, 40 );
        rePassword.setFont(new Font("Dialog", Font. PLAIN, 20 ));
        add (rePassword);

        //re-password textfield
        JPasswordField rePasswordField = new JPasswordField();
        rePasswordField.setBounds(20, 360,getWidth() - 50, 40);
        rePasswordField.setFont(new Font("Dialog", Font. PLAIN,28));
        add(rePasswordField);

        //regiter button
        JButton regiter = new JButton("Regiter");
        regiter.setBounds(20, 460,getWidth() - 50, 40);
        regiter.setFont(new Font("Dialog", Font. BOLD,20));
        regiter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = String.valueOf(passwordField.getPassword());
                String rePassword = String.valueOf(rePasswordField.getPassword());
                if(validateInput(username,password,rePassword)) {
                    if(MyJDBC.register(username,password)){
                        RegisterGui.this.dispose();
                        LoginGui loginGui = new LoginGui();
                        loginGui.setVisible(true);
                        JOptionPane.showMessageDialog(loginGui,"Registration success!");
                    }else{
                        JOptionPane.showMessageDialog(RegisterGui.this,"Error: username is already taken");
                    }
                }else{
                    JOptionPane.showMessageDialog(RegisterGui.this,
                            "Error: Password must be at least 6 characters\n"+
                            "and/or Password must be same");
                }
            }
        });
        add(regiter);

        //login link
        JLabel loginLabel = new JLabel("<html><a href=\"#\">Have an account? Sign-in here</a></html>");
        loginLabel.setBounds(0, 510,getWidth() - 10, 30);
        loginLabel.setFont(new Font("Dialog", Font.PLAIN,20));
        loginLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                RegisterGui.this.dispose();
                LoginGui loginGui = new LoginGui();
                loginGui.setVisible(true);
            }
        });
        add(loginLabel);

    }

    //validate user inputs
    private boolean validateInput(String username,String password,String rePassword){
        if(username.length() == 0 || password.length() == 0 || rePassword.length() == 0) return false;
        if(password.length() < 6) return false;
        if(!password.equals(rePassword)) return false;
        return true;
    }
}
