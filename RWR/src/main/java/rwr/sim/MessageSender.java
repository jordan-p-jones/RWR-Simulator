package rwr.sim;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Class to send Signal messages with RabbitMQ.
 */
@Component
public class MessageSender
{
    private final RabbitTemplate rabbitTemplate;
    private final static ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Constructor for a MessageSender given a RabbitTemplate.
     * 
     * @param rabbitTemplate is the RabbitTemplate to use.
     */
    public MessageSender( RabbitTemplate rabbitTemplate ) 
    {
        this.rabbitTemplate = rabbitTemplate;
    }
    
    /**
     * Send a message containing a list of Signals. The message will be encoded
     * as JSON converted to an array of bytes.
     * 
     * @param signals is a list of Signals to send as a message.
     * @throws JsonProcessingException if an error occurs while attempting to convert signal data to JSON.
     * @throws AmqpException if an error occurs while attempting to send a message with AMQP.
     */
    public void sendMessage( List<Signal> signals ) throws JsonProcessingException, AmqpException
    {        
        SignalListWrapper signalWrapper = new SignalListWrapper( signals );
        String json = MessageSender.objectMapper.writeValueAsString( signalWrapper );
        
        this.rabbitTemplate.convertAndSend( 
            RwrApplication.exchangeName, 
            null, 
            json.getBytes( StandardCharsets.UTF_8 ) );
    }
}
