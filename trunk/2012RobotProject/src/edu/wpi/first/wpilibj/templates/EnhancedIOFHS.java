package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DriverStationEnhancedIO;
import edu.wpi.first.wpilibj.DriverStationEnhancedIO.EnhancedIOException;

public class EnhancedIOFHS 
{
    DriverStationEnhancedIO enhancedIO;
    
    public EnhancedIOFHS(DriverStationEnhancedIO enhancedION)
    {
        enhancedIO = enhancedION;
    }
    
    public DriverStationEnhancedIO getEnhancedIO()
    {
        return enhancedIO;
    }
    
    public double getSlider()
    {
        try 
        {
            return enhancedIO.getAnalogIn(1);
        } 
        catch (EnhancedIOException ex) 
        {
            ex.printStackTrace();
            return -1.0;
        }
    }
    
    public boolean getFireButton()
    {
        try
        {
            return enhancedIO.getDigital(1);
        }
        catch (EnhancedIOException ex)
        {
            ex.printStackTrace();
            return false;
        }
    }
    
    public boolean getCompressorSwitch()
    {
        try
        {
            return enhancedIO.getDigital(2);
        }
        catch (EnhancedIOException ex)
        {
            ex.printStackTrace();
            return false;
        }
    }
    
    public boolean[] getBallIntakeSwitch()
    {
        try
        {
            boolean[] bool = new boolean[2];
            
            bool[0] = enhancedIO.getDigital(3);
            bool[1] = enhancedIO.getDigital(4);
            
            return bool;
        }
        catch (EnhancedIOException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }
    
    public boolean[] getBallElevatorSwitch()
    {
        try
        {
            boolean[] bool = new boolean[2];
            
            bool[0] = enhancedIO.getDigital(5);
            bool[1] = enhancedIO.getDigital(6);
            
            return bool;
        }
        catch (EnhancedIOException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }
}
