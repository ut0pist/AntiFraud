import java.sql.*;

public class AntiFraud {
    private int FraudPoints = 0;

    private Connection connection;
    private ResultSet rs;
    private Statement stmt;

    public AntiFraud(Transaction transaction) throws SQLException {
        EvaluateTransaction(transaction);
    }

    /*public int GetFraudPoints() {
        return FraudPoints;
    }*/

    private void EvaluateTransaction(Transaction transaction) throws SQLException {
        if (CheckBlackList(transaction)) {
            FraudPoints = 30;
            return;
        } else if (CheckWhiteList(transaction)) {
            FraudPoints = 0;
            return;
        } else {
            Rules rules = new Rules(transaction);
            FraudPoints = rules.getFraudPoints();
        }
        System.out.println("FraudPoints = " + FraudPoints);
    }

    public boolean getResult() {
        //boolean legitimacy;
        if (FraudPoints > 25) return false;
        else return true;
    }

    private boolean CheckBlackList(Transaction transaction) throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hakathon?serverTimezone=Europe/Moscow&useSSL=false", "root", "root");
        stmt = connection.createStatement();

        try {
            String Query = "select status from client_info where id_client = " + transaction.GetRecipientId() + " and status is not null";
            rs = stmt.executeQuery(Query);
            rs.next();

            if (rs.getInt(1) == 0) return true;
            else return false;
        } catch (SQLException e) {
            return false;
        }
    }

    private boolean CheckWhiteList(Transaction transaction) throws SQLException {
        //connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hakathon?serverTimezone=Europe/Moscow&useSSL=false", "root", "root");
        boolean result;
        stmt = connection.createStatement();

        try {
            String Query = "select status from client_info where id_client = " + transaction.GetRecipientId() + " and status is not null";
            rs = stmt.executeQuery(Query);
            rs.next();

            if (rs.getBoolean(1) == true) {
                result = true;
            }
            else result = false;
        } catch (SQLException e) {
            result = false;
        }
        connection.close();
        return result;
    }
}
