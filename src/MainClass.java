import java.sql.SQLException;

public class MainClass {
    public static void main (String[] args) throws SQLException {
        Transaction transaction = new Transaction(1, 2, 2000, "ip-address");
        AntiFraud antiFraud = new AntiFraud(transaction);

        //Если legitimacy == false, то транзакция запрещена. Иначе - разрешена.
        boolean legitimacy = antiFraud.getResult();
        System.out.println("Transaction is " + legitimacy);
    }
}
