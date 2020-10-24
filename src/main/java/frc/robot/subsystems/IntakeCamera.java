package frc.robot.subsystems;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IntakeCamera extends SubsystemBase {

    public void IntakeCamera() {
        // Creates UsbCamera and MjpegServer [1] and connects them
        UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
        camera.setResolution(640, 480);
    }

    /**
     * Runs every loop.
     */
    public void periodic() {
    }
}