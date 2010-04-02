/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;
import edu.fhs.actuators.KickerControl;
import edu.fhs.input.UltrasonicFHS;
import edu.fhs.vision.VisionDirectedDrive;
import edu.wpi.first.wpilibj.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends IterativeRobot
{
    public static final int SLOT_1 = 1;
    public static final int SLOT_8 = 8;
private Joystick joy1;
private Joystick joy2;
private Jaguar leftFrontJag;
private Jaguar rightFrontJag;
private Jaguar leftRearJag;
private Jaguar rightRearJag;
private Victor armWinch;
private RobotDrive drive;
private Relay compressorRelay;
private DigitalInput transducer;
private Solenoid solenoid1;
private Solenoid solenoid2;
private Solenoid solenoid3;  //rename relays later according to usasge (elevator, base roller, etc.)
private Solenoid solenoid4;
private Solenoid armAngleOut;
private Solenoid armAngleIn;
private Solenoid armExtentionOut;
private Solenoid armExtentionIn;
private DriverStationLCD dsout;
private KickerControl kickerControl = new KickerControl();
private AnalogChannel pressure;
private UltrasonicFHS ultrasonicKicker;
private UltrasonicFHS ultrasonicLeft;
private UltrasonicFHS ultrasonicRight;
private Gyro gyro;
private Gyro gyro2;
private int UDelay = 10;
private int IRDelay = 10;
private int kickerDelay = 100;
private int kickerDelay2 = 0;
private boolean kicked = false;
private double psi;
private VisionDirectedDrive vision;
private int fieldPosition;

    /**    *
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit()
    {
        joy1 = new Joystick(1);
        joy2 = new Joystick(2);
        leftFrontJag = new Jaguar(1);
        leftRearJag = new Jaguar(3);
        rightFrontJag = new Jaguar(2)
                {
                  public void set(double d)
                    {
                        super.set(d * -1);
                    }
                };
         rightRearJag = new Jaguar(4)
                {
                  public void set(double d)
                    {
                        super.set(d * -1);
                    }
                };
        armWinch = new Victor(5);
        armAngleOut = new Solenoid(5);
        armAngleIn = new Solenoid(6);
        armExtentionOut = new Solenoid(7);
        armExtentionIn = new Solenoid(8);

        drive = new RobotDrive(leftFrontJag, leftRearJag, rightFrontJag, rightRearJag, 1);
       dsout = DriverStationLCD.getInstance();
        try
        {
            compressorRelay = new Relay(SLOT_8,Relay.Direction.kForward);
            transducer = new DigitalInput(10);
        }
        catch(NullPointerException n)
        {
            dsout.println(DriverStationLCD.Line.kUser3, 0,"ERROR: compressor/regulator not connected");
        }
        if(compressorRelay != null && transducer != null)
        {
                compressorRelay.set(Relay.Value.kOff);
        }
        
        /*
        gyro = new Gyro(1,9);
        axisCamera1 = AxisCamera.getInstance();
        try {
            re1 = new Relay(3);
            re2 = new Relay(4);
            auxDrive = new AuxDriver(joy3);
            ultra1 = new Ultrasonic(5,6);
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Unable to init devices correctly!" );
            e.printStackTrace();
        }
         * */
        
        dsout.updateLCD();
       try
       {
            solenoid1 = new Solenoid(SLOT_8,1);
            solenoid2 = new Solenoid(SLOT_8,2);
            solenoid3 = new Solenoid(SLOT_8,3);
            solenoid4 = new Solenoid(SLOT_8,4);
            kickerControl.setSolenoid1(solenoid1);
            kickerControl.setSolenoid2(solenoid2);
            kickerControl.setSolenoid3(solenoid3);
            kickerControl.setSolenoid4(solenoid4);
       }
        catch(NullPointerException n){
            dsout.println(DriverStationLCD.Line.kUser4, 0,"ERROR: compressor/regulator not connected");
        }

        try
        {
            pressure = new AnalogChannel(SLOT_1,7);
            ultrasonicKicker = new UltrasonicFHS(SLOT_1,8);
            ultrasonicLeft = new UltrasonicFHS(SLOT_1,3);
            ultrasonicRight = new UltrasonicFHS(SLOT_1,4);
            gyro = new Gyro(SLOT_1,1);
            gyro2 = new Gyro(SLOT_1,2);
        }
         catch(NullPointerException n)
         {
             dsout.println(DriverStationLCD.Line.kUser5, 0,"ERROR: compressor/regulator not connected");
         }
            
        if(kickerControl.getSolenoid1() == null || kickerControl.getSolenoid2() == null || kickerControl.getSolenoid3() == null || kickerControl.getSolenoid4() == null)
        {
            dsout.println(DriverStationLCD.Line.kUser6, 1, "solenoids set to null");
        }
        
        dsout.updateLCD();
        vision = new VisionDirectedDrive(gyro, joy1, drive,ultrasonicRight,ultrasonicKicker,kickerControl);
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic()
    {
        fieldPosition = 1;
        if(compressorRelay != null && transducer != null)
        {
            if(!transducer.get())
            {
                compressorRelay.set(Relay.Value.kOn);
            }
            else
            {
                compressorRelay.set(Relay.Value.kOff);
            }
        }

        if(ultrasonicLeft.getRangeInches() > 110)
        {
            leftFrontJag.set(0);
            rightFrontJag.set(0);
            leftRearJag.set(0);
            rightRearJag.set(0);
        }
        else
        {
            if(fieldPosition == 1)
            {
                vision.autonomousCloseZone();
            }
            else if(fieldPosition == 2)
            {
                vision.autonomousMiddleZone();
            }
            else if(fieldPosition == 3)
            {
                vision.autonomousFarZone(); //does nothing
            }
        }

        /*
        leftFrontJag.set(0);
        rightFrontJag.set(0);
        leftRearJag.set(0);
        rightRearJag.set(0);
        /*
        double inputSpeed = 0.5;
        updateDashboard();
        leftFrontJag.set(pitchAdj.gyroAutonomousAngleSpeedAdjust(inputSpeed, isAutonomous(), gyro.getAngle()));
        rightFrontJag.set(pitchAdj.gyroAutonomousAngleSpeedAdjust(inputSpeed, isAutonomous(), gyro.getAngle()));
        leftRearJag.set(pitchAdj.gyroAutonomousAngleSpeedAdjust(inputSpeed, isAutonomous(), gyro.getAngle()));
        rightRearJag.set(pitchAdj.gyroAutonomousAngleSpeedAdjust(inputSpeed, isAutonomous(), gyro.getAngle()));
        */
        
         }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic()
    {

        updateDashboard();
        if(compressorRelay != null && transducer != null)
        {
            if(!transducer.get())
            {
                compressorRelay.set(Relay.Value.kOn);
            }
            else
            {
                compressorRelay.set(Relay.Value.kOff);
            }
        }

        if(compressorRelay != null && joy1.getRawButton(6))
        {
            compressorRelay.set(Relay.Value.kOn);
        }
        if(compressorRelay != null && joy1.getRawButton(5))
        {
            compressorRelay.set(Relay.Value.kOff);
        }
         
        drive.holonomicDrive(joy1.getMagnitude(), joy1.getDirectionDegrees(),joy1.getThrottle());
               
        if(joy2.getRawButton(3))
        {
            armExtentionIn.set(false);
            armExtentionOut.set(true);
        }
        else if(joy2.getRawButton(2))
        {
            armExtentionIn.set(true);
            armExtentionOut.set(false);
        }
        else
        {
            armExtentionIn.set(false);
            armExtentionOut.set(false);
        }
        if(joy2.getRawButton(6))
        {
            armAngleIn.set(false);
            armAngleOut.set(true);
        }
        else if(joy2.getRawButton(7))
        {
            armAngleIn.set(true);
            armAngleOut.set(false);
        }
        else
        {
            armAngleIn.set(false);
            armAngleOut.set(false);
        }
        if(joy2.getTrigger())
        {
            armWinch.set(joy2.getY());
        }
        else
        {
            armWinch.set(0);
        }

        dsout.println(DriverStationLCD.Line.kMain6, 1, "LF " + truncate(leftFrontJag.get()) + " LR " + truncate(leftRearJag.get()));
        dsout.println(DriverStationLCD.Line.kUser2, 1, "RF " + truncate(rightFrontJag.get()) + " RR " + truncate(rightRearJag.get()));
        if(ultrasonicLeft != null && pressure != null)
        {
            if(UDelay == 10)
            {
                psi = pressure.getAverageVoltage()*37.76-32.89;
                UDelay = 0;
            }
            //dsout.println(DriverStationLCD.Line.kUser4, 1, "U voltage: " + ultraV);
            UDelay++;
        }
        if(ultrasonicKicker != null && pressure != null)
        {
            if(IRDelay == 10)
            {
                psi = pressure.getAverageVoltage()*37.76-32.89;
                IRDelay = 0;
            }
            
            IRDelay++;
        }
        dsout.println(DriverStationLCD.Line.kUser4, 1, "gyro1: " + gyro.getAngle());
        dsout.println(DriverStationLCD.Line.kUser5, 1, "gyro2: " + gyro2.getAngle());
        dsout.println(DriverStationLCD.Line.kUser3, 1, "pressure: " + truncate(psi));
        /*
        dsout.println(DriverStationLCD.Line.kUser3, 1, "U range: " + );
        String distanceGivenByUltrasound =  Double.toString(ultrasonic.pidGet());
        dsout.println(DriverStationLCD.Line.kUser2, 1, distanceGivenByUltrasound);
        ultrasonic.pidGet()(String)
         * */
        if((joy1.getRawButton(4) || kicked) && (kickerDelay > 99 || kickerDelay2 > 0))
        {
            kickerControl.getSolenoid2().set(true);
            kickerControl.getSolenoid3().set(true);
            kickerControl.getSolenoid4().set(true);
            kickerDelay = 0;
            kicked = true;
            if(kickerDelay2 > 25)
            {
                kickerDelay2 = 0;
                kicked = false;
            }
            else{kickerDelay2++;}
        }
        else if(joy1.getRawButton(8))
        {
            kickerControl.getSolenoid1().set(true);
            kickerControl.getSolenoid2().set(true);
            kickerControl.getSolenoid3().set(true);
            kickerControl.getSolenoid4().set(true);
        }
        else
        {
             kickerControl.getSolenoid1().set(false);
             kickerControl.getSolenoid2().set(false);
             kickerControl.getSolenoid3().set(false);
             kickerControl.getSolenoid4().set(false);
             kickerDelay++;
        }

        dsout.updateLCD();
    }

    private double truncate(double rawDouble)
    {
        return ((double) ((int) (100 * rawDouble))) / 100;
    }

    void updateDashboard() {
        Dashboard lowDashData = DriverStation.getInstance().getDashboardPackerLow();
        lowDashData.addCluster();
        {
            lowDashData.addCluster();
            {     //analog modules
                lowDashData.addCluster();
                {
                    for (int i = 1; i <= 8; i++) {
                        lowDashData.addFloat((float) AnalogModule.getInstance(1).getAverageVoltage(i));
                    }
                }
                lowDashData.finalizeCluster();
                lowDashData.addCluster();
                {
                    for (int i = 1; i <= 8; i++) {
                        lowDashData.addFloat((float) AnalogModule.getInstance(2).getAverageVoltage(i));
                    }
                }
                lowDashData.finalizeCluster();
            }
            lowDashData.finalizeCluster();

            lowDashData.addCluster();
            { //digital modules
                lowDashData.addCluster();
                {
                    lowDashData.addCluster();
                    {
                        int module = 4;
                        lowDashData.addByte(DigitalModule.getInstance(module).getRelayForward());
                        lowDashData.addByte(DigitalModule.getInstance(module).getRelayForward());
                        lowDashData.addShort(DigitalModule.getInstance(module).getAllDIO());
                        lowDashData.addShort(DigitalModule.getInstance(module).getDIODirection());
                        lowDashData.addCluster();
                        {
                            for (int i = 1; i <= 10; i++) {
                                lowDashData.addByte((byte) DigitalModule.getInstance(module).getPWM(i));
                            }
                        }
                        lowDashData.finalizeCluster();
                    }
                    lowDashData.finalizeCluster();
                }
                lowDashData.finalizeCluster();

                lowDashData.addCluster();
                {
                    lowDashData.addCluster();
                    {
                        int module = 6;
                        lowDashData.addByte(DigitalModule.getInstance(module).getRelayForward());
                        lowDashData.addByte(DigitalModule.getInstance(module).getRelayReverse());
                        lowDashData.addShort(DigitalModule.getInstance(module).getAllDIO());
                        lowDashData.addShort(DigitalModule.getInstance(module).getDIODirection());
                        lowDashData.addCluster();
                        {
                            for (int i = 1; i <= 10; i++) {
                                lowDashData.addByte((byte) DigitalModule.getInstance(module).getPWM(i));
                            }
                        }
                        lowDashData.finalizeCluster();
                    }
                    lowDashData.finalizeCluster();
                }
                lowDashData.finalizeCluster();

            }
            lowDashData.finalizeCluster();

            lowDashData.addByte(Solenoid.getAll());

            lowDashData.addDouble(999.123d);
        }
        lowDashData.finalizeCluster();
        lowDashData.commit();

    }
}