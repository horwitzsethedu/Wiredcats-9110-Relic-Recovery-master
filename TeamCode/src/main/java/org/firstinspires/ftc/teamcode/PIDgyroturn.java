package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by Juan Velasquez on 8/26/2017.
 */
@Autonomous(name = "PID Gyro Turn", group = "Autonomous")
public class PIDgyroturn extends OpMode {

    DcMotor left;
    DcMotor right;

    ModernRoboticsI2cGyro gyro;

    @Override
    public void init() {

        left = hardwareMap.dcMotor.get("left");
        right = hardwareMap.dcMotor.get("right");

        gyro = (ModernRoboticsI2cGyro) hardwareMap.gyroSensor.get("gyro");

        left.setDirection(DcMotorSimple.Direction.REVERSE);

        gyro.calibrate();

    }
    @Override
    public void loop(){

        telemetry.addData("Gyro Angle", gyro.getHeading());
        telemetry.update();
        PIDturn(270);

    }
    //Max Power Can Only Be 15%
    public void PIDturn(int angle){

        int zAccumulated = gyro.getHeading();

        if ((angle*2) - 2*zAccumulated >= 10){

            left.setPower(-.15);
            right.setPower(.15);

        }
        else if ((angle*2) - 2*zAccumulated <= -10){

            left.setPower(.15);
            right.setPower(-.15);

        }
        else if (((angle*2) - 2*zAccumulated) <= 10 || ((angle*2) - 2*zAccumulated >= -10)){

            left.setPower(0.0);
            right.setPower(0.0);

        }

    }
}
