import java.util.Date;

// An immutable, dated record of activity on a client's account
// (e.g. an order placed, an invoice, a payment). Held by Client.
public class Transaction
{
    private final String type;        // e.g. "ORDER", "PAYMENT", "INVOICE"
    private final String description; // human-readable detail
    private final double amount;      // signed: credit(+) / charge(-); 0 if not monetary
    private final Date date;

    public Transaction(String type, String description, double amount)
    {
        this.type = type;
        this.description = description;
        this.amount = amount;
        this.date = new Date();
    }

    public String getType()
    {
        return type;
    }
    public String getDescription()
    {
        return description;
    }
    public double getAmount()
    {
        return amount;
    }
    public Date getDate()
    {
        return date;
    }

    public String toString()
    {
        return "[" + date + "] " + type + ": " + description
             + String.format(" ($%.2f)", amount);
    }
}