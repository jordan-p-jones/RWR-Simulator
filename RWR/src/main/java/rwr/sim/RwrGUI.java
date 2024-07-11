package rwr.sim;

import java.util.List;

import org.springframework.stereotype.Component;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Class to manage display of the GUI and to handle dynamic element updates.
 */
@Component
public class RwrGUI
{
    private Stage stage;
    final static int LINE_SIZE = 3;
    final static int WINDOW_SIZE = 800;
    final static int CROSS_SIZE = 20;
    final static int OUTER_CIRCLE_RADIUS = ( WINDOW_SIZE / 2 ) - 50;
    
    /**
     * Create and draw the GUI elements which will remain fixed throughout
     * the lifetime of the program.
     * 
     * @param initialStage is an empty stage to create elements on.
     */
    public void createFixedStage( Stage initialStage )
    {
        
        this.stage = initialStage;
        this.stage.setTitle("RWR Simulator");
        StackPane root = new StackPane();
        
        // Draw background
        BackgroundFill blackBackgroundFill = new BackgroundFill( Color.BLACK, null, null );
        Background blackBackground = new Background( blackBackgroundFill );
        root.setBackground( blackBackground );
        
        // Draw outer circle
        Circle outerCircle = new Circle( OUTER_CIRCLE_RADIUS );
        outerCircle.setFill( null );
        outerCircle.setStroke( Color.GREEN );
        outerCircle.setStrokeWidth( LINE_SIZE );
        
        // Draw inner circle
        Circle innerCircle = new Circle( outerCircle.getRadius() / 2 );
        innerCircle.setFill( null );
        innerCircle.setStroke( Color.GREEN );
        innerCircle.setStrokeWidth( LINE_SIZE );
        
        // Draw horizontal line of inner cross
        Line hLine = new Line( 
            ( WINDOW_SIZE / 2 ) - CROSS_SIZE, 
            WINDOW_SIZE / 2, 
            ( WINDOW_SIZE / 2 ) + CROSS_SIZE, 
            WINDOW_SIZE / 2 
        );
        hLine.setStroke( Color.GREEN );
        hLine.setStrokeWidth( LINE_SIZE );
        
        // Draw vertical line of inner cross
        Line vLine = 
            new Line( 
                WINDOW_SIZE / 2, 
                ( WINDOW_SIZE / 2 ) - CROSS_SIZE, 
                WINDOW_SIZE / 2, 
                ( WINDOW_SIZE / 2 ) + CROSS_SIZE 
        );
        vLine.setStroke( Color.GREEN );
        vLine.setStrokeWidth( LINE_SIZE );
        
        final int LINE_SEPARATION_DEGREES = 30;
        final int NUM_LINES = 360 / LINE_SEPARATION_DEGREES;
        Pane linesPane = new Pane();
        
        // Draw angled dashes evenly around the outer edge
        for( int i = 1; i <= NUM_LINES; i++ )
        {
            Line line = 
                new Line(
                    this.findXPosOnRing( i * LINE_SEPARATION_DEGREES, 1 ),
                    this.findYPosOnRing( i * LINE_SEPARATION_DEGREES, 1 ),
                    this.findXPosOnRing( i * LINE_SEPARATION_DEGREES, 0.85 ),
                    this.findYPosOnRing( i * LINE_SEPARATION_DEGREES, 0.85 )
            );
            line.setStroke( Color.GREEN );
            line.setStrokeWidth( LINE_SIZE );
            linesPane.getChildren().add( line );
        }
        
        // Empty Pane as last child to hold dynamic elements
        Pane dynamicPane = new Pane();
        
        // The order of items in this list is important for finding them later
        root.getChildren().addAll( outerCircle, innerCircle, hLine, vLine, linesPane, dynamicPane );
        
        Scene scene = new Scene( root, WINDOW_SIZE, WINDOW_SIZE );
        this.stage.setScene( scene );
        this.stage.show();
    }
    
    /**
     * Update the GUI display with a given list of contacts.
     * 
     * @param freshContacts is a list of RwrContacts that should display on the
     *        GUI in the next window update. The list is absolute and is not
     *        relative to any prior window states.
     */
    public void refreshRwrContacts( List<RwrContact> freshContacts )
    {
        // Clear all current contacts
        ObservableList<Node> rootChildren = 
            this.stage.getScene().getRoot().getChildrenUnmodifiable();
        Pane dynamicPane = 
            (Pane) rootChildren.get( rootChildren.size() - 1 );
        dynamicPane.getChildren().clear();
        
        // Display updated state of current contacts        
        for( RwrContact contact : freshContacts )
        {            
            double threatFactor = contact.getThreatCd().equals( Threat.LOW.getCode() ) ? 0.8 : 0.28;
            double xPos = this.findXPosOnRing( contact.getDirection(), threatFactor );
            double yPos = this.findYPosOnRing( contact.getDirection(), threatFactor );
            String contactSymbol = 
                contact.getThreatCd().equals( Threat.LAUNCH.getCode() ) ? "<"+contact.getSymbol()+">" : contact.getSymbol();    
            Text contactText = new Text( xPos, yPos, contactSymbol );
            Color contactColor = 
                contact.getThreatCd().equals( Threat.LOW.getCode() ) || 
                contact.getThreatCd().equals( Threat.HIGH.getCode() ) ? Color.GREEN : Color.RED;
            contactText.setStroke( contactColor );
            contactText.setFill( contactColor );
            contactText.setFont( Font.font( "Consolas", 24 ) );
            dynamicPane.getChildren().add( contactText );
        }        
    }
    
    /**
     * Find the absolute x coordinate within the window to position something
     * at a certain angle in degrees around the outer display circle.
     * 
     * @param angleInDegrees is a value ranging from 0 to 360.
     * 
     * @param distanceFactor is used to position something some distance away
     *        from the display circle. 1.0 means exactly on the circle, while
     *        smaller values move the position further inside the circle.
     *        
     * @return the absolute x coordinate within the window to position something
     *         at a certain angle in degrees around the outer display circle.
     */
    private double findXPosOnRing( double angleInDegrees, double distanceFactor )
    {
        double angleInRadians = Math.toRadians( angleInDegrees );
        return ( WINDOW_SIZE / 2 ) + ( OUTER_CIRCLE_RADIUS * distanceFactor * Math.sin( angleInRadians ) );
    }
    
    /**
     * Find the absolute y coordinate within the window to position something
     * at a certain angle in degrees around the outer display circle.
     * 
     * @param angleInDegrees is a value ranging from 0 to 360.
     * 
     * @param distanceFactor is used to position something some distance away
     *        from the display circle. 1.0 means exactly on the circle, while
     *        smaller values move the position further inside the circle.
     *        
     * @return the absolute y coordinate within the window to position something
     *         at a certain angle in degrees around the outer display circle.
     */
    private double findYPosOnRing( double angleInDegrees, double distanceFactor )
    {
        double angleInRadians = Math.toRadians( angleInDegrees );
        return ( WINDOW_SIZE / 2 ) - ( OUTER_CIRCLE_RADIUS * distanceFactor * Math.cos( angleInRadians ) );
    }
}
