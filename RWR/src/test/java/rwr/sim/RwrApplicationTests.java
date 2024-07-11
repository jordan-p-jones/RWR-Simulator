package rwr.sim;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Class to run unit tests.
 */
@SpringBootTest()
public class RwrApplicationTests {
    
    /**
     * Validate that Signal objects can be converted to and from their RabbitMQ
     * message format and back again, and that they are still the same once
     * before and after.
     * 
     * @throws IOException if an exception occurs while encoding or decoding
     *         the objects to/from JSON.
     */
    @Test
    void testSignalListEncodeAndDecode() throws IOException
    {
        // Encode and send message
        List<Signal> signalsEncode = Arrays.asList( new Signal( 85, 212, 2 ), new Signal( 185, 268, 6 ) );
        SignalListWrapper signalWrapperEncode = new SignalListWrapper( signalsEncode );
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString( signalWrapperEncode );
        byte[] message = json.getBytes( StandardCharsets.UTF_8 );
        
        // Receive and decode message
        SignalListWrapper signalWrapperDecode = objectMapper.readValue( message, SignalListWrapper.class );
        List<Signal> signalsDecode = signalWrapperDecode.getSignals();
        
        // Validate equality of signals
        for( int i = 0; i < signalsEncode.size(); i++ )
        {
            Signal s1 = signalsEncode.get(i);
            Signal s2 = signalsDecode.get(i);
            Assert.isTrue( s1.getDirection() == s2.getDirection(), "Direction: " + s1.getDirection() + " != " + s2.getDirection() );
            Assert.isTrue( s1.getFrequency() == s2.getFrequency(), "Frequency: " + s1.getFrequency() + " != " + s2.getFrequency() );
            Assert.isTrue( s1.getStrength() == s2.getStrength(), "Strength: " + s1.getStrength() + " != " + s2.getStrength() );
        }        
    }
    
    /**
     * Validates that Signal objects are converted to RwrContact objects correctly.
     */
    @Test
    void testSignalConversionToContact()
    {
        List<Signal> signals = 
            Arrays.asList( 
                    new Signal( 1, 212, 2 ), 
                    new Signal( 2, 241.2, 4 ), 
                    new Signal( 3, 287.5, 7 ), 
                    new Signal( 4, 900, 9 ) );
        List<RwrContact> contacts = SignalInterpreter.convertSignalsToContacts( signals );
        RwrContact c0 = contacts.get(0);
        RwrContact c1 = contacts.get(1);
        RwrContact c2 = contacts.get(2);
        RwrContact c3 = contacts.get(3);
        
        Assert.isTrue( c0.getDirection() == 1, "Contact 0 direction: " + c0.getDirection() + " != 1" );
        Assert.isTrue( c1.getDirection() == 2, "Contact 1 direction: " + c1.getDirection() + " != 2" );
        Assert.isTrue( c2.getDirection() == 3, "Contact 2 direction: " + c2.getDirection() + " != 3" );
        Assert.isTrue( c3.getDirection() == 4, "Contact 3 direction: " + c3.getDirection() + " != 4" );
        
        Assert.isTrue( c0.getSymbol().equals("21"), "Contact 0 symbol: " + c0.getSymbol() + "!= 21" );
        Assert.isTrue( c1.getSymbol().equals("22"), "Contact 1 symbol: " + c1.getSymbol() + "!= 22" );
        Assert.isTrue( c2.getSymbol().equals("25"), "Contact 2 symbol: " + c2.getSymbol() + "!= 25" );
        Assert.isTrue( c3.getSymbol().equals("U"), "Contact 3 symbol: " + c3.getSymbol() + "!= U" );
        
        Assert.isTrue( c0.getThreatCd().equals(Threat.LOW.getCode()), "Contact 0 threat: " + c0.getThreatCd() + " != Threat.LOW" );
        Assert.isTrue( c1.getThreatCd().equals(Threat.HIGH.getCode()), "Contact 1 threat: " + c1.getThreatCd() + " != Threat.HIGH" );
        Assert.isTrue( c2.getThreatCd().equals(Threat.LOCK.getCode()), "Contact 2 threat: " + c2.getThreatCd() + " != Threat.LOCK" );
        Assert.isTrue( c3.getThreatCd().equals(Threat.LAUNCH.getCode()), "Contact 3 threat: " + c3.getThreatCd() + " != Threat.LAUNCH" );
    }

}