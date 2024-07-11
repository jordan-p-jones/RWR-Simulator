package rwr.sim;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for analyzing raw Signal data.
 */
public class SignalInterpreter
{
    /**
     * Interpret the given signal data and convert it into a list of RwrContacts
     * ready for use in the GUI.
     * 
     * @param signals is a list of Signals to interpret.
     * 
     * @return a list of RwrContacts.
     */
    public static List<RwrContact> convertSignalsToContacts( List<Signal> signals )
    {
        List<RwrContact> rwrContacts = new ArrayList<>();
        
        for( Signal signal : signals )
        {
            RwrContact rwrContact = new RwrContact();
            rwrContact.setDirection( signal.getDirection() );
            rwrContact.setSymbol( interpretSignalFrequency( signal.getFrequency() ) );
            rwrContact.setThreatCd( interpretSignalStrength( signal.getStrength() ) );
            rwrContacts.add( rwrContact );
        }
        
        return rwrContacts;
    }
    
    /**
     * Given a frequency value, determine what radar emitter type it corresponds
     * to and return the appropriate symbol for display.
     * 
     * @param frequency represents a signal frequency.
     * 
     * @return a String short code that corresponds to the emitter type.
     */
    private static String interpretSignalFrequency( double frequency )
    {
        String contactSymbol = null;
        
        // Note that these values are totally fictitious and are for
        // demonstration purposes only. Could replace this with a
        // database lookup.
        if( frequency >= 200.0 && frequency <= 210.99 )
        {
            contactSymbol = "19";
        }
        else if( frequency >= 211.3 && frequency <= 230.45 )
        {
            contactSymbol = "21";
        }
        else if( frequency >= 241.1 && frequency <= 249.8 )
        {
            contactSymbol = "22";
        }
        else if( frequency >= 252.0 && frequency <= 261.57 )
        {
            contactSymbol = "23";
        }
        else if( frequency >= 266.7 && frequency <= 272.0 )
        {
            contactSymbol = "24";
        }
        else if( frequency >= 287.0 && frequency <= 299.3 )
        {
            contactSymbol = "25";
        }
        else if( frequency >= 310.0 && frequency <= 320.0 )
        {
            contactSymbol = "50";
        }
        else if( frequency >= 340.6 && frequency <= 351.2 )
        {
            contactSymbol = "Tu";
        }
        else if( frequency >= 380.0 && frequency <= 400.0 )
        {
            contactSymbol = "A";
        }
        else if( frequency >= 500.0 && frequency <= 600.0 )
        {
            contactSymbol = "EW";
        }
        else
        {
            contactSymbol = "U";
        }
        
        return contactSymbol;
    }
    
    /**
     * Given a strength value 1-10, return a corresponding threat value.
     * 
     * @param strength is a value 1-10 that represents the intensity of
     *        signal strength.
     *        
     * @return a threatCd String that can be used directly by the GUI.
     */
    private static String interpretSignalStrength( int strength )
    {
        String threatCd = null;
        
        if( strength >= 9 && strength <= 10 )
        {
            threatCd = Threat.LAUNCH.getCode();
        }
        else if( strength >= 7 )
        {
            threatCd = Threat.LOCK.getCode();
        }
        else if( strength >= 4 )
        {
            threatCd = Threat.HIGH.getCode();
        }
        else
        {
            threatCd = Threat.LOW.getCode();
        }
        
        return threatCd;
    }
}
