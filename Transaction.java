import java.util.Date;

/**
 * An immutable, dated record of a single activity on a {@link Client}'s account,
 * such as an order being charged, an invoice, or a payment being received.
 */
public class Transaction
{
    /** Category of the activity, e.g. {@code "ORDER"}, {@code "PAYMENT"}, {@code "INVOICE"}. */
    private final String type;

    /** Human-readable detail describing the activity. */
    private final String description;

    /** Signed amount: credit (positive) or charge (negative). */
    private final double amount;

    /** When the transaction occurred (set at construction). */
    private final Date date;

    /**
     * Creates a transaction time-stamped with the current moment.
     *
     * @param type        the category of activity (e.g. {@code "ORDER"}, {@code "PAYMENT"})
     * @param description a human-readable detail
     * @param amount      the signed amount: positive to credit the client, negative to charge
     */
    public Transaction(String type, String description, double amount)
    {
        this.type = type;
        this.description = description;
        this.amount = amount;
        this.date = new Date();
    }

    /**
     * Returns the category of this transaction.
     *
     * @return the transaction type
     */
    public String getType()
    {
        return type;
    }

    /**
     * Returns the human-readable detail for this transaction.
     *
     * @return the description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Returns the signed amount of this transaction (positive credits the client,
     * negative charges the client).
     *
     * @return the signed amount
     */
    public double getAmount()
    {
        return amount;
    }

    /**
     * Returns the time at which this transaction occurred.
     *
     * @return the transaction date
     */
    public Date getDate()
    {
        return date;
    }

    /**
     * Returns a one-line description of this transaction
     *
     * @return a formatted string with the date, type, description, and amount
     */
    @Override
    public String toString()
    {
        return "[" + date + "] " + type + ": " + description
             + String.format(" ($%.2f)", amount);
    }
}