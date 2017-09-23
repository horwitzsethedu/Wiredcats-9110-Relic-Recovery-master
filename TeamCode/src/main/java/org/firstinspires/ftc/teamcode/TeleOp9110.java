package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;


//wat is extens ecks dee
//guang help meh ~Anjew
//WIlllllllllllllllllllllllY BIllY BIllY BO WIllY

@TeleOp(name="Teleop9110", group="Opmode")
        public class TeleOp9110 extends OpMode {

        //Drive train
        DcMotor left;
        DcMotor right;

        //LINEAR
        DcMotor linears;

        //SKIZZORS
        DcMotor scissors;

        //Glyphs conveyor
        DcMotor conveyor;

        //Distance sensor
        public OpticalDistanceSensor distance;

        int Intake = 0;
        int Outtake = 0;


        @Override
        public void init() {

        left = hardwareMap.dcMotor.get("l");
        right = hardwareMap.dcMotor.get("r");
        scissors = hardwareMap.dcMotor.get("s");
        conveyor = hardwareMap.dcMotor.get("c");
        linears = hardwareMap.dcMotor.get("li");
        distance = hardwareMap.opticalDistanceSensor.get("d");



        left.setDirection(DcMotorSimple.Direction.REVERSE);

        }

        @Override
        public void loop() {
        //DRIVE TRAIN
        float lefty = gamepad1.left_stick_y;
        float rightx = gamepad1.right_stick_x;

        if (lefty > 0.5){

        left.setPower(1.0);
        right.setPower(1.0);
        }
        else if (lefty < -0.5){

        left.setPower(-1.0);
        right.setPower(-1.0);
        }
        if (rightx > 0.5){

        right.setPower(1.0);
        left.setPower(-1.0);
        }
        else if (rightx < -0.5) {

        right.setPower(-1.0);
        left.setPower(1.0);
        }


        //Scissor lift
        float leftTrigger2 = gamepad2.left_trigger;
        float rightTrigger2 = gamepad2.right_trigger;
        if (leftTrigger2 < 0.05){
        scissors.setPower(0.0);
        }
        else if (leftTrigger2 > 0.05) {
        scissors.setPower(1.0);
        }
        if (rightTrigger2 < 0){
        scissors.setPower(0.0);
        }
        else if (rightTrigger2 > 0.05) {
        scissors.setPower(-1.0);
        }

        //Linear Slide
        float leftTrigger1 = gamepad1.left_trigger;
        float rightTrigger1 = gamepad1.right_trigger;
        if (leftTrigger1 < 0.05){
        linears.setPower(0.0);
        }
        else if (leftTrigger1 > 0.05){
        linears.setPower(1.0);
        }
        if (rightTrigger1 < 0.05){
        linears.setPower(0.0);
        }
        else if (rightTrigger1 > 0.05){
        linears.setPower(-1.0);
        }

        //Conveyor INTAKE & OUTTAKE, THX JUANNNN
        boolean intake = gamepad1.a;
        boolean outtake = gamepad1.x;

        //Intake //sensor measured in centimeters
        if (intake == true && Intake == 0 && distance.getLightDetected() < 0.5){

        Intake = 1;


        }
        else if(intake == false && Intake == 1 && distance.getLightDetected() < 0.5){

        conveyor.setPower(0.5);
        Intake = 2;

        }
        else if (intake == true && Intake == 2 || distance.getLightDetected() > 0.5){

        Intake = 3;

        }
        else if (intake == false && Intake == 3 || distance.getLightDetected() > 0.5){

        conveyor.setPower(0.0);
        Intake = 0;

        }

        //Outtake
        if (outtake == true && Outtake == 0){

        Outtake = 1;
        }
        else if (outtake == false && Outtake == 1){

        conveyor.setPower(-0.5);
        Outtake = 2;
        }
        else if (outtake == true && Outtake == 2){

        Outtake = 3;
        }
        else if (outtake == false && Outtake == 3){

        Outtake = 0;
        }


        }


}
