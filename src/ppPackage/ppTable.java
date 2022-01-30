package ppPackage;
import java.awt.Color;
import java.awt.event.MouseEvent;

import acm.graphics.GPoint;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;
import static ppPackage.ppSimParams.*;

/**
 * The ppTable class contains the constructor ppTable that, when called using the proper argument, will create instances of the GRect class,
 * so for the purposes of this assignment, ppTable will only display the floor. 
 * @author eric, professor Ferrie and T.A. Poulin's tutorials
 *
 */
public class ppTable {
	
	GraphicsProgram GProgram;
		/** 
		 * Creates a new ppTable object that, when called, adds GRects in GProgram
		 * @param GProgram Allows the object to be added in the GProgram
		 */
	public ppTable(GraphicsProgram GProgram) {
		this.GProgram = GProgram;
		drawGroundLine();	
	}
	/**
	 * Method to convert from world to screen coordinates.
	 * @param p - GPoint p object 
	 * @return - returns new GPoint object with screen coordinates x and y
	 */
	public GPoint W2S (GPoint p) {
		double X = p.getX();
		double Y = p.getY();
		double x = X*Xs;
		double y = ymax-(Y*Ys);
		return new GPoint(x,y);
	}
	/**
	 * Method to convert from screen to world coordinates.
	 * @param P - GPoint P object
	 * @return - returns new GPoint object with world coordinates X and Y
	 */
	public GPoint S2W (GPoint P) {
		double x = P.getX();
		double y = P.getY();
		double X = x*xs;
		double Y = ppTableHgt - y*ys;
		return new GPoint (X,Y);
	}
	
	/**
	 * Method to remove everything from the GCanvas and draw the ground line.
	 */
	void newScreen() {
		GProgram.removeAll();
		drawGroundLine();
	}
	
	/**
	 * Method to draw the ground line.
	 */
	public void drawGroundLine() {
		GRect floor = new GRect(0, HEIGHT, WIDTH+OFFSET,3); // a thick line
		floor.setColor(Color.BLACK);
		floor.setFilled(true);
		GProgram.add(floor);
	}
}