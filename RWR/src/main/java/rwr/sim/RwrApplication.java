package rwr.sim;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.core.JsonProcessingException;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.stage.Stage;

/**
 * Main class to run the application.
 */
@SpringBootApplication
public class RwrApplication extends Application 
{
    public static final String exchangeName = "messageExchange";
    public static final String queueName = "messageQueue";
    private static MessageSender messageSender;
    private static RwrGUI rwrGUI;
    
    /**
     * Starts the application, called from main method.
     * 
     * @param stage is an empty Stage for the GUI.
     */
    @Override
    public void start( Stage stage ) 
    {
        RwrApplication.rwrGUI.createFixedStage( stage );
        
        // Create background task to run simulation work on so that the UI
        // does not block waiting for the JavaFX Application thread.
        Task<Void> task = new Task<>() 
        {
            @Override
            protected Void call() throws Exception 
            {
                List<List<Signal>> simData = createSimData();
                runSimulation( simData );
                return null;
            }
        };
        
        new Thread(task).start();
    }
    
    /**
     * Create the raw Signal data to simulate detecting over a period of time.
     * 
     * @return a 2D list of Signals. Each inner list represents all
     *         the signals detected by a device at a certain point in time.
     *         There is a list of these lists to represent the signal picture
     *         updating and changing as time passes.
     */
    private List<List<Signal>> createSimData()
    {
        List<List<Signal>> simData = new ArrayList<>();
        
        simData.add( new ArrayList<>( Arrays.asList( new Signal( 90, 212, 2 ) ) ) );
        simData.add( new ArrayList<>( Arrays.asList( new Signal( 85, 212, 2 ), new Signal( 185, 268, 6 ) ) ) );
        simData.add( new ArrayList<>( Arrays.asList( new Signal( 80, 212, 3 ), new Signal( 188, 268, 5 ) ) ) );
        simData.add( new ArrayList<>( Arrays.asList( new Signal( 75, 212, 3 ), new Signal( 191, 268, 3 ), new Signal( 300, 510, 1 ) ) ) );
        simData.add( new ArrayList<>( Arrays.asList( new Signal( 70, 212, 6 ), new Signal( 193, 268, 2 ), new Signal( 296, 510, 1 ) ) ) );
        simData.add( new ArrayList<>( Arrays.asList( new Signal( 60, 212, 6 ), new Signal( 194, 268, 1 ), new Signal( 292, 510, 1 ) ) ) );
        simData.add( new ArrayList<>( Arrays.asList( new Signal( 55, 212, 7 ), new Signal( 195, 268, 1 ), new Signal( 288, 510, 1 ) ) ) );
        simData.add( new ArrayList<>( Arrays.asList( new Signal( 54, 212, 8 ), new Signal( 284, 510, 1 ) ) ) );
        simData.add( new ArrayList<>( Arrays.asList( new Signal( 53, 212, 9 ), new Signal( 280, 510, 1 ) ) ) );
        simData.add( new ArrayList<>( Arrays.asList( new Signal( 52, 212, 9 ), new Signal( 278, 510, 1 ) ) ) );
        simData.add( new ArrayList<>( Arrays.asList( new Signal( 50, 212, 9 ), new Signal( 276, 510, 1 ) ) ) );
        
        return simData;
    }
    
    /**
     * Begin the simulation and start sending data.
     * 
     * @param simData is a 2D list of Signals. Each inner list represents all
     *        the signals detected by a device at a certain point in time.
     *        There is a list of these lists to represent the signal picture
     *        updating and changing as time passes.
     */
    private void runSimulation( List<List<Signal>> simData )
    {
        for( List<Signal> signalsAtPointInTime : simData )
        {
            try 
            {
                Thread.sleep( 1000 );
            } 
            catch (InterruptedException ie) 
            {
                Thread.currentThread().interrupt();
            }
            
            try
            {
                RwrApplication.messageSender.sendMessage( signalsAtPointInTime );
            } 
            catch ( JsonProcessingException e )
            {
                System.err.println( "Error occurred while attempting to convert signal data to JSON." );
                e.printStackTrace();
            }
            catch (AmqpException e)
            {
                System.err.println( "Error occurred while attempting to send a message with AMQP." );
                e.printStackTrace();
            }
        }
    }

    /**
     * Main method to run the application.
     * 
     * @param args contains any program arguments given.
     */
    public static void main( String[] args ) 
    {
        ConfigurableApplicationContext appContext = SpringApplication.run( RwrApplication.class, args );
        Application.launch();
        
        // Terminate the application when the UI window is closed.
        appContext.close();
    }
    
    /**
     * Create a Queue and allow its injection to be managed by the Spring container.
     * 
     * @return a Queue.
     */
    @Bean
    protected Queue createQueue() 
    {
        return new Queue( RwrApplication.queueName, false );
    }
    
    /**
     * Create a FanoutExchange and allow its injection to be managed by the Spring container.
     * 
     * @return a FanoutExchange.
     */
    @Bean
    protected FanoutExchange createExchange() 
    {
        return new FanoutExchange( RwrApplication.exchangeName );
    }
    
    /**
     * Creating a Binding between a Queue and a FanoutExchange and allow it to
     * be managed by the Spring container.
     * 
     * @param queue is a Queue to bind to the FanoutExchange.
     * @param exchange is a FanoutExchange to bind to.
     * 
     * @return a new Binding.
     */
    @Bean
    protected Binding createBinding( Queue queue, FanoutExchange exchange ) 
    {
        return BindingBuilder.bind( queue ).to( exchange );
    }
    
    /**
     * Create a SimpleMessageListenerContainer and allow it to be managed by 
     * the Spring container.
     * 
     * @param connectionFactory is a ConnectionFactory.
     * @param listenerAdapter is a MessageListenerAdapter.
     * @return a SimpleMessageListenerContainer.
     */
    @Bean
    protected SimpleMessageListenerContainer createContainer(
        ConnectionFactory connectionFactory,
        MessageListenerAdapter listenerAdapter ) 
    {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory( connectionFactory );
        container.setQueueNames( RwrApplication.queueName );
        container.setMessageListener( listenerAdapter );
        return container;
    }

    /**
     * Create a MessageListenerAdapter which binds to a given MessageReceiver
     * and defines which method should be called when a message is received.
     * Allow injection of the MessageListenerAdapter to be managed by the
     * Spring container.
     * 
     * @param receiver is a MessageReceiver.
     * @return a MessageListenerAdapter.
     */
    @Bean
    protected MessageListenerAdapter createListenerAdapter( MessageReceiver receiver ) 
    {
        return new MessageListenerAdapter( receiver, "receiveMessage" );
    }
    
    /**
     * Set the messageSender using Spring's dependency injection.
     * 
     * @param messageSender is the MessageSender to set.
     */
    @Autowired
    public void setMessageSender( MessageSender messageSender )
    {
        RwrApplication.messageSender = messageSender;
    }
    
    /**
     * Set the rwrGUI using Spring's dependency injection.
     * 
     * @param rwrGUI is the rwrGUI to set.
     */
    @Autowired
    public void setRwrGUI( RwrGUI rwrGUI )
    {
        RwrApplication.rwrGUI = rwrGUI;
    }
    
}
