package GUIs;

import db_objs.MyJDBC;
import db_objs.Transaction;
import db_objs.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;

public class BankAppDialag extends JDialog implements ActionListener {
    private static User user;
    private BankingGui bankingGui;
    private JLabel balanceLabel,enterAmountLabel,enterUserLabel;
    private JTextField enterAmountTextFiled,enterUserField;
    private JButton actionButton;
    private JPanel pastTransactionPanel;
    private ArrayList<Transaction> pastTransactions;

    public BankAppDialag(BankingGui bankingGui,User user,String title) {
        setTitle(title);
        setSize(400,400);
        setModal(true);
        setLocationRelativeTo(bankingGui);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(null);
        this.bankingGui = bankingGui;
        this.user = user;
    }
    public void addCurrentBalanceAmount(){
        //current balance label
        balanceLabel = new JLabel("Balance: ₹" + user.getCurrentBalance());
        balanceLabel.setBounds(0,10,getWidth() - 20,20);
        balanceLabel.setFont(new Font("Dialog",Font.BOLD,16));
        balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(balanceLabel);

        //enter amount label
        enterAmountLabel = new JLabel("Enter Amount:");
        enterAmountLabel.setBounds(0, 50, getWidth() - 20, 20);
        enterAmountLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        enterAmountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(enterAmountLabel);

        //enterAmountLabel text field
        enterAmountTextFiled = new JTextField();
        enterAmountTextFiled.setBounds(15  ,80,getWidth() - 50,40);
        enterAmountTextFiled.setFont(new Font("Dialog",Font.BOLD,20));
        enterAmountTextFiled.setHorizontalAlignment(SwingConstants.CENTER);
        add(enterAmountTextFiled);
    }

    //adding action button to the dialog
    public void addActionButton(String actionButtonType){
        actionButton = new JButton(actionButtonType);
        actionButton.setBounds(15, 300, getWidth() - 50, 40);
        actionButton.setFont(new Font("Dialog", Font.BOLD, 20));
        actionButton.addActionListener(this);
        add(actionButton);
    }

    //adding user field for transfer
    public void addUserField(){
        // enter user label
        enterUserLabel = new JLabel("Enter User:");
        enterUserLabel.setBounds(0, 160, getWidth() - 20, 20);
        enterUserLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        enterUserLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(enterUserLabel);

        // enter user field
        enterUserField = new JTextField();
        enterUserField.setBounds(15, 190, getWidth() - 50, 40);
        enterUserField.setFont(new Font("Dialog", Font.BOLD, 20));
        enterUserField.setHorizontalAlignment(SwingConstants.CENTER);
        add(enterUserField);
    }

    //handling transaction
    private void handleTransacion(String transactionType,float  amount){
        Transaction transaction;
        if(transactionType.equalsIgnoreCase("Deposit")){
            user.setCurrentBalance(user.getCurrentBalance().add(new BigDecimal(amount)));
            transaction = new Transaction(user.getId(),transactionType,new BigDecimal(amount),null);

        }else{
            user.setCurrentBalance(user.getCurrentBalance().subtract(new BigDecimal(amount)));
            transaction = new Transaction(user.getId(),transactionType,new BigDecimal(-amount),null);
        }
        if ((MyJDBC.addTransactionToDatabase(transaction)) && (MyJDBC.updateCurrentBalance(user))) {
            JOptionPane.showMessageDialog(this, transactionType + " Success!");
            resetFieldsAndUpdateBalance();
        }else{
            JOptionPane.showMessageDialog(this, transactionType + " Failed!");
        }
    }

    //reseTting fields
    private void resetFieldsAndUpdateBalance(){
        enterAmountTextFiled.setText("");
        if(enterUserField != null){
            enterUserField.setText("");
        }
        // update balance on dialog
        balanceLabel.setText("Balance: $" + user.getCurrentBalance());
        // update balance on main gui
        bankingGui.getCurrentBalanceField().setText("Balance: $" + user.getCurrentBalance());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonPressed = e.getActionCommand();
        float amount = Float.parseFloat(enterAmountTextFiled.getText());
        if(buttonPressed.equalsIgnoreCase("Deposit")){
            handleTransacion(buttonPressed,amount);
        }else{
            int result = user.getCurrentBalance().compareTo(BigDecimal.valueOf(amount));
            if(result < 0){
                JOptionPane.showMessageDialog(this, "Error: Input value is more than current balance");
                return;
            }
            if(buttonPressed.equalsIgnoreCase("Withdraw")) {
                // we want to handle the deposit transaction
                handleTransacion(buttonPressed, amount);
            }else{
                String transferredUser = enterUserField.getText();
                handleTransfer(user, transferredUser, amount);
            }
        }
    }

    private void handleTransfer(User user, String transferredUser, float amount) {
        if(MyJDBC.transfer(user, transferredUser, amount)){
            // show success dialog
            JOptionPane.showMessageDialog(this, "Transfer Success!");
            resetFieldsAndUpdateBalance();
        }else{
            // show failure dialog
            JOptionPane.showMessageDialog(this, "Transfer Failed...");
        }
    }

    public void addPastTransactionComponents() {
        pastTransactionPanel = new JPanel();

        // make layout 1x1
        pastTransactionPanel.setLayout(new BoxLayout(pastTransactionPanel, BoxLayout.Y_AXIS));

        // add scrollability to the container
        JScrollPane scrollPane = new JScrollPane(pastTransactionPanel);

        // displays the vertical scroll only when it is required
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(0, 20, getWidth() - 15, getHeight() - 80);

        // perform db call to retrieve all of the past transaction and store into array list
        pastTransactions = MyJDBC.getPastTransaction(user);

        // iterate through the list and add to the gui
        for(int i = 0; i < pastTransactions.size(); i++){
            // store current transaction
            Transaction pastTransaction = pastTransactions.get(i);

            // create a container to store an individual transaction
            JPanel pastTransactionContainer = new JPanel();
            pastTransactionContainer.setLayout(new BorderLayout());

            // create transaction type label
            JLabel transactionTypeLabel = new JLabel(pastTransaction.getTransactionType());
            transactionTypeLabel.setFont(new Font("Dialog", Font.BOLD, 20));

            // create transaction amount label
            JLabel transactionAmountLabel = new JLabel(String.valueOf(pastTransaction.getTransactionAmount()));
            transactionAmountLabel.setFont(new Font("Dialog", Font.BOLD, 20));

            // create transaction date label
            JLabel transactionDateLabel = new JLabel(String.valueOf(pastTransaction.getTransactionDate()));
            transactionDateLabel.setFont(new Font("Dialog", Font.BOLD, 20));

            // add to the container
            pastTransactionContainer.add(transactionTypeLabel, BorderLayout.WEST); // place this on the west side
            pastTransactionContainer.add(transactionAmountLabel, BorderLayout.EAST); // place this on the east side
            pastTransactionContainer.add(transactionDateLabel, BorderLayout.SOUTH); // place this on the south side

            // give a white background to each container
            pastTransactionContainer.setBackground(Color.WHITE);

            // give a black border to each transaction container
            pastTransactionContainer.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            // add transaction component to the transaction panel
            pastTransactionPanel.add(pastTransactionContainer);
        }

        // add to the dialog
        add(scrollPane);
    }
}



