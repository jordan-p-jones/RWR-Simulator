package rwr.sim;

import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 * Class to represent the detection and interpretation of a signal at a 
 * point in time. Mainly used for logging purposes to record what the system
 * did if it were running in real-time.
 */
@Entity
@Table(name = "SIGNAL_EVENT")
public class SignalEvent
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SIGNAL_EVENT_ID")
    private long id;
    
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "SIGNAL_ID")
    private Signal signal;
    
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "RWR_CONTACT_ID")
    private RwrContact rwrContact;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DETECTION_TIME")
    private Date detectionTime;

    /**
     * Get the id.
     *
     * @return the id
     */
    public long getId()
    {
        return id;
    }

    /**
     * Set the id.
     *
     * @param id the id to set
     */
    public void setId( long id )
    {
        this.id = id;
    }

    /**
     * Get the signal.
     *
     * @return the signal
     */
    public Signal getSignal()
    {
        return signal;
    }

    /**
     * Set the signal.
     *
     * @param signal the signal to set
     */
    public void setSignal( Signal signal )
    {
        this.signal = signal;
    }

    /**
     * Get the rwrContact.
     *
     * @return the rwrContact
     */
    public RwrContact getRwrContact()
    {
        return rwrContact;
    }

    /**
     * Set the rwrContact.
     *
     * @param rwrContact the rwrContact to set
     */
    public void setRwrContact( RwrContact rwrContact )
    {
        this.rwrContact = rwrContact;
    }

    /**
     * Get the detectionTime.
     *
     * @return the detectionTime
     */
    public Date getDetectionTime()
    {
        return detectionTime;
    }

    /**
     * Set the detectionTime.
     *
     * @param detectionTime the detectionTime to set
     */
    public void setDetectionTime( Date detectionTime )
    {
        this.detectionTime = detectionTime;
    }
    
}
