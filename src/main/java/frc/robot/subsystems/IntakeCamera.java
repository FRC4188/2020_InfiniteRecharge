package frc.robot.subsystems;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode.PixelFormat;
import edu.wpi.first.cameraserver.CameraServer;

public class IntakeCamera {

    public IntakeCamera() {
    }

    public void start() {
        // Creates UsbCamera and MjpegServer [1] and connects them
        CameraServer.getInstance().startAutomaticCapture().setExposureAuto();

        // Creates the CvSink and connects it to the UsbCamera
        CvSink cvSink = CameraServer.getInstance().getVideo();
                
        // Creates the CvSource and MjpegServer [2] and connects them
        CvSource outputStream = CameraServer.getInstance().putVideo("Blur", 640, 480);
    }
}