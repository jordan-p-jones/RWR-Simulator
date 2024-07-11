package rwr.sim;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * POJO class to encapsulate the data necessary to
 * display a contact on the RWR GUI.
 */
@Entity
@Table(name = "RWR_CONTACT")
public class RwrContact
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RWR_CONTACT_ID")
    private long id;
    
    @Column(name = "DIRECTION")
    private int direction;
    
    @Column(name = "SYMBOL")
    private String symbol;
    
    @Column(name = "THREAT_CD")
    private String threatCd;
    
    public RwrContact() {}
    
    /**
     * Get the direction.
     *
     * @return the direction
     */
    public int getDirection()
    {
        return direction;
    }
    
    /**
     * Set the direction.
     *
     * @param direction the direction to set
     */
    public void setDirection( int direction )
    {
        this.direction = direction;
    }
    
    /**
     * Get the symbol.
     *
     * @return the symbol
     */
    public String getSymbol()
    {
        return symbol;
    }
    
    /**
     * Set the symbol.
     *
     * @param symbol the symbol to set
     */
    public void setSymbol( String symbol )
    {
        this.symbol = symbol;
    }

    /**
     * Get the threatCd.
     *
     * @return the threatCd
     */
    public String getThreatCd()
    {
        return threatCd;
    }

    /**
     * Set the threatCd.
     *
     * @param threatCd the threatCd to set
     */
    public void setThreatCd( String threatCd )
    {
        this.threatCd = threatCd;
    }
    
}
