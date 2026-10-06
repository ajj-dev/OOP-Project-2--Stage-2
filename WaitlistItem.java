import java.util.Date;

/** One client waiting for a quantity of the {@link Product} this entry belongs to. */
public class WaitlistItem
{
    /** Id of the client who is waiting for the product. */
    private final String clientID;

    /** Units the client is still waiting for; reduced as the request is filled. */
    private int quantity;

    /** When the client joined this product's waitlist, used for FIFO ordering. */
    private final Date date;

    /**
     * Creates a waitlist entry, time-stamped with the current moment.
     *
     * @param clientID the id of the waiting client
     * @param quantity the number of units the client is waiting for
     */
    public WaitlistItem(String clientID, int quantity)
    {
        this.clientID = clientID;
        this.quantity = quantity;
        this.date = new Date();
    }

    /**
     * Returns the id of the waiting client.
     *
     * @return the client id
     */
    public String getClientID()
    {
        return clientID;
    }

    /**
     * Returns the number of units still being waited for.
     *
     * @return the waiting quantity
     */
    public int getQuantity()
    {
        return quantity;
    }

    /**
     * Returns the time at which the client joined the waitlist.
     *
     * @return the join time, used for FIFO processing
     */
    public Date getDate()
    {
        return date;
    }

    /**
     * Updates the quantity still being waited for, for example after a shipment
     * partially fills the request.
     *
     * @param quantity the new waiting quantity
     */
    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    /**
     * Returns a short human-readable description of this entry,
     *
     * @return a string describing the waiting client and quantity
     */
    @Override
    public String toString()
    {
        return "Client=" + clientID + ", WaitingQty=" + quantity;
    }
}