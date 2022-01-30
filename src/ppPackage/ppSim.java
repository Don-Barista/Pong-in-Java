package ppPackage;

import acm.graphics.GPoint;
import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;

import static ppPackage.ppSimParams.*;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
/**
 * The ppSim class hosts the simulation of the program. Because of the main method, this class becomes the entry point for the java program.
 * Its through this class that the run method containing the simulation of the ball is run, thanks to the start() method. Unlike in assignment 2, 
 * the initial parameters of the ball are randomly generated. However, with a predefined seed, the outcomes are not random.
 * @author eric and professor @author Ferrie's assignment 4, as well as @author T.A. Poulin
 *
 */
public class ppSim extends GraphicsProgram {

	ppTable myTable;
	ppPaddle myPaddle;
	ppBall myBall;
	ppPaddleAgent LPaddle;

	/**
	 * The main method is the entry point of the java program. 
	 * @param args
	 */
	public static void main(String[] args) {
		new ppSim().start(args);	// this means look in ppSim for public void run method
	}

	/**
	 * This run method is where objects are called using the constructors to be displayed. 
	 * It is void because it doesn't return anything.
	 */
	public void init() {

		// Buttons, JSlider and JToggle on the southern side of the canvas
		JButton newServeButton = new JButton("New Serve");
		JButton clearScoreButton = new JButton("Clear");
		JButton quitButton = new JButton("Quit");
		traceButton = new JToggleButton("Trace", false);
		JLabel lessLag = new JLabel("-Lag");
		lagSlider = new JSlider(0, 10, 5);
		JLabel moreLag = new JLabel("+Lag");
		add(newServeButton, SOUTH);
		add(clearScoreButton, SOUTH);
		add(quitButton, SOUTH);
		add(traceButton, SOUTH);
		add(lessLag, SOUTH);
		add(lagSlider, SOUTH);
		add(moreLag, SOUTH);

		// Scoreboard
		JLabel agentLabel = new JLabel("Agent");
		add(agentLabel, NORTH);
		agentScore = new JLabel(agentPointsDisplay);
		add(agentScore, NORTH);
		JLabel humanLabel = new JLabel("Player");
		add(humanLabel, NORTH);
		humanScore = new JLabel(humanPoints.toString());
		add(humanScore, NORTH);

		this.resize(ppSimParams.WIDTH+OFFSET, ppSimParams.HEIGHT+OFFSET);
		addMouseListeners();
		addActionListeners();

		// Create ppTable instance and RandomGenerator instance
		RandomGenerator rgen = RandomGenerator.getInstance();
		rgen.setSeed(RSEED);

		// Creates a ppTable object using the ppTable constructor
		// and assign it to the instance variable myTable 
		this.myTable = new ppTable(this);

		//this.myBall = newBall();
		newGame();


	}
	
	/**
	 * Method to create a ppBall object equal to the ppBall instance of this class. The arguments in the constructor
	 * for the ppBall object is randomly generated, but the randomly generated values are limited for the purpose
	 * of this assignment.
	 * @return
	 */
	ppBall newBall() {
		// Create ppTable instance and RandomGenerator instance
		RandomGenerator rgen = RandomGenerator.getInstance();
		rgen.setSeed(RSEED);

		// Generate parameters for ppBall
		Color iColor = Color.RED; //Color of the ball
		double iYinit = rgen.nextDouble(YinitMIN, YinitMAX); // Random double between 25% and 75% of table height for initial Y of the ball
		double iLoss = rgen.nextDouble(EMIN, EMAX);	// Random double between 0.2 and 0.2 for energy loss per collision
		double iVel = rgen.nextDouble(VoMIN, VoMAX); // Random double between 5.0 and 5.0 for initial velocity 
		double iTheta = rgen.nextDouble(ThetaMIN, ThetaMAX); // Random double between 0.0 and 20.0 for launch angle

		// Create ball
		return this.myBall = new ppBall(Xinit, iYinit, iVel, iTheta, iLoss, iColor, myTable, this);
	}

	/**
	 * Method to create a new game. It kills the simulation loop in session if there is one, clears the canvas,
	 * and creates new ppPaddle and ppPaddleAgent objects as well as a ppBall object.
	 */
	public void newGame() {
		if (myBall != null) myBall.kill(); // stop current game in play
		myTable.newScreen();
		myBall = newBall();
		myPaddle = new ppPaddle(ppPaddleXinit, ppPaddleYinit, Color.GREEN, myTable, this);
		LPaddle = new ppPaddleAgent(LPaddleXinit, LPaddleYinit, Color.BLUE, myTable, this);
		LPaddle.attachBall(myBall);
		myBall.setRightPaddle(myPaddle);
		myBall.setLeftPaddle(LPaddle);		
		LPaddle.start();
		myPaddle.start();
		pause(STARTDELAY);
		myBall.start();
	}

	/**
	 * Method to retrieve action performed by user's mouse and call the appropriate method depending
	 * on what the user clicked.
	 */
	public void actionPerformed(ActionEvent e) {
		String button = e.getActionCommand();
		if (button.equals("New Serve")){
			newGame();
		}
		if (button.equals("Quit")) {
			System.exit(0);
		}
		if (button.equals("Clear")) {
			myBall.clearScore();
		}
	}

	/**
	 * Method mouseMoved takes the action event of the movement of the user's mouse and uses the 
	 * location of that mouse to set a new location for the ppPaddle object.
	 */
	public void mouseMoved(MouseEvent e) {
		if(myTable == null || myPaddle == null) return;
		GPoint Pm = myTable.S2W(new GPoint(e.getX(), e.getY()));
		double PaddleX = myPaddle.getP().getX();
		double PaddleY = Pm.getY();
		myPaddle.setP(new GPoint(PaddleX, PaddleY));
	}


}

