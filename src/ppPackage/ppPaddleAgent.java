package ppPackage;

import static ppPackage.ppSimParams.*;

import java.awt.Color;

import acm.graphics.GPoint;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;

import java.awt.Color;
import acm.graphics.GPoint;
import acm.program.GraphicsProgram;


/**
 * ppPaddleAgent class is a subclass of ppPaddle. It has its own run method, meaning it overrides the run method of the ppPaddle superclass. 
 * The new run method allows for the change in position of the paddle with regards to the Y position of the ball, which changes at a rythm 
 * determined by the AgentLag global variable.
 * @author eric, with help from professor Ferrie and T.A. Poulin. 
 *
 */
public class ppPaddleAgent extends ppPaddle {
	// When a class extends another class, the new class has the instance variables and methods of the extended class
	
	ppBall myBall;
	int AgentLag;
	
	/**
	 * Constructor to create an object on ppPaddleAgent, which is the subclass of ppPaddle. The arguments to construct this object is
	 * the same as the ones to construct a ppPaddle object.
	 * @param X - Center of the paddle along the X axis in world coordinates
	 * @param Y - Center of the paddle along the Y axis in world coordinates
	 * @param myColor - Color of the paddle
	 * @param myTable - A reference to the ppTable instance
	 * @param GProgram - A reference to the ppSim class used to manage the display
	 */
	public ppPaddleAgent(double X, double Y, Color myColor, ppTable myTable, GraphicsProgram GProgram) {
		super(X, Y, myColor, myTable, GProgram);
		
	} 

	/**
	 * Loop that computes the value of the velocity of the ppPaddle agent and makes the paddle move to the Y position
	 * of the ball at a certain rythm. 
	 */
	public void run() {		
		int ballSkip = 0;
		AgentLag = lagSlider.getValue();
		double lastX = X;
		double lastY = Y;
			

		while(true) {			
			GProgram.pause(TICK*TSCALE);			
			Vx=(X-lastX)/(TICK);
			Vy=(Y-lastY)/(TICK);
			lastX=X;
			lastY=Y;
			if (ballSkip++ >= AgentLag) {
				// get the ball Y
				double Y = myBall.getP().getY();
				// set paddle position to that Y
				this.setP(new GPoint(this.getP().getX(), Y));
				ballSkip = 0;
			}
		}
	}
	
	/*
	 * sets the value of ppBall object equal to the ppBall instance of ppPaddleAgent class.
	 */
	public void attachBall(ppBall myBall) {
		this.myBall = myBall;
	}
}
