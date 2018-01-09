
package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Bot;
import org.firstinspires.ftc.teamcode.Enums.MovementEnum;
import org.firstinspires.ftc.teamcode.Enums.ReleasePosition;

@Autonomous(name = "BlueAutonCloseV2", group = "Auton")
//@Disabled
public class BlueAutonCloseV2 extends OpMode {
    Bot robot = new Bot();
    ElapsedTime timer;
    int command = 0;
    //VuforiaLocalizer vuforia;
    //VuforiaTrackables relicTrackables;
    //VuforiaTrackable relicTemplate;
    //RelicRecoveryVuMark vuMark;

    double power;
    int generalTarget;
    boolean hitjewel = false;

    @Override
    public void init() {
        robot.init(hardwareMap);
        timer = new ElapsedTime();
        robot.setDriveMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.releaseMove(ReleasePosition.INIT);
        robot.jewelUp();
        robot.backIntakeWallUp();

        //VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        //parameters.vuforiaLicenseKey = "AbZUuPf/////AAAAGUmS0Chan00iu7rnRhzu63+JgDtPo889M6dNtjvv+WKxiMJ8w2DgSJdM2/zEI+a759I7DlPj++D2Ryr5sEHAg4k1bGKdo3BKtkSeh8hCy78w0SIwoOACschF/ImuyP/V259ytjiFtEF6TX4teE8zYpQZiVkCQy0CmHI9Ymoa7NEvFEqfb3S4P6SicguAtQ2NSLJUX+Fdn49SEJKvpSyhwyjbrinJbak7GWqBHcp7fGh7TNFcfPFMacXg28XxlvVpQaVNgkvuqolN7wkTiR9ZMg6Fnm0zN4Xjr5lRtDHeE51Y0bZoBUbyLWSA+ts3SyDjDPPUU7GMI+Ed/ifb0csVpM12aOiNr8d+HsfF2Frnzrj2";
        //parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
        //vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        //relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        //relicTemplate = relicTrackables.get(0);

        telemetry.addData(">", "Press Play to start");
        telemetry.update();
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        timer.reset();
        robot.jewelOut();
        robot.setDriveMotorModes(DcMotor.RunMode.RUN_USING_ENCODER);
        //relicTrackables.activate();
    }

    @Override
    public void loop() {
        switch (command) {
            /*case -1:
                vuMark = RelicRecoveryVuMark.from(relicTemplate);
                if (vuMark != RelicRecoveryVuMark.UNKNOWN) {
                    command++;
                    telemetry.addData("VuMark", "%s visible", vuMark);
                } else {
                    telemetry.addData("VuMark", "not visible");
                }
                break;*/

            case 0:
                if (timer.milliseconds() > 500){
                    timer.reset();
                    robot.jewelOuter();
                    command++;
                }
                break;

            case 1:
                if (hitjewel) {
                    robot.drive(MovementEnum.STOP);
                    robot.jewelUp();
                    timer.reset();
                    generalTarget = robot.distanceToRevs(50);
                    robot.runToPosition(generalTarget);
                    command++;
                } else if (timer.milliseconds() > 2000) {
                    robot.drive(MovementEnum.STOP);
                    robot.jewelKnockforward();
                    try {Thread.sleep(300);}catch(Exception e){}
                    robot.jewelKnockback();
                    try {Thread.sleep(300);}catch(Exception e){}
                    robot.jewelUp();
                    timer.reset();
                    generalTarget = robot.distanceToRevs(50);
                    robot.runToPosition(generalTarget);
                    command++;
                } else if (robot.colorSensor.blue() >= 1) {
                    hitjewel = true;
                    robot.jewelKnockback();
                } else if (robot.colorSensor.red() >= 1) {
                    hitjewel = true;
                    robot.jewelKnockforward();
                }
                break;

            case 2:
                power = robot.slowDownScale(robot.FL.getCurrentPosition(), robot.FR.getCurrentPosition(), robot.BL.getCurrentPosition(), robot.BR.getCurrentPosition(), generalTarget, generalTarget, generalTarget, generalTarget);
                robot.drive(MovementEnum.FORWARD, power);
                if (power == 0) {
                    robot.drive(MovementEnum.STOP, 0);
                    robot.setDriveMotorModes(DcMotor.RunMode.RUN_USING_ENCODER);
                    timer.reset();
                    command++;
                }
                break;

            case 3:
                if (timer.milliseconds() > 800) {
                    robot.drive(MovementEnum.STOP);
                    timer.reset();
                    generalTarget = robot.distanceToRevs(27);
                    robot.runToPosition(generalTarget);
                    robot.setDriveMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    command++;
                } else {
                    robot.adjustHeading(90, false);
                }
                break;

            case 4:
                if (timer.milliseconds() > 500) {
                    robot.runToPosition(generalTarget);
                    timer.reset();
                    command++;
                }
                break;

            case 5:
                power = robot.slowDownScale(robot.FL.getCurrentPosition(), robot.FR.getCurrentPosition(), robot.BL.getCurrentPosition(), robot.BR.getCurrentPosition(), generalTarget, generalTarget, generalTarget, generalTarget);
                robot.drive(MovementEnum.FORWARD, power * .4);
                if (power == 0 || timer.milliseconds() > 2000) {
                    robot.drive(MovementEnum.STOP, 0);
                    robot.setDriveMotorModes(DcMotor.RunMode.RUN_USING_ENCODER);
                    timer.reset();
                    command++;
                }
                break;

            case 6:
                if (timer.milliseconds() < 3000) {
                    robot.adjustHeading(0, false);
                } else {
                    robot.drive(MovementEnum.STOP);
                    robot.setDriveZeroPowers(DcMotor.ZeroPowerBehavior.BRAKE);
                    timer.reset();
                    command++;
                }
                break;

            case 7:
                robot.relRDrop();
                robot.relLDrop();
                robot.jewelOut();
                robot.intakeDrop.setPower(-1);
                if (timer.milliseconds() > 1000) {
                    robot.intakeDrop.setPower(0);
                    robot.releaseMove(ReleasePosition.MIDDLE);
                    robot.flipUp();
                    robot.jewelUp();
                    robot.setDriveMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    timer.reset();
                    robot.setDriveZeroPowers(DcMotor.ZeroPowerBehavior.FLOAT);
                    command++;
                }
                break;

            case 8:
                if (timer.milliseconds()  > 500) { // may need to be 750
                    timer.reset();
                    command++;
                }
                break;

            case 9:
                robot.flipDown();
                timer.reset();
                generalTarget = -1*robot.distanceToRevs(19);
                robot.runToPosition(generalTarget);
                command++;
                break;

            case 10:
                power = robot.slowDownScale(robot.FL.getCurrentPosition(), robot.FR.getCurrentPosition(), robot.BL.getCurrentPosition(), robot.BR.getCurrentPosition(), generalTarget, generalTarget, generalTarget, generalTarget);
                robot.drive(MovementEnum.BACKWARD, power);
                if (power == 0 || timer.milliseconds() > 2000) {
                    robot.drive(MovementEnum.STOP, 0);
                    robot.setDriveMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    timer.reset();
                    command++;
                }
                break;

            case 11:
                robot.backIntakeWallDown();
                timer.reset();
                command++;
                break;

            case 12:
                if (timer.milliseconds() > 250) {
                    robot.releaseMove(ReleasePosition.UP);
                    timer.milliseconds();
                    command++;
                }
                break;

            case 13:
                if (timer.milliseconds() > 500) {
                    generalTarget = robot.distanceToRevs(20);
                    robot.runToPosition(generalTarget);
                    timer.reset();
                    command++;
                }
                break;

            case 14:
                power = robot.slowDownScale(robot.FL.getCurrentPosition(), robot.FR.getCurrentPosition(), robot.BL.getCurrentPosition(), robot.BR.getCurrentPosition(), generalTarget, generalTarget, generalTarget, generalTarget);
                robot.drive(MovementEnum.FORWARD, power);
                if (power == 0 || timer.milliseconds() > 2000) {
                    robot.drive(MovementEnum.STOP, 0);
                    robot.releaseMove(ReleasePosition.MIDDLE);
                    robot.setDriveMotorModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    timer.reset();
                    command++;
                }
                break;
        }

        telemetry.addData("red", robot.colorSensor.red());
        telemetry.addData("blue", robot.colorSensor.blue());
        telemetry.addData("time", timer.seconds());
        telemetry.addData("command", command);
        telemetry.update();
    }

    @Override
    public void stop() {

    }

}