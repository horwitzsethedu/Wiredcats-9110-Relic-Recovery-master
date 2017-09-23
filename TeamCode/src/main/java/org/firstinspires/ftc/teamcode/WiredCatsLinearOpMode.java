package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * Created by Juan Velasquez on 9/18/2017.
 */

public abstract class WiredcatsLinearOpMode extends LinearOpMode {

    //Drive Train
    public DcMotor frontLeft;
    public DcMotor frontRight;
    public DcMotor backLeft;
    public DcMotor backRight;

    //Intake
    public DcMotor intakeLeft;
    public DcMotor intakeRight;

    // Glyph Lift
    public DcMotor glyphter;

    //Relic Lift
    public DcMotor relicLift;

    //Clamp Servos
    public Servo leftClamp;
    public Servo rightClamp;

    //Jewel Servo
    public Servo jewel;

    //Timer
    public ElapsedTime runtime = new ElapsedTime();

    //Color Sensor
    public ColorSensor jewelCS;

    //Gyros
    public ModernRoboticsI2cGyro gyroCW;
    public ModernRoboticsI2cGyro gyroCCW;

    //ODS
    public OpticalDistanceSensor glyph;

    //Touch Sensor
    public TouchSensor right;
    public TouchSensor left;

    float lefty = gamepad1.left_stick_y;
    float leftx = gamepad1.left_stick_x;
    float rightx = gamepad1.right_stick_x;

    double fl = lefty + leftx + rightx;
    double fr = lefty + -leftx + -rightx;
    double bl = lefty + -leftx + rightx;
    double br = lefty + leftx + -rightx;

    double flp = Range.clip(fl, -1, 1);
    double frp = Range.clip(fr, -1, 1);
    double blp = Range.clip(bl, -1, 1);
    double brp = Range.clip(br, -1, 1);

    //Vuforia Setup
    public static final String TAG = "Vuforia VuMark Sample";

    OpenGLMatrix lastLocation = null;

    VuforiaLocalizer vuforia;

    public void initialize() {

        //Drive Train
        frontLeft = hardwareMap.dcMotor.get("fl");
        frontRight = hardwareMap.dcMotor.get("fr");
        backLeft = hardwareMap.dcMotor.get("bl");
        backRight = hardwareMap.dcMotor.get("br");

        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        //Intake
        intakeLeft = hardwareMap.dcMotor.get("il");
        intakeRight = hardwareMap.dcMotor.get("ir");

        intakeLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        //Glyph Lift
        glyphter = hardwareMap.dcMotor.get("gl");

        glyphter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        glyphter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //Relic Lift
        relicLift = hardwareMap.dcMotor.get("rl");

        //Clamp Servos
        leftClamp = hardwareMap.servo.get("lc");
        rightClamp = hardwareMap.servo.get("rc");

        //Jewel Servo
        jewel = hardwareMap.servo.get("j");

        //Color Sensor
        jewelCS = hardwareMap.colorSensor.get("jcs");

        //Gyros
        gyroCW = (ModernRoboticsI2cGyro) hardwareMap.gyroSensor.get("gcw");
        gyroCCW = (ModernRoboticsI2cGyro) hardwareMap.gyroSensor.get("gccw");
        gyroCCW.setI2cAddress(I2cAddr.create8bit(0x5a));

        gyroCW.calibrate();
        gyroCCW.calibrate();

        //ODS
        glyph = hardwareMap.opticalDistanceSensor.get("g");

        //Touch Sensors

        right = hardwareMap.touchSensor.get("r");
        left = hardwareMap.touchSensor.get("l");

        //Vuforia Setup
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        parameters.vuforiaLicenseKey = "AVPejl3/////AAAAGWSyhkBAQkDRnvXeL3i+1HFQBFmB3JIQ9LxjRIYgAGkfX0vAJcEMSoCmNmoVBti30U6L3gHSQQblRnqy4fUv2msd7a/XMUFBK3/MGEvVrUpXyGwIS0o22xE/ypsXa+bhBFkju3VwLmy8d2aj4TAIPghhOsM7ayDt37y/08OJLts08eE3UDu0rne9YM/IfnPnI3yHfk2Q3hXdb7S8Q0JP9ZCmD2n0Fjv7+hgcjE6o6I0WXWlByOVwCqqEKQpPAGBSbMV36PBekzHH4P2t2cDEro5HFfDxYFK00R40yvdL+SOxTvhrMStRK4rDID/oOr2g5V+nDl89R7JD72Taj9myrE97wZoTc0GR+/LckbbKN2N1";

        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate");

        sleep(2000);
        telemetry.addData("Initialization ", "complete");
        telemetry.update();

    }

    public void GyroCW(int angle) throws InterruptedException {

        telemetry();

        int Heading = gyroCW.getHeading();

        if ((angle * 2) - Heading * 2 >= 10) {

            turnRight(0.15);

        }
        else if ((angle * 2) - Heading * 2 <= -10){

            turnLeft(0.15);

        }
        else if (((angle*2) - Heading * 2) <= 10 || ((angle*2) - Heading * 2 >= -10)){

            stopDrive();

        }

    }

    public void GyroCCW(int angle) throws InterruptedException {

        telemetry();

        int Heading = gyroCCW.getHeading();

        if ((angle * 2) - Heading * 2 >= 10) {

            turnRight(0.15);

        }
        else if ((angle * 2) - Heading * 2 <= -10){

            turnLeft(0.15);

        }
        else if (((angle*2) - Heading * 2) <= 10 || ((angle*2) - Heading * 2 >= -10)){

            stopDrive();

        }

    }

    public String getColor(ColorSensor jewelCS) throws InterruptedException{

        if (jewelCS.blue() > jewelCS.red() + 2){

            return "blue";

        }
        else if (jewelCS.red() > jewelCS.blue() + 2){

            return "red";

        }
        else{

            return "other";

        }

    }

    public void knockJewel(String colorStr) throws InterruptedException {

        runtime.reset();

        while (opModeIsActive() && runtime.seconds() < 5) {

            if (getColor(jewelCS).equals(colorStr)) {

                encFB(0.05, 0.05, 100, 100, 2);
                sleep(100);
                encFB(-0.05, 0.05, 75, 75, 2);

            }
            else{

                encFB(-0.05, -0.05, 100, 100, 2);
                sleep(100);
                encFB(0.05, 0.05, 75, 75 , 2);

            }

            stopDrive();

        }
    }

    public void turnLeft(double speed) throws InterruptedException {

        telemetry();
        frontLeft.setPower(-speed);
        frontRight.setPower(speed);
        backLeft.setPower(-speed);
        backRight.setPower(speed);

    }

    public void turnRight(double speed) throws InterruptedException {

        telemetry();
        frontLeft.setPower(speed);
        frontRight.setPower(-speed);
        backLeft.setPower(speed);
        backRight.setPower(-speed);

    }

    public void stopDrive() throws InterruptedException{

        telemetry();
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);

    }

    public void stopAll() throws InterruptedException {

        telemetry();
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
        intakeLeft.setPower(0);
        intakeRight.setPower(0);
        glyphter.setPower(0);
        relicLift.setPower(0);

    }

    //Forwards is 1.0, Backwards is -1.0
    public void encFB(double leftSpeed, double rightSpeed, double leftCounts, double rightCounts, double timeoutS) throws InterruptedException {

        resetEncoders();

        if (opModeIsActive()){

            runtime.reset();
            frontLeft.setPower(leftSpeed);
            frontRight.setPower(rightSpeed);
            backLeft.setPower(leftSpeed);
            backRight.setPower(rightSpeed);

            while (opModeIsActive() && (runtime.seconds() < timeoutS)){

                telemetry();
                if (Math.abs(frontLeft.getCurrentPosition()) > Math.abs(leftCounts) * 7/8 || Math.abs(frontRight.getCurrentPosition()) > Math.abs(rightCounts) * 7/8 || Math.abs(backLeft.getCurrentPosition()) > Math.abs(leftCounts) * 7/8 || Math.abs(backRight.getCurrentPosition()) > Math.abs(rightCounts) * 7/8){

                    frontLeft.setPower(leftSpeed * 0.5);
                    frontRight.setPower(rightSpeed * 0.5);
                    backLeft.setPower(leftSpeed * 0.5);
                    backRight.setPower(rightSpeed * 0.5);

                }
                else if (Math.abs(frontLeft.getCurrentPosition()) > Math.abs(leftCounts) * 0.5 || Math.abs(frontRight.getCurrentPosition()) > Math.abs(rightCounts) * 0.5 || Math.abs(backLeft.getCurrentPosition()) > Math.abs(leftCounts) * 0.5 || Math.abs(backRight.getCurrentPosition()) > Math.abs(rightCounts) * 0.5){

                    frontLeft.setPower(leftSpeed * 0.75);
                    frontRight.setPower(rightSpeed * 0.75);
                    backLeft.setPower(leftSpeed * 0.75);
                    backRight.setPower(rightSpeed * 0.75);

                }
                else {
                    telemetry.addData("speed",1);
                }
                if (Math.abs(frontLeft.getCurrentPosition()) > Math.abs(leftCounts) || Math.abs(frontRight.getCurrentPosition()) > Math.abs(rightCounts) || Math.abs(backLeft.getCurrentPosition()) > Math.abs(leftCounts) || Math.abs(backRight.getCurrentPosition()) > Math.abs(rightCounts)){

                    stopDrive();
                    break;

                }
                idle();

            }
            stopAll();

            resetEncoders();

        }

    }

    //Left is 1.0, Right is -1.0
    public void encLR(double leftSpeed, double rightSpeed, double leftCounts, double rightCounts, double timeoutS) throws InterruptedException {

        resetEncoders();

        if (opModeIsActive()){

            runtime.reset();
            frontLeft.setPower(leftSpeed);
            frontRight.setPower(rightSpeed);
            backLeft.setPower(leftSpeed);
            backRight.setPower(rightSpeed);

            while (opModeIsActive() && (runtime.seconds() < timeoutS)){

                telemetry();
                if (Math.abs(frontLeft.getCurrentPosition()) > Math.abs(rightCounts) * 7/8 || Math.abs(frontRight.getCurrentPosition()) > Math.abs(rightCounts) * 7/8 || Math.abs(backLeft.getCurrentPosition()) > Math.abs(leftCounts) * 7/8 || Math.abs(backRight.getCurrentPosition()) > Math.abs(leftCounts) * 7/8){

                    frontLeft.setPower(rightSpeed * 0.5);
                    frontRight.setPower(rightSpeed * 0.5);
                    backLeft.setPower(leftSpeed * 0.5);
                    backRight.setPower(leftSpeed * 0.5);

                }
                else if (Math.abs(frontLeft.getCurrentPosition()) > Math.abs(rightCounts) * 0.5 || Math.abs(frontRight.getCurrentPosition()) > Math.abs(rightCounts) * 0.5 || Math.abs(backLeft.getCurrentPosition()) > Math.abs(leftCounts) * 0.5 || Math.abs(backRight.getCurrentPosition()) > Math.abs(leftCounts) * 0.5){

                    frontLeft.setPower(rightSpeed * 0.75);
                    frontRight.setPower(rightSpeed * 0.75);
                    backLeft.setPower(leftSpeed * 0.75);
                    backRight.setPower(leftSpeed * 0.75);

                }
                else {
                    telemetry.addData("speed",1);
                }
                if (Math.abs(frontLeft.getCurrentPosition()) > Math.abs(rightCounts) || Math.abs(frontRight.getCurrentPosition()) > Math.abs(rightCounts) || Math.abs(backLeft.getCurrentPosition()) > Math.abs(leftCounts) || Math.abs(backRight.getCurrentPosition()) > Math.abs(leftCounts)){

                    stopDrive();
                    break;

                }
                idle();

            }
            stopAll();

            resetEncoders();

        }

    }

    //DTL = Diagonal Top Left. Top Left is 1.0, Bottom Right is -1.0
    public void encDTL(double leftSpeed, double rightSpeed, double leftCounts, double rightCounts, double timeoutS) throws InterruptedException {

        resetEncoders();

        if (opModeIsActive()){

            runtime.reset();

            frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

            frontLeft.setPower(0);
            frontRight.setPower(rightSpeed);
            backLeft.setPower(leftSpeed);
            backRight.setPower(0);

            while (opModeIsActive() && (runtime.seconds() < timeoutS)){

                telemetry();
                if (Math.abs(frontRight.getCurrentPosition()) > Math.abs(rightCounts) * 7/8 || Math.abs(backLeft.getCurrentPosition()) > Math.abs(leftCounts) * 7/8){

                    frontLeft.setPower(0);
                    frontRight.setPower(rightSpeed * 0.5);
                    backLeft.setPower(leftSpeed * 0.5);
                    backRight.setPower(0);

                }
                else if (Math.abs(frontRight.getCurrentPosition()) > Math.abs(rightCounts) * 0.5 || Math.abs(backLeft.getCurrentPosition()) > Math.abs(leftCounts) * 0.5){

                    frontLeft.setPower(0);
                    frontRight.setPower(rightSpeed * 0.75);
                    backLeft.setPower(leftSpeed * 0.75);
                    backRight.setPower(0);

                }
                else {
                    telemetry.addData("speed",1);
                }
                if (Math.abs(frontRight.getCurrentPosition()) > Math.abs(rightCounts) || Math.abs(backLeft.getCurrentPosition()) > Math.abs(leftCounts)){

                    stopDrive();
                    break;

                }
                idle();

            }
            stopAll();

            frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            resetEncoders();

        }

    }

    //DTR = Diagonal Top Right. Top Right is 1.0, Bottom Left is -1.0
    public void encDTR(double leftSpeed, double rightSpeed, double leftCounts, double rightCounts, double timeoutS) throws InterruptedException {

        resetEncoders();

        if (opModeIsActive()){

            runtime.reset();

            frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

            frontLeft.setPower(leftSpeed);
            frontRight.setPower(0);
            backLeft.setPower(0);
            backRight.setPower(rightSpeed);

            while (opModeIsActive() && (runtime.seconds() < timeoutS)){

                telemetry();
                if (Math.abs(frontLeft.getCurrentPosition()) > Math.abs(rightCounts) * 7/8 || Math.abs(backRight.getCurrentPosition()) > Math.abs(leftCounts) * 7/8){

                    frontLeft.setPower(leftSpeed * 0.5);
                    frontRight.setPower(0);
                    backLeft.setPower(0);
                    backRight.setPower(rightSpeed * 0.5);

                }
                else if (Math.abs(frontLeft.getCurrentPosition()) > Math.abs(rightCounts) * 0.5 || Math.abs(backRight.getCurrentPosition()) > Math.abs(leftCounts) * 0.5){

                    frontLeft.setPower(leftSpeed * 0.75);
                    frontRight.setPower(0);
                    backLeft.setPower(0);
                    backRight.setPower(rightSpeed * 0.75);

                }
                else {
                    telemetry.addData("speed",1);
                }
                if (Math.abs(frontLeft.getCurrentPosition()) > Math.abs(rightCounts) || Math.abs(backRight.getCurrentPosition()) > Math.abs(leftCounts)){

                    stopDrive();
                    break;

                }
                idle();

            }
            stopAll();

            frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            resetEncoders();

        }

    }

    public void resetEncoders() {

        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        glyphter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        glyphter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    public void zeroEncoders(){

        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        glyphter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        glyphter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }

    public void straight(double speed){

        while (opModeIsActive()){

            frontLeft.setPower(speed);
            frontRight.setPower(speed);
            backLeft.setPower(speed);
            backRight.setPower(speed);

        }

        idle();

    }

    public void backwards(double speed){

        while (opModeIsActive()){

            frontLeft.setPower(-speed);
            frontRight.setPower(-speed);
            backLeft.setPower(-speed);
            backRight.setPower(-speed);

        }

        idle();

    }

    public void left(double speed){

        while (opModeIsActive()){

            frontLeft.setPower(-speed);
            frontRight.setPower(-speed);
            backLeft.setPower(speed);
            backRight.setPower(speed);

        }

        idle();

    }

    public void right(double speed){

        while (opModeIsActive()){

            frontLeft.setPower(speed);
            frontRight.setPower(speed);
            backLeft.setPower(-speed);
            backRight.setPower(-speed);

        }

        idle();

    }

    public void intake(){

        int a = 0;

        if (gamepad1.a && a == 0){

            a = 1;

        }
        else if (!gamepad1.a && a == 1 && glyph.getLightDetected() < 0.5){

            intakeLeft.setPower(0.5);
            intakeRight.setPower(0.5);
            a = 2;

        }
        else if (!gamepad1.a && a == 1 && glyph.getLightDetected() >= 0.5){

            intakeLeft.setPower(0.0);
            intakeRight.setPower(0.0);
            a = 0;

        }
        else if(gamepad1.a && a == 2 && glyph.getLightDetected() >= 0.5){

            a = 3;

        }
        else if (!gamepad1.a && a == 2 && glyph.getLightDetected() >= 0.5){

            a = 3;

        }
        else if (!gamepad1.a && a == 3 && glyph.getLightDetected() >= 0.5){

            intakeLeft.setPower(0.0);
            intakeRight.setPower(0.0);
            a = 0;

        }

    }

    public void outtake() throws InterruptedException{

        if (opModeIsActive()) {

            intakeLeft.setPower(0.5);
            intakeRight.setPower(0.5);

        }

    }

    public void glyphterUp() throws InterruptedException{

        resetEncoders();

        if (opModeIsActive()){

            runtime.reset();

            while (runtime.seconds() < 3){

                telemetry();

                if (glyphter.getCurrentPosition() < 500){

                    glyphter.setPower(0.8);

                }
                else if (glyphter.getCurrentPosition() >= 500){

                    glyphter.setPower(0.0);

                }

                idle();

            }

            stopAll();

            resetEncoders();

        }

    }

    public void glyphterDown() throws InterruptedException{

        resetEncoders();

    }

    public void driveToRedCryptobox() throws InterruptedException{

        if (!right.isPressed()){

            left(0.5);

        }
        else{

            stopDrive();

        }

    }

    public void driveToBlueCryptobox() throws InterruptedException{

        if (!left.isPressed()){

            right(0.5);

        }
        else{

            stopDrive();

        }

    }

    public void telemetry() {

        try {

            telemetry.addData("color", getColor(jewelCS));

        } catch (InterruptedException e) {

            telemetry.addData("GyroCW", gyroCW.getHeading());
            telemetry.addData("GyroCCW", gyroCCW.getHeading());
            telemetry.addData("FL Enc", frontLeft.getTargetPosition() - frontLeft.getCurrentPosition());
            telemetry.addData("FR Enc", frontRight.getTargetPosition() - frontRight.getCurrentPosition());
            telemetry.addData("BL Enc", backLeft.getTargetPosition() - backLeft.getCurrentPosition());
            telemetry.addData("BR Enc", backRight.getTargetPosition() - backRight.getCurrentPosition());
            telemetry.addData("GL Enc", glyphter.getTargetPosition() - glyphter.getCurrentPosition());
            telemetry.update();

        }

    }
}
