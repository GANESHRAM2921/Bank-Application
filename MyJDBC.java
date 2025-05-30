package db_objs;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

public class MyJDBC {
    //database connection attributes
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/bank_app";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "9384485782";

    //validating user id return user obj if it true
    public static User validateLogin(String username,String password){
        try {
            //establishing connection
            Connection connection = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM users WHERE username = ? AND password = ?");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2,password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                int userId = resultSet.getInt("id");
                BigDecimal currentBalance = resultSet.getBigDecimal("current_Balance");
                return new User(userId,username,password,currentBalance);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    //register new user return boolean value
    public static boolean register(String username,String password){
        if(!check(username)){
            try {
                //establishing connection
                Connection connection = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO users(username, password, current_balance)" + "values(?, ?, ?)");
                preparedStatement.setString(1,username);
                preparedStatement.setString(2,password);
                preparedStatement.setBigDecimal(3,new BigDecimal(0));
                preparedStatement.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    //checking if username already taken return boolean value
    private static boolean check(String username){
        try {
            //establishing connection
            Connection connection = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM users WHERE username = ?");
            preparedStatement.setString(1,username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(!resultSet.next())
                return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    //update transaction database
    public static boolean addTransactionToDatabase(Transaction transaction){
        try {
            //establishing connection
            Connection connection = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);
            PreparedStatement insertTransaction = connection.prepareStatement(
                    "INSERT transactions(user_id, transaction_amount, transaction_date, transaction_type)" +
                    "VALUES(?, ? ,NOW(),?)");
            insertTransaction.setInt(1,transaction.getUserId());
            insertTransaction.setBigDecimal(2,transaction.getTransactionAmount());
            insertTransaction.setString(3,transaction.getTransactionType());
            insertTransaction.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //updateCurrentBalance
    public static boolean updateCurrentBalance(User user){
        try {
            //establishing connection
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            PreparedStatement updateBalance = connection.prepareStatement(
                    "UPDATE users SET current_balance = ? WHERE username = ?");
            updateBalance.setBigDecimal(1,user.getCurrentBalance());
            updateBalance.setString(2,user.getUserName());
            updateBalance.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static boolean transfer(User user,String transferUsername,float transferAmount){
        try{
            //establishing connection
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            PreparedStatement querUser = connection.prepareStatement(
                    "SELECT * FROM users WHERE username = ?");
            querUser.setString(1,transferUsername);
            ResultSet resultSet = querUser.executeQuery();
            while (resultSet.next()){
                User transferedUser = new User(resultSet.getInt("id"),
                        transferUsername,
                        resultSet.getString("password"),
                        resultSet.getBigDecimal("current_balance"));
                Transaction transferTransaction = new Transaction(
                        user.getId(),
                        "Transfer",
                        new BigDecimal(-transferAmount),
                        null
                );
                Transaction receivedTransaction = new Transaction(
                        transferedUser.getId(),
                        "Transfer",
                        new BigDecimal(transferAmount),
                        null
                );
                transferedUser.setCurrentBalance(transferedUser.getCurrentBalance().add(BigDecimal.valueOf(transferAmount)));
                updateCurrentBalance(transferedUser);
                user.setCurrentBalance(transferedUser.getCurrentBalance().subtract(BigDecimal.valueOf(transferAmount)));
                updateCurrentBalance(transferedUser);
                addTransactionToDatabase(transferTransaction);
                addTransactionToDatabase(receivedTransaction);
                return true;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public static ArrayList<Transaction> getPastTransaction(User user) {
        ArrayList<Transaction> pastTransactions = new ArrayList<>();
        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement selectAllTransaction = connection.prepareStatement(
                    "SELECT * FROM transactions WHERE user_id = ?"
            );
            selectAllTransaction.setInt(1, user.getId());

            ResultSet resultSet = selectAllTransaction.executeQuery();

            // iterate throught the results (if any)
            while(resultSet.next()){
                // create transaction obj
                Transaction transaction = new Transaction(
                        user.getId(),
                        resultSet.getString("transaction_type"),
                        resultSet.getBigDecimal("transaction_amount"),
                        resultSet.getDate("transaction_date")
                );

                // store into array list
                pastTransactions.add(transaction);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }

        return pastTransactions;
    }
}
