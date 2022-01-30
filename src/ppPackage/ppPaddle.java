package ppPackage;

import static ppPackage.ppSimParams.*;

import java.awt.Color;

import acm.graphics.GPoint;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;

public class ppPaddle extends Thread {	
	
	//Instance variables of the ppPaddle class
	double X;
	double Y;
	double Vx;
	double Vy;
	Color myColor;
	GRect myPaddle;
	ppTable myTable;
	GraphicsProgram GProgram;
	
	//public ppPaddle () {}
	
	/**
	 * Creates a ppPaddle object when called. This constructor assigns its arguments to the classe's instance variables. 
	 * When called, this constructor creates a new GRect object with its X and Y arguments representing the center
	 * of the GRect. 
	 * @param X - Center of the paddle along the X axis in world coordinates
	 * @param Y - Center of the paddle along the Y axis in world coordinates
	 * @param myColor - Color of the paddle
	 * @param myTable - A reference to the ppTable instance
	 * @param GProgram - A reference to the ppSim class used to manage the display
	 */
	public ppPaddle (double X, double Y, Color myColor, ppTable myTable, GraphicsProgram GProgram) {
		this.X = X;
		this.Y = Y;
		this.myColor = myColor;
		this.myTable = myTable;
		this.GProgram = GProgram;

		
		double upperLeftX = X - ppPaddleW/2;
		double upperLeftY = Y + ppPaddleH/2;
		GPoint p = myTable.W2S(new GPoint(upperLeftX, upperLeftY));
		
		double ScrX = p.getX();
		double ScrY = p.getY();
		myPaddle = new GRect(ScrX, ScrY , ppPaddleW*Xs, ppPaddleH*Ys);
		myPaddle.setFilled(true);
		myPaddle.setColor(myColor);
		GProgram.add(myPaddle);
		
	}
	/**
	 * Run method calculates the velocity of the ppPaddle instance in both X and Y axis.
	 */
	public void run() {
		double lastX = X;
		double lastY = Y;
		while (true) {
			Vx=(X-lastX)/(TICK);
			Vy=(Y-lastY)/(TICK);
			lastX=X;
			lastY=Y;
			GProgram.pause(TICK*TSCALE); // Time to mS
			}
		}
	
	/**
	 * Method getV is a getter method that, when called, will return a GPoint containing 
	 * the velocity of the paddle along the X and Y axis.
	 * @return
	 */
	public GPoint getV() {
		GPoint velocity = new GPoint(Vx,Vy);
		return velocity;
	}
	
	/**
	 * Method setP is a setter method that, when called, will set the ppPaddle object to a specific location.
	 * @param P - GPoint location of the paddle
	 */
	public void setP(GPoint P) {
		// update instance variables
		this.X = P.getX();
		this.Y = P.getY();
		
		double upperLeftX = X - ppPaddleW/2;
		double upperLeftY = Y + ppPaddleH/2;
		GPoint p = myTable.W2S(new GPoint(upperLeftX, upperLeftY));
		
		// move the GRect instance
		double ScrX = p.getX();
		double ScrY = p.getY();
		this.myPaddle.setLocation(ScrX,ScrY);
			
	}
	

	
	/**
	 * Method getP is a getter method that, when called, will return a GPoint containing the location of 
	 * the ppPaddle object along the X and Y axis.
	 * @return
	 */
	public GPoint getP() {
		GPoint P = new GPoint(X,Y);
		return P;
	}
	
	/**
	 * Method getSgnVy is a getter method that, when called, will return the sign of the velocity of the ppPaddle
	 * object along the Y axis.
	 * @return
	 */
	public double getSgnVy() {
		if(Vy<0) return -1;
		else return 1;
	}
	/**
	 * Method contact is a boolean method that will return true if an object comes into contact with 
	 * the ppPaddle object.
	 * @param Sx - Value along X axis of an object
	 * @param Sy - Value along Y axis of an object 
	 * @return - true if the Y axis of an object is in between the top and bottom of the ppPaddle object
	 */
	public boolean contact(double Sx, double Sy) {
		// called whenever X + Xo >= myPaddle.getP().getX()-ppPaddleW AND y of ball between y of paddles
		return (Sy >= Y - ppPaddleH/2 && Sy <= Y + ppPaddleH/2);
	}
}
