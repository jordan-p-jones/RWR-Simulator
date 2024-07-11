package rwr.sim;

import java.util.List;

/**
 * Class to wrap a list of Signal objects into a single
 * object for JSON serialization.
 */
public class SignalListWrapper
{
    private List<Signal> signals;
    
    public SignalListWrapper() {}
    
    /**
     * Construct a SignalListWrapper around a given list of Signals.
     * 
     * @param signals is a list of Signals to wrap.
     */
    public SignalListWrapper( List<Signal> signals )
    {
        this.signals = signals;
    }

    /**
     * Get the signals.
     *
     * @return the signals
     */
    public List<Signal> getSignals()
    {
        return signals;
    }

    /**
     * Set the signals.
     *
     * @param signals the signals to set
     */
    public void setSignals( List<Signal> signals )
    {
        this.signals = signals;
    }
}