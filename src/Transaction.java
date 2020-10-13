import java.util.Date;

public class Transaction {
    private int SenderId, RecipientId;
    private Date date;
    private String Location;
    private int Sum;

    public Transaction(int SenderId, int RecipientId, int Sum, String Location){
        this.SenderId = SenderId;
        this.RecipientId = RecipientId;
        this.Sum = Sum;
        this.date = new Date();
        this.Location = Location;
    }

    public int GetSenderId() {
        return SenderId;
    }

    public int GetRecipientId() {
        return RecipientId;
    }

    public Date GetDate() {
        return date;
    }

    public int GetSum() {
        return Sum;
    }

    public String GetLocation() {return Location;}
}
