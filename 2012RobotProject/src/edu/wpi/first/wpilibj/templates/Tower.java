package edu.wpi.first.wpilibj.templates;
//Imports
import edu.wpi.first.wpilibj.*;

public class Tower
{
    //DriverStation
    DriverStation driverStation;

	//Drive Motors
    private Victor bottomShooterMotor, topShooterMotor, ballElevatorMotor, ballIntakeMotor;

	//Solenoids
    public Solenoid bridgeSolenoid;
    private Compressor compressor;

	private Relay cameraLights; //Relay
    private EnhancedIOFHS enhancedIO; //Controls

	//Misc. Variables
    private double shooterSpeed;
    private boolean bridgeDown = false;
    private boolean cameraLightState;

	private boolean firstShooterPress;

    public Tower(DriverStation ds, int bottomShooterMotorN, int topShooterMotorN, int ballElevatorMotorN, int ballIntakeMotorN, int cameraLightN, EnhancedIOFHS IO)
    {
        //DriverStation
        driverStation = ds;
        //Drive Motors
        bottomShooterMotor = new Victor(bottomShooterMotorN);
        topShooterMotor = new Victor(topShooterMotorN);
        ballElevatorMotor = new Victor(ballElevatorMotorN);
        ballIntakeMotor = new Victor(ballIntakeMotorN)
		{
            public void set(double d)
            {
                super.set(d * 0.50);
            }
        };

		//Compressor
        compressor = new Compressor(6,2);
		compressor.start();

        enhancedIO = IO; //Controls
        cameraLights = new Relay(cameraLightN,Relay.Direction.kForward); //Relay
		bridgeSolenoid = new Solenoid(2); //Solenoid
        shooterSpeed = 1.0;
		bridgeDown = false;
		cameraLightState = false;

		firstShooterPress = false;
    }

    public void cameraLightOn() //Turns the camera light on
    {
        cameraLights.set(Relay.Value.kOn);
        cameraLightState = true;
    }
    public void cameraLightOff() //Turns the camera light off
    {
        cameraLights.set(Relay.Value.kOff);
        cameraLightState = false;
    }

	public void startCompressor()
	{
		compressor.start();
	}

	public void stopCompressor()
	{
		compressor.stop();
	}

    public boolean toggleCameraLight() //Toggles the camera light
    {
        if(cameraLightState) {
            cameraLights.set(Relay.Value.kOff);
            cameraLightState = false;
            return cameraLightState;
		}
		else {
            cameraLights.set(Relay.Value.kOn);
            cameraLightState = true;
            return cameraLightState;
        }
    }

    public Victor getBottomShooterMotor() //Gets the bottom shooter Victor
    {
        return bottomShooterMotor;
    }

    public Victor getTopShooterMotor() //Gets the top shooter Victor
    {
        return topShooterMotor;
    }

    public Victor getBallElevatorMotor() //Gets the ball elevator Victor
    {
        return ballElevatorMotor;
    }

    public Victor getBallIntakeMotor() //Gets the ball intake Victor
    {
        return ballIntakeMotor;
    }

    public void setBottomShooterMotor(double input) //Sets the bottom shooter Victor
    {
        bottomShooterMotor.set(input);
    }

    public void setTopShooterMotor(double input) //Sets the top shooter Victor
    {
        topShooterMotor.set(input);
    }

    public void setBallElevator(double input) //Sets the ball elevator Victor
    {
        ballElevatorMotor.set(input);
    }

    public void setBallIntakeMotor(double input) //Sets the ball intake Victor
    {
        ballIntakeMotor.set(input);
    }

    public void setShooterMotors(double input) //Sets the shooter motors Victors
    {
        setBottomShooterMotor(input);
        setTopShooterMotor(-input*0.5);
    }

    public void setShooterSpeed(double input) //Sets the shooter speed
    {
        shooterSpeed = input;
    }

    public void shoot() //Shoots the ball
    {

		/*setShooterSpeed(enhancedIO.getSlider());

		if (firstShooterPress) {
			setShooterMotors(shooterSpeed * 2);
			firstShooterPress = false;
			System.out.println("Shooter First Press.");
		}
		if (!firstShooterPress) {
			setShooterMotors(shooterSpeed);
		}*/

		setShooterSpeed(enhancedIO.getSlider());
		setShooterMotors(shooterSpeed);
    }

    public void run() //Runs the tower
    {
        if(enhancedIO.getFireButton()) {
			firstShooterPress = true;
            shoot();
		}
		else {
			setShooterSpeed(0.0);
			setShooterMotors(shooterSpeed);
		}

		firstShooterPress = false;
        ////////

        if(enhancedIO.getBallElevatorSwitch()[0])
            setBallElevator(1.0);
        else if(enhancedIO.getBallElevatorSwitch()[1])
            setBallElevator(-1.0);
        else
            setBallElevator(0.0);

        ////////

        if(enhancedIO.getBallIntakeSwitch()[0])
            setBallIntakeMotor(.5);
        else if(enhancedIO.getBallIntakeSwitch()[1])
            setBallIntakeMotor(-.5);
        else
            setBallIntakeMotor(0.0);

        ////////

        if(!bridgeDown && enhancedIO.getBridgeManipulatorSwitch()[1])
        {
            bridgeSolenoid.set(true);
            bridgeDown = true;
        }

        if(bridgeDown && enhancedIO.getBridgeManipulatorSwitch()[0])
        {
            bridgeSolenoid.set(false);
            bridgeDown = false;
        }
    }
}
