package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Magazine extends SubsystemBase{

    private CANSparkMax magMotor = new CANSparkMax(4, MotorType.kBrushless);
    private CANEncoder magEncoder = new CANEncoder(magMotor);
    double speed;

    public Magazine(){
        configMotors();
    }

    public void updateShuffleboard(){
        SmartDashboard.putNumber("Magazine temp", magMotor.getMotorTemperature());
        SmartDashboard.putNumber("Magazine velocity", magEncoder.getVelocity());
        SmartDashboard.putNumber("Magazine speed", getSpeed());
    }

    public void configMotors(){
        magMotor.setClosedLoopRampRate(0.5);
        magMotor.setOpenLoopRampRate(0.5);
    }

    @Override
    public void periodic(){
        updateShuffleboard();
    }

    public void setSpeed(double speed){
        this.speed = speed;
    }

    public double getSpeed(){
        return speed;
    }

    /**
     * Sets belt motor to a given percentage [-1.0, 1.0].
     */
    public void set(double percent){
        magMotor.set(percent);
    }
}