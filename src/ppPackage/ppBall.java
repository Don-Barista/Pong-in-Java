package ppPackage;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JToggleButton;

import acm.graphics.GOval;
import acm.graphics.GPoint;
import acm.program.GraphicsProgram;
import acm.program.Program;

import static ppPackage.ppSimParams.*;

/**
 * ppBall class contains the simulation loop and the instance variables of the ball. 
 * @author eric, @author professor Ferrie, @author T.A. Poulin
 *
 */
public class ppBall extends Thread {

	//Instance variables
	private double XinitVr; // Initial position of ball - X
	private double YinitVr; // Initial position of ball - Y
	private double Vo; // Initial velocity (Magnitude)
	private double theta; // Initial direction
	private double loss; // Energy loss on collision
	private Color color; // Color of ball
	private GraphicsProgram GProgram; // Instance of ppSim class (this)
	GOval myBall; // Graphics object representing ball
	ppTable myTable; // Instance of ppTable class
	ppPaddle RPaddle; // Instance of ppPaddle class for right variable
	ppPaddle LPaddle; // Instance of ppPaddle class for left paddle
	double X, Xo, Y, Yo;
	double Vx, Vy;
	boolean running;
	/**
	 * Creates a new ppBall object which adds a new GOval object. The constructor copies its parameters to the instance variables above. 
	 * The parameters ppTable and ppPaddle allows for the ppBall object to call methods from those classes.
	 * @param Xinit - Initial X position of the ball
	 * @param Yinit - Initial Y position of the ball
	 * @param Vo - Initial velocity of the ball
	 * @param theta - Initial launch angle of the ball
	 * @param loss - Energy loss per collision
	 * @param color - Color of the ball
	 * @param myTable - A reference to the ppTable instance
	 * @param GProgram - A reference to the ppSim class used to manage the display
	 */
	public ppBall (double Xinit, double Yinit, double Vo, double theta, double loss,
			Color color, ppTable myTable, GraphicsProgram GProgram) {
		this.XinitVr = Xinit;
		this.YinitVr = Yinit;
		this.Vo = Vo;
		this.theta = theta;
		this.loss = loss;
		this.color = color;
		this.GProgram = GProgram;
		this.myTable = myTable;

		myBall = new GOval (XinitVr*Xs, YinitVr*Ys, 2*bSize*Xs,2*bSize*Ys);
		myBall.setColor(color);
		myBall.setFilled(true);
		GProgram.add(myBall);
	}
	/**
	 * Method to plot a dot at the current location in screen coordinates.
	 * @param ScrX - Screen position x of the ball
	 * @param ScrY - Screen position y of the ball
	 */
	private void trace(double ScrX, double ScrY) {
		GOval dot = new GOval(ScrX, ScrY, PD, PD);
		dot.setColor(Color.BLACK);
		dot.setFilled(true);
		GProgram.add(dot);
	}
	/*
	public void setRightPaddle(ppPaddle myPaddle) {
		this.RPaddle = myPaddle;
	}
	 */
	/**
	 * This run method contains the loop of the simulation, meaning the calculations for the
	 * position of the ball and its tracer.
	 */
	public void run() {		
		// Initialize simulation parameters
		// Initialize program variables
		Xo = XinitVr+bSize;	// Set initial X position to constant Xinit
		Yo = YinitVr-bSize;	// Set initial Y position to constant Yinit
		double time = 0; // Time starts at 0 and counts up
		double Vt = bMass*g / (4*Pi*bSize*bSize*k); // Terminal velocity
		double Vox = Vo*Math.cos(theta*Pi/180); // X component of velocity					
		double Voy = Vo*Math.sin((theta*Pi/180)); // Y component of velocity

		// Condition to keep simulation loop running
		boolean running = true;
		
		// Main simulation loop
		while (running) {
			X = Vox*Vt/g*(1-Math.exp(-g*time/Vt));
			Y = Vt/g*(Voy+Vt)*(1-Math.exp(-g*time/Vt))-Vt*time;
			Vx = Vox*Math.exp(-g*time/Vt);
			Vy = (Voy+Vt)*Math.exp(-g*time/Vt)-Vt;

			// Potential and kinetic energy local variables
			double KEx = 0.5*bMass*Vx*Vx*(1-loss);
			double KEy = 0.5*bMass*Vy*Vy*(1-loss);
			double PE = bMass*g*(Y+Yo);

			// Boundary on the Y axis to determine when ball is considered out of bounds
			if(Y+Yo + 2*bSize >= Ymax + 5*bSize) {
				if (Vx > 0) {
					incrementHumanScore();
				}
				else if (Vx < 0) {
					incrementAgentScore();
				}
				running = false;
			}
					
			//Simulation of collision with floor
			if (Y+Yo <= bSize) {
				PE = 0;
				//Reinitialized Vox and Voy with energy loss
				Vox = Math.sqrt(2*KEx/bMass);
				Voy = Math.sqrt(2*KEy/bMass);
				if (Vx<0) Vox = -Vox;

				//Reinitialize the simulation
				time = 0;
				Xo += X; //New Xo is X
				Yo = bSize; //New Yo is height of ball
				X = 0; 
				Y = 0;	

			}

			//Simulation of collision with right wall
			if (X+Xo >= ppPaddleXinit - bSize && Vox>0) {
				if(RPaddle.contact(X+Xo, Y+Yo) && X+Xo <= ppPaddleXinit + bSize) {
					PE = bMass*g*(Y+Yo);

					//Reinitialized Vox and Voy with energy loss
					Vox = -1*Math.sqrt(2*KEx/bMass);
					Voy = Math.sqrt(2*KEy/bMass);
					Vox=Vox*ppPaddleXgain; // Scale X component of velocity
					Voy=Voy*ppPaddleYgain*RPaddle.getV().getY(); // Scale Y + same dir. as paddle

					// Cap the Vox to a set maximum
					if(Vox > Math.abs(5)) Vox = 5;					
					// Cap the Voy to a set maximum
					if(Voy > 8) Voy = 8;
					

					//Reinitialize the simulation
					time = 0;
					Xo = XwallR - bSize; //Accumulate distance between collisions
					Yo = Yo + Y; //New Yo is height of ball
					X = 0; 
					Y = 0;
				}
				
				// If human paddle misses ball, increments agent's score
				// and stops the simulation loop until newServeButton is called
				else if (X+Xo >= ppTableXlen+OFFSET*xs){
					incrementAgentScore();
					running = false;					
				}
			}


			//Simulation of collision with left wall
			if (X+Xo <= LPaddleXinit+ppPaddleW && Vox<0) {
				if (LPaddle.contact(X+Xo, Y+Yo)){
					PE = bMass*g*(Y+Yo);

					//Reinitialized Vox and Voy with energy loss
					Vox = Math.sqrt(2*KEx/bMass);
					if (Vy<=0) 
					Voy = -1*Math.sqrt(2*KEy/bMass);
					else if (Vy>0) 
					Voy = Math.sqrt(2*KEy/bMass);				
					Vox=Vox*LPaddleXgain; // Scale X component of velocity
					Voy=Voy*LPaddleYgain*LPaddle.getV().getY(); // Scale Y + same dir. as paddle
					
					// Cap the Vox to a set maximum
					if(Vox > Math.abs(5)) Vox = 5;
					// Cap the Voy to a set maximum
					if(Voy > 8) Voy = 8;
					
					//Reinitialize the simulation
					time = 0;
					Xo = XwallL + bSize; //New Xo is X
					Yo = Yo + Y; //New Yo is height of ball
					X = 0; 
					Y = 0;
				}
				// If agent paddle misses ball
				else {
					incrementHumanScore();
					running = false;
				}
			}

			// Update and display
			//Display current values (1 time/second)
			if(MESG) System.out.printf("t: %.2f\t\t X: %.2f\t Y: %.2f\t Vx: %.2f\t Vy: %.2f\n",time,X+Xo,Y+Yo,Vx,Vy);


			// Update the position of the ball.  Plot a tick mark at current location.

			GPoint p = myTable.W2S(new GPoint(Xo+X-bSize, Yo+Y+bSize));		// Get current position in screen coordinates
			double ScrX = p.getX();
			double ScrY = p.getY();
			myBall.setLocation(ScrX,ScrY);
			
			// static traceButton has one value shared amongst all classes
			// because of "static". Instance of traceButton in ppSim is the
			// same as in ppBall. Initially false, when traceButton's value 
			// true, the simulation will display the trace of the ball.
			if (traceButton.isSelected())trace(ScrX+(bSize*Xs),ScrY+(bSize*Ys));

			time += TICK;
			// if energy falls lower than threshold value, stop loop
			if ((KEx + KEy + PE) < ETHR) running = false;

			// Pause display
			GProgram.pause(TICK*1000);
		}


}

	/**
	 * Sets the value of the reference to the Human paddle.
	 * @param myPaddle - ref to instance of ppPaddle
	 */
	public void setRightPaddle(ppPaddle myPaddle) {
		this.RPaddle = myPaddle;
	}
	
	/**
	 * Sets the value of the reference to the Agent paddle
	 * @param myPaddle - ref to instance of ppPaddle
	 */
	public void setLeftPaddle(ppPaddle myPaddle) {
		this.LPaddle = myPaddle;
	}

	/**
	 * Gets the absolute position of the Ball in a new GPoint object. X Value
	 * for position on X axis, Y value for the position on Y axis.
	 * @return
	 */
	public GPoint getP() {
		return new GPoint(Xo + X, Y + Yo);
	}

	/**
	 * Gets the velocity of the ball in a new GPoint object. X value
	 * for velocity on X axis, Y value for the velocity on Y axis.
	 * @return
	 */
	public GPoint getV() {
		return new GPoint(Vx, Vy);

	}

	// terminates simulation
	/**
	 * Method to stop the simulation loop when called.
	 */
	void kill() {
		running = false;
	}
	
	/**
	 * Method to increment agent's score.
	 */
	public void incrementAgentScore() {
		agentPoints++;
		agentPointsDisplay = agentPoints.toString();
		agentScore.setText(agentPointsDisplay);
	}
	
	/**
	 * Method to increment human's score.
	 */
	public void incrementHumanScore() {
		humanPoints++;
		humanPointsDisplay = humanPoints.toString();
		humanScore.setText(humanPointsDisplay);
	}
	
	/**
	 * Method to clear the score board.
	 */
	public void clearScore() {
		agentPoints = 0;
		humanPoints = 0;
		agentPointsDisplay = agentPoints.toString();
		humanPointsDisplay = humanPoints.toString();
		agentScore.setText(agentPointsDisplay);
		humanScore.setText(humanPointsDisplay);
	}

}

