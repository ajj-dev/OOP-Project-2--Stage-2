import java.util.Date;

// One client waiting for a quantity of the Product this item belongs to.
// A Product keeps a FIFO list of these; entries are created during ordering
// when stock is insufficient, and cleared as stock is replenished.
public class WaitlistItem
{
    private final String clientID;   // the client who is waiting
    private int quantity;            // how many units they are waiting for
    private final Date date;         // when they joined the waitlist (for FIFO)

    public WaitlistItem(String clientID, int quantity)
    {
        this.clientID = clientID;
        this.quantity = quantity;
        this.date = new Date();
    }

    public String getClientID()
    {
        return clientID;
    }
    public int getQuantity()
    {
        return quantity;
    }
    public Date getDate()
    {
        return date;
    }
    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    public String toString()
    {
        return "Client=" + clientID + ", WaitingQty=" + quantity;
    }
}