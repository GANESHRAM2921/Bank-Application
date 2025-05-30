package GUIs;

import db_objs.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BankingGui extends BaseFrame implements ActionListener {
    private JTextField currentBalanceField;
    public JTextField getCurrentBalanceField(){return currentBalanceField;}
    public BankingGui(User user) {
        super("Banking Page",user);
    }

    @Override
    protected void addGuiComponents() {
        String welcome = "<html>"
                + "<body style = 'text-align:center'>" +
                "<b>Hello " + user.getUserName() + "</b><br>" +
                "what would you like to do today?</body></html>";
        JLabel welcomeLabel = new JLabel(welcome);
        welcomeLabel.setBounds(0,20,getWidth() - 10,40);
        welcomeLabel.setFont(new Font("Dialog",Font.PLAIN,16));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(welcomeLabel);

        //currentBalance label
        JLabel currentBalance = new JLabel("Current Balance");
        currentBalance.setBounds(0,80,getWidth() - 10,30);
        currentBalance.setFont(new Font("Dialog",Font.BOLD,22));
        currentBalance.setHorizontalAlignment(SwingConstants.CENTER);
        add(currentBalance);

        //current balance field
        currentBalanceField = new JTextField("$" + user.getCurrentBalance());
        currentBalanceField.setBounds(15,120,getWidth() - 50,40);
        currentBalanceField.setFont(new Font("Dialog",Font.BOLD,28));
        currentBalanceField.setHorizontalAlignment(SwingConstants.RIGHT);
        currentBalanceField.setEditable(false);
        add(currentBalanceField);

        //deposit button
        JButton depositButton = new JButton("Deposit");
        depositButton.setBounds(15, 180,getWidth() - 50, 50);
        depositButton.setFont(new Font("Dialog", Font. BOLD,22));
        depositButton.addActionListener(this);
        add(depositButton);

        //withdraw button
        JButton withdrawButton = new JButton("Withdraw");
        withdrawButton.setBounds(15, 250,getWidth() - 50, 50);
        withdrawButton.setFont(new Font("Dialog", Font. BOLD,22));
        withdrawButton.addActionListener(this);
        add(withdrawButton);

        //past transactions button
        JButton pastTransactionButton = new JButton("Past Transaction");
        pastTransactionButton.setBounds(15, 320,getWidth() - 50, 50);
        pastTransactionButton.setFont(new Font("Dialog", Font. BOLD,22));
        pastTransactionButton.addActionListener(this);
        add(pastTransactionButton);

        //transfer button
        JButton transferButton = new JButton("Transfer");
        transferButton.setBounds(15, 390,getWidth() - 50, 50);
        transferButton.setFont(new Font("Dialog", Font. BOLD,22));
        transferButton.addActionListener(this);
        add(transferButton);

        //log out button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(15, 500,getWidth() - 50, 50);
        logoutButton.setFont(new Font("Dialog", Font. BOLD,22));
        logoutButton.addActionListener(this);
        add(logoutButton);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonPressed = e.getActionCommand();
        if(buttonPressed.equalsIgnoreCase("logout")) {
            BankingGui.this.dispose();
            new LoginGui().setVisible(true);
            return;
        }
        BankAppDialag bankAppDialag  = new BankAppDialag(this,user,buttonPressed);
        if(buttonPressed.equalsIgnoreCase("Deposit") || buttonPressed.equalsIgnoreCase("Withdraw")
                || buttonPressed.equalsIgnoreCase("Transfer")){
            bankAppDialag.addCurrentBalanceAmount();
            bankAppDialag.addActionButton(buttonPressed);
            if(buttonPressed.equalsIgnoreCase("Transfer")){
                bankAppDialag.addUserField();
            }
            bankAppDialag.setVisible(true);
        }else if(buttonPressed.equalsIgnoreCase("Past Transaction")){
            bankAppDialag.addPastTransactionComponents();
        }

        // make the app dialog visible
        bankAppDialag.setVisible(true);

    }
}
