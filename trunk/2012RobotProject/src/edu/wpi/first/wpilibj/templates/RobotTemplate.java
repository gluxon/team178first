package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.NIVisionException;

public class RobotTemplate extends IterativeRobot
{
    private Joystick joystick;//, joystickAux;
    //private KinectFHS kinect;
    private EnhancedIOFHS enhancedIO;
    private DriverStation driverStation;

    private Drivetrain drivetrain;
    private Tower tower;

	private Sensors sensors;
	//private double ShooterSpeed;
	//private boolean isPressedShooterSpeed;
	//private boolean bridgeDown;

	//private RobotDrive robotDrive;
	//private Victor frontLeft, rearLeft, frontRight, rearRight;

	//private RobotDrive robotDrive;

    //private Sensors sensors;
    private CameraFHS camera;

    private Watchdog watchdog;

    private ImageAnalysis imageAnalysis;
	private DashboardHigh dashboardHigh;

    //double gyroLast;
    //double gyroOffset;

	//boolean hasAnalyzed;
	//boolean isPressedLast;

	int luminosityMin;
	//boolean isPressedLastLuminosityMin;

	double numParticles;

    public void robotInit()
    {
		System.out.println("In robotInit");

		joystick = new Joystick(1);
		//joystickAux  = new Joystick(2);
	//luminosityMin = 130;
	//isPressedLast = false;
	//isPressedLastLuminosityMin = false;
	dashboardHigh = new DashboardHigh();
	sensors = new Sensors();
    //gyroLast = 0.0;
	//gyroOffset = 0.0;

	//compressorToggle = true;
	//isPressedCompressorToggle = false;
	//compressor = new Compressor(5,1);
	//compressor.start();
	//bridgeSolenoid = new Solenoid(2); //slot 3, channel 2
	//intakeSolenoid = new Solenoid(3);
	//ShooterSpeed = 1.0;
	//isPressedShooterSpeed = false;
	//bridgeDown = false;


	//kinect = new KinectFHS(drivetrain);
        driverStation = DriverStation.getInstance();
		enhancedIO = new EnhancedIOFHS(driverStation);

		//robotDrive = new RobotDrive(frontLeft, rearLeft, frontRight, rearRight);
    drivetrain = new Drivetrain(1,2,3,4,joystick,1.0);
    tower = new Tower(driverStation,8,7,6,5,3,enhancedIO);

	//sensors = new Sensors();

    imageAnalysis = new ImageAnalysis(AxisCamera.getInstance());
	camera = new CameraFHS(drivetrain, imageAnalysis);

        watchdog = Watchdog.getInstance();

    }

    public void autonomousPeriodic()
    {
	//kinect.autonomousKinect();

        /*
        double gyroAngle = sensors.getGyro().getAngle()/180;
        drivetrain.frontLeftSet(gyroAngle);
        drivetrain.frontRightSet(gyroAngle);
        drivetrain.rearLeftSet(gyroAngle);
        drivetrain.rearRightSet(gyroAngle);
        */



	/*try
	{
	    camera.centerOnFirstTarget();
	}
	catch (AxisCameraException ex)
	{
	    ex.printStackTrace();
	}
	catch (NIVisionException ex)
	{
	    ex.printStackTrace();
	}

*/
    }

    public void teleopPeriodic()
    {
		drivetrain.drive();
		tower.run();
		dashboardHigh.updateDashboardHigh(drivetrain, 0, sensors.getUltrasonicLeft().getRangeInches(), sensors.getUltrasonicRight().getRangeInches(), 0, luminosityMin, 0, joystick);
		//dashboardHigh.updateDashboardHigh(drivetrain,0.0,0.0,0.0,0.0, luminosityMin, 0,joystick);

		//robotDrive.mecanumDrive_Polar(joystick.getMagnitude(), joystick.getDirectionDegrees(), joystick.getTwist());
		/*
		double numRec;
if(imageAnalysis.getRectangles() == null)
	numRec = -1;
			else
	numRec = imageAnalyisif s.getRectangles().length;
		dashboardHigh.updateDashboardHigh(drivetrain,sensors.getGyro().getAngle(),sensors.getUltrasonicLeft().getRangeInches(),sensors.getUltrasonicRight().getRangeInches(),numRec, luminosityMin, compState,joystick);

		*/

        /*****Debug*****/
/*
		if (joystick.getRawButton(9) && isPressedLastLuminosityMin == false) {
			if (luminosityMin > 0)
				luminosityMin -= 5;
		}

		if (joystick.getRawButton(10) && isPressedLastLuminosityMin == false) {
			if (luminosityMin < 255)
				luminosityMin += 5;
		}

		if(joystick.getRawButton(9) || joystick.getRawButton(10))
			isPressedLastLuminosityMin = true;
		else
			isPressedLastLuminosityMin = false;


        if (joystick.getRawButton(7))
        {
            double gyroAngle = sensors.getGyro().getAngle();
            // Change speed based on Angle
            double speed = Math.abs(gyroAngle / 7);
            System.out.println("Motor Speed: " + speed);

            double deadZone = 1;

            if (gyroAngle < deadZone && gyroAngle > -deadZone)
            {
                System.out.println("Straight: " + gyroAngle);
            }
            else if (gyroAngle > deadZone)
            {
                System.out.println("DOWN: " + gyroAngle);
		drivetrain.frontLeftSet(speed);
		drivetrain.rearLeftSet(speed);
		drivetrain.frontRightSet(-speed);
		drivetrain.rearRightSet(-speed);
            }
            else if (gyroAngle < -deadZone)
            {
                System.out.println("UP: " + gyroAngle);
		drivetrain.frontLeftSet(-speed);
		drivetrain.rearLeftSet(-speed);
		drivetrain.frontRightSet(speed);
                drivetrain.rearRightSet(speed);
            }
        }

        if (joystick.getRawButton(8))
        {
            sensors.getGyro().reset();
        }

	if(joystick.getRawButton(11) && isPressedLast == false)
	{
            try
            {
                camera.centerOnTarget(0,luminosityMin);
            }
            catch (AxisCameraException ex)
            {
            }
            catch (NIVisionException ex)
            {
            }
	}

	if(joystick.getRawButton(11))
		isPressedLast = true;
	else
		isPressedLast = false;

        if(joystick.getRawButton(12))
        {
            try
            {
                imageAnalysis.updateImageTeleop(drivetrain);
                if(imageAnalysis.getRectangles().length > 1)
                {
                    System.out.println("Height:" + imageAnalysis.getHeight(0) + " Distance:" + imageAnalysis.getDistance(0));
                }
            }
            catch (AxisCameraException ex)
            {
            }
            catch (NIVisionException ex)
            {
            }
        }

	if (joystick.getRawButton(12))
        {
            System.out.println(sensors.getUltrasonicLeft().getRangeInches() + " : " + sensors.getUltrasonicRight().getRangeInches());
        }

	*/

	System.out.println("Left: " + sensors.getUltrasonicLeft().getValue());
	System.out.println("Right: " + sensors.getUltrasonicRight().getValue());
	watchdog.feed();
    }
}