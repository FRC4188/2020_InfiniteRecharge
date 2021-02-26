// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.auto.EightBall;
import frc.robot.commands.auto.MoveForward;
import frc.robot.commands.auto.SixBall;
import frc.robot.commands.auto.SkillsChallenge;
import frc.robot.commands.auto.WheelEightBall;
import frc.robot.commands.drive.FollowTrajectory;
import frc.robot.commands.drive.ResetOdometry;
import frc.robot.subsystems.DiffDrive;
import frc.robot.subsystems.Shooter;
import frc.robot.utils.paths.WaypointsList;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  Field2d m_fieldSim;

  private final DiffDrive drivetrain;
  private final Shooter shooter;

  private final Joystick pilot = new Joystick(0);

  private final SendableChooser<SequentialCommandGroup> autoChooser = new SendableChooser<SequentialCommandGroup>();

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer(Field2d m_fieldSim) {
    this.m_fieldSim = m_fieldSim;
    drivetrain = new DiffDrive(m_fieldSim);
    shooter = new Shooter();

    setDefaultCommands();
    configureButtonBindings();
    addAutoCommands();
  }

  private void setDefaultCommands() {
    /*drivetrain.setDefaultCommand(new RunCommand(() -> {
      double volts = SmartDashboard.getNumber("Drivetrain Set Voltage", 0.0);
      drivetrain.setVolts(volts, volts);
    }, drivetrain));*/
    /*drivetrain.setDefaultCommand(new RunCommand(() -> {
      double speed = SmartDashboard.getNumber("Drivetrain Set Speed", 0.0);
      drivetrain.setSpeeds(speed, speed);
    }, drivetrain));*/
    drivetrain.setDefaultCommand(new RunCommand(() -> drivetrain.drive(pilot.getRawAxis(0), pilot.getRawAxis(1)), drivetrain));
    shooter.setDefaultCommand(new RunCommand(() -> shooter.setVelocity(SmartDashboard.getNumber("Shooter set Velocity", 0.0)), shooter));
  }

  public void freeze() {
    drivetrain.setVolts(0.0, 0.0);
  }

  public DiffDrive getDrive() {
    return drivetrain;
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    SmartDashboard.putData("Reset Position", new InstantCommand(() -> drivetrain.resetOdometry(new Pose2d()), drivetrain));
  }

  private void addAutoCommands() {
    autoChooser.setDefaultOption("Do Nothing", null);
    autoChooser.addOption("Six Ball Auto", new SixBall(drivetrain));
    autoChooser.addOption("Eight Ball Auto", new EightBall(drivetrain));
    autoChooser.addOption("Wheel Eight Ball", new WheelEightBall(drivetrain));
    autoChooser.addOption("15 Meters Forward", new MoveForward(drivetrain));
    autoChooser.addOption("Skills Challenge", new SkillsChallenge(drivetrain));

    SmartDashboard.putData("Choose Auto: ", autoChooser);
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return autoChooser.getSelected();
}}
