package ppPackage;

import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;

/**
 * The PPSimParams class contains the static variables necessary to run the simulation. In order to avoid having to instantiate the same variables every time, 
 * the variables are defined here. 
 * @author eric and Values from 1 to 8 @author professor Ferrie
 *
 */
public class ppSimParams {
	// 1. Parameters defined in screen coordinates (pixels, acm coordinates)
	public static final int WIDTH = 1280; // n.b. screen coordinates
	public static final int HEIGHT = 600;
	public static final int OFFSET = 200;
	
	//2. Ping-pong table parameters
	public static final double ppTableXlen = 2.74; // Length
	public static final double ppTableHgt = 1.52; // Ceiling
	public static final double XwallL = 0.05; // Position of left wall
	public static final double XwallR = 2.69; // Position of right wall

	// 3. Parameters defined in simulation coordinates (MKS, X-->range, Y-->height)

	public static final double g = 9.8; // MKS
	public static final double k = 0.1316; // Vt constant
	public static final double Pi = 3.1416;
	public static final double bSize = 0.02; // pp ball radius
	public static final double bMass = 0.0027; // pp ball mass
	public static final double TICK = 0.01; // Clock tick duration (sec)
	public static final double ETHR = 0.001; // Minimum ball energy
	public static final double Xmin = 0.0; // Minimum value of X (pp table)
	public static final double Xmax = ppTableXlen; // Maximum value of X
	public static final double Ymin = 0.0; // Minimum value of Y
	public static final double Ymax = ppTableHgt; // Maximum value of Y
	public static final int xmin = 0; // Minimum value of x
	public static final int xmax = WIDTH; // Maximum value of x
	public static final int ymin = 0; // Minimum value of y
	public static final int ymax = HEIGHT; // Maximum value of y
	public static final double Xs = (xmax-xmin)/(Xmax-Xmin); // Scale factor to convert world X to screen x
	public static final double Ys = (ymax-ymin)/(Ymax-Ymin); // Scale factor to convert world Y to screen y
	public static final double xs = (Xmax-Xmin)/(xmax-xmin); // Scale factor to convert screen x to world X
	public static final double ys = (Ymax-Ymin)/(ymax-ymin); // Scale factor to convert screen y to world Y
	public static final double Xinit = XwallL; // Initial ball location (X)
	public static final double Yinit = Ymax/2; // Initial ball location (Y)
	public static final double PD = 1; // Trace point diameter
	public static final double TSCALE = 2000; //Scaling parameter for pause()

	// 4. Paddle Parameters
	static final double ppPaddleH = 8*2.54/100; //Paddle height
	static final double ppPaddleW = 0.5*2.54/100; // Paddle width
	static final double ppPaddleXinit = XwallR + ppPaddleW/2; //Initial Paddle X
	static final double ppPaddleYinit = Yinit; // Initial Paddle Y
	static final double ppPaddleXgain = 1.7; // Vx gain on paddle hit
	static final double ppPaddleYgain = 1.0; // Vy gain on paddle hit
	
	// 5. Agent Paddle Parameters
	static final double LPaddleXinit = XwallL + ppPaddleW/2; //Initial X position of Agent paddle
	static final double LPaddleYinit = Yinit; //Initial Y position of Agent paddle
	static final double LPaddleXgain = 1.7; //Corresponding X gain for agent
	static final double LPaddleYgain = 1.0; // Corresponding Y gain for agent
		
	// 6. Parameters used by the ppSim class
	static final double YinitMAX = 0.75*Ymax; // Max inital height at 75% of range
	static final double YinitMIN = 0.25*Ymax; // Min inital height at 25% of range
	static final double EMIN = 0.2; // Minimum loss coefficient
	static final double EMAX = 0.2; // Maximum loss coefficient
	static final double VoMIN = 5.0; // Minimum velocity
	static final double VoMAX = 5.0; // Maximum velocity
	static final double ThetaMIN = 0.0; // Minimum launch angle
	static final double ThetaMAX = 20.0; // Maximum launch angle
	static final long RSEED = 8976232; // Random number gen. seed value
	
	// 7. Gloabl varaibles
	public static JToggleButton traceButton; // Ref. to trace button
	
	// 8. Miscellaneous
	public static final boolean DEBUG = false; // Debug msg. and single step if true
	public static final boolean MESG = true; // Enable status messages on console
	public static final int STARTDELAY = 1000; // Delay between setup and start

	// 9. Constant parameters to make the game playable
	public static final double VoxMin = 0.5;
	
	// 10. Score
	public static Integer agentPoints = 0;
	public static Integer humanPoints = 0;
	public static String agentPointsDisplay = "0";
	public static String humanPointsDisplay = "0";
	public static JLabel agentScore;
	public static JLabel humanScore;
	public static JSlider lagSlider;
}
