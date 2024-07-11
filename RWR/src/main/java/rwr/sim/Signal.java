package rwr.sim;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * POJO class to represent a simplified model of raw
 * signal data.
 */
@Entity
@Table(name = "SIGNAL")
public class Signal
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SIGNAL_ID")
    private long id;
    
    @Column(name = "DIRECTION")
    private int direction;
    
    @Column(name = "FREQUENCY")
    private double frequency;
    
    @Column(name = "STRENGTH")
    private int strength;
    
    public Signal() {}
    
    /**
     * Constructor for a Signal given a direction, frequency, and
     * strength.
     * 
     * @param direction is a value from 0-360 that represents the direction in degrees.
     * @param frequency represents the signal frequency.
     * @param strength is a value from 1-10 that represents the signal strength.
     */
    public Signal( int direction, double frequency, int strength )
    {
        this.direction = direction;
        this.frequency = frequency;
        this.strength = strength;
    }

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
     * Get the frequency.
     *
     * @return the frequency
     */
    public double getFrequency()
    {
        return frequency;
    }
    
    /**
     * Set the frequency.
     *
     * @param frequency the frequency to set
     */
    public void setFrequency( double frequency )
    {
        this.frequency = frequency;
    }
    
    /**
     * Get the strength.
     *
     * @return the strength
     */
    public int getStrength()
    {
        return strength;
    }
    
    /**
     * Set the strength.
     *
     * @param strength the strength to set
     */
    public void setStrength( int strength )
    {
        this.strength = strength;
    }
}
