package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by Juan Velasquez on 9/18/2017.
 */
@TeleOp(name = "TeleOp", group = "TeleOp")
public class WiredCatsTeleOp extends WiredcatsLinearOpMode {

    public void runOpMode() throws InterruptedException{

        initialize();

        while(!isStarted()){

            idle();

        }

        while(opModeIsActive()){

            telemetry();

            int a = 0;

            //Drive Train

                frontLeft.setPower(flp);
                frontRight.setPower(frp);
                backLeft.setPower(blp);
                backRight.setPower(brp);

            //Intake Toggle
            if (gamepad1.a && a == 0){

                a = 1;

            }
            else if (!gamepad1.a && a == 1){

                intakeLeft.setPower(0.5);
                intakeRight.setPower(0.5);
                a = 2;

            }
            else if(gamepad1.a && a == 2){

                a = 3;

            }
            else if (!gamepad1.a && a == 3){

                intakeLeft.setPower(0.0);
                intakeRight.setPower(0.0);
                a = 0;

            }

            intake();

        }


    }

}
