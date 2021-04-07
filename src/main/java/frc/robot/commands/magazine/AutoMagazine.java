package frc.robot.commands.magazine;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Magazine;

/**
 * Runs magazine and indexer motors to feed cells if shooter is at correct rpm.
 */
public class AutoMagazine extends CommandBase {

  private final Magazine magazine;
  private final Intake intake;
  private boolean forw;
  private boolean cont;

  private int midCounter = 0;
  private int topCounter = 0;
  private boolean ballInMagazine;

  /**
   * Constructs a new AutoMagazine command to feed cells if shooter is at correct rpm.
   *
   * @param magazine Magazine subsystem to use for turning motors.
   * @param intake Intake subsystem that feeds into the magazine.
   * @param forward Whether the system is taking balls through the regular track or reversed.
   */
  public AutoMagazine(Magazine magazine, Intake intake, boolean forward, boolean cont) {
    addRequirements(magazine, intake);
    this.magazine = magazine;
    this.intake = intake;
    this.forw = forward;
    this.cont = cont;
  }

  @Override
  public void initialize() {
  }

    @Override
  public void execute() {
    boolean top = magazine.topBeamClear();
    boolean mid = magazine.midBeamClear();
    boolean entry = magazine.entryBeamClear();

    /*
    if (forw) {
      if (!mid && top) {
        intake.spin(-1.0, -1.0);
        magazine.set(0.35);  
      } else if (mid && top) {
          magazine.set(0.0);
          intake.spin(-1.0, -1.0);
        }
                else if (!top) {
                    magazine.set(0.0);
                    intake.spin(-1.0,0.0);
                }
            } else {
                if (!mid && top) {
                    intake.spin(1.0, 1.0);
                    magazine.set(-0.1);
                }
                else if (mid && top) {
                    magazine.set(0.0);
                    intake.spin(1.0, 0.5);
                }
                else if (!top) {
                    magazine.set(-0.35);
                    intake.spin(1.0,0.0);
                }
            }
            */
        
        /*if (forw) {
            if (top) {
                if(!entry) magazine.set(0.35);
                else if (!mid) magazine.set(0.175);
                else magazine.set(0.0);
            } else magazine.set(0.0);

            if (top && entry) intake.spin(-1.0, -1.0);
            else intake.spin(-1.0, 0.0);
        } else {
            intake.spin(1.0, 1.0);
            magazine.set(-0.5);
        }*/
    if (forw) {
      if (!mid) midCounter++;
      if (!top) topCounter++;
    
      if ((topCounter + 1) == midCounter) ballInMagazine = true;
      else ballInMagazine = false;
  
      if ((topCounter == midCounter) && !ballInMagazine) {      //ball has been shot
        magazine.set(0.75);
        intake.spin(-1.0, -1.0);
      } /*else if (ballInMagazine) {
        magazine.set(0.75);*/
       else {
        magazine.set(0.0);
        intake.spin(0.0, 0.0);
      }
    }
  }

    @Override
    public boolean isFinished() {
        return !cont;
    }

    @Override
    public void end(boolean interrupted) {
        // Upon stop intake stops and if the magazine is being controlled automatically it will stop.
        magazine.set(0.0);
        intake.spin(0.0, 0.0);
    }

}