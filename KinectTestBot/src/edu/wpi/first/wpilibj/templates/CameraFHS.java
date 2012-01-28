package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;

public class CameraFHS 
{
    private ImageAnalysis analysis;
    private AxisCamera axis;
    private Drivetrain drivetrain;
    
    public CameraFHS()
    {
	axis = AxisCamera.getInstance();
	drivetrain = Drivetrain.getInstance();
	analysis = new ImageAnalysis(axis);
    }
    
    public void centerOnFirstTarget() throws AxisCameraException, NIVisionException
    {
	analysis.updateImage();
	
	ParticleAnalysisReport[] report = analysis.getValidTargets();
	
	double xNormal;
	
	if(report[0] != null)
	{
	    xNormal = report[0].center_mass_x_normalized;
	
	    drivetrain.frontLeftSet(xNormal*0.5);
	    drivetrain.frontRightSet(xNormal*0.5);
	    drivetrain.rearLeftSet(xNormal*0.5);
	    drivetrain.rearRightSet(xNormal*0.5);
	}
	else
	{
	    System.out.println("No Valid Targets Found!");
	}
    }
    
}
