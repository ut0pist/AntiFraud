import java.sql.*;


public class Rules {
    private int FraudPoints;
    private Transaction transaction;

    private Connection connection;
    private ResultSet rs;
    private Statement stmt;

    public int getFraudPoints () {return FraudPoints;}

    private void ConnectToDB() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hakathon?serverTimezone=Europe/Moscow&useSSL=false", "root", "root");
        stmt = connection.createStatement();
    }

    public Rules(Transaction transaction) throws SQLException {
        ConnectToDB();
        this.transaction = transaction;
        CallRules();
    }

    // Сравнивает среднюю сумму транзакции с суммой текущей транзакции
    private int SumRule() throws SQLException {
        int points = 0;
        int CurrentSum = transaction.GetSum();
        int CurrentSender = transaction.GetSenderId();
        int CoefficientAvg = 0;
        int Coefficient500 ;

        String Query = "select AVG (sum_transaction) from transaction_info where id_sender = " + CurrentSender;
        rs = stmt.executeQuery(Query);
        rs.next();

        int AvgSum = rs.getInt(1);
        if (AvgSum != 0) {
            CoefficientAvg = CurrentSum / AvgSum;
        }

        Coefficient500 = CurrentSum / 500;
        points = CoefficientAvg + Coefficient500;


        //System.out.println("SumRule: " + points);

        return points;
    }

    //Вычисляет время, которое прошло с момента последней транзакции по сравнению с текущей
    private int TimeSinceLastTransactionsRule() throws SQLException {
        int Points = 0;
        int difference =0;
        String Query = "select dt_transaction from transaction_info where id_sender = " + transaction.GetSenderId() + " order by id_transaction desc limit 1";
        rs = stmt.executeQuery(Query);
        rs.next();

        try {
            difference = (int) ((transaction.GetDate().getTime() - rs.getDate(1).getTime()) / 60000);

            if (difference <= 10) Points = Math.abs(difference - 11);
        }
        catch (SQLException e){

        }

        /*System.out.println("TimeSinceLastTransactions: difference " + difference);
        System.out.println("TimeSinceLastTransactions: " + Points);*/

        return Points;
    }

    //Определяет местоположение отправителя
    private int LocationRule() throws SQLException {
        int points = 0;

        String Query = "select transaction_ip from transaction_info where id_sender = " + transaction.GetSenderId() + " order by id_transaction desc limit 1";
        rs = stmt.executeQuery(Query);
        rs.next();

        try {
            if (rs.getString(1) != transaction.GetLocation()) {
                points = 5;
            }
        }
        catch (SQLException e){

        }

        //System.out.println("Location: " + points);

        return points;
    }

    //Определяет, были ли транзакции между отправителем и получателем когда-либо.
    private int KnownRecipient() throws SQLException {
        int points = 0;

        String Query = "select COUNT(id_transaction) from transaction_info where id_sender = " + transaction.GetSenderId() + " and id_recepient = " + transaction.GetRecipientId();
        rs = stmt.executeQuery(Query);
        rs.next();

        if (rs.getInt(1) == 0) {
            points = 5;
        }

        //System.out.println("KnownRecipient: " + points);

        return points;
    }

    //TODO Add account age rule

    //TODO Add Item category rule


    public void CallRules() throws SQLException {
        FraudPoints = SumRule() + TimeSinceLastTransactionsRule() + LocationRule() + KnownRecipient();
    }
}
