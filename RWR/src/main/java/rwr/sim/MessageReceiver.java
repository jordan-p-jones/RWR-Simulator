package rwr.sim;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.application.Platform;

/**
 * Class to receive Signal messages with RabbitMQ.
 */
@Component
public class MessageReceiver
{
	private final static ObjectMapper objectMapper = new ObjectMapper();
	private static RwrGUI rwrGUI;
	private SignalEventRepository signalEventRepository;
	
	/**
	 * Receive a message through AMQP given through an array of bytes.
	 * 
	 * @param message is an array of bytes representing a JSON String, 
	 *        which composes a SignalListWrapper. The SignalListWrapper 
	 *        contains the list of Signals ultimately encoded in the message.
	 */
	public void receiveMessage( byte[] message ) 
	{
		Date eventTime = new Date();
		List<Signal> updatedSignals = null;
		List<SignalEvent> signalEvents = null;
	    
	    try
		{
	    	updatedSignals = 
	    		MessageReceiver.objectMapper.readValue( message, SignalListWrapper.class )
	    		.getSignals();
		} 
	    catch (IOException e)
		{
			System.err.println( "Error occurred while attempting to deserialize AMQP message." );
			e.printStackTrace();
		}
		
	    if( updatedSignals != null && updatedSignals.size() > 0 )
	    {
	    	List<RwrContact> rwrContacts = 
    			SignalInterpreter.convertSignalsToContacts( updatedSignals );
	    	
	    	// Must use Platform.runLater to update UI from a non-application thread.
	    	// Otherwise, IllegalStateException will occur.
	    	Platform.runLater(
	    		() -> { MessageReceiver.rwrGUI.refreshRwrContacts( rwrContacts ); }
	    	);
	    	
	    	// Create and save events.
	    	signalEvents = new ArrayList<>();
	    	
	    	for( int i = 0; i < updatedSignals.size(); i++ )
	    	{
	    		SignalEvent signalEvent = new SignalEvent();
	    		signalEvent.setSignal( updatedSignals.get( i ) );
	    		signalEvent.setRwrContact( rwrContacts.get( i ) );
	    		signalEvent.setDetectionTime( eventTime );
	    		signalEvents.add( signalEvent );
	    	}
	    	
	    	this.signalEventRepository.saveAllAndFlush( signalEvents );
	    }
	}
	
	/**
	 * Setter for rwrGUI.
	 * 
	 * @param rwrGUI is the rwrGUI to set.
	 */
	@Autowired
    public void setRwrGUI( RwrGUI rwrGUI )
    {
		MessageReceiver.rwrGUI = rwrGUI;
    }

	/**
	 * Set the signalEventRepository.
	 *
	 * @param signalEventRepository the signalEventRepository to set
	 */
	@Autowired
	public void setSignalEventRepository( SignalEventRepository signalEventRepository )
	{
		this.signalEventRepository = signalEventRepository;
	}
}
