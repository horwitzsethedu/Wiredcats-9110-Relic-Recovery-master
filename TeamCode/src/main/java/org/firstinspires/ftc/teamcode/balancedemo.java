package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;

/**
 * Created by Juan Velasquez on 8/30/2017.
 */
@Autonomous(name = "Balance Demo", group = "Autonomous")
public class balancedemo extends OpMode {

    DcMotor left;
    DcMotor right;

    //Front Range Sensor
    byte[] range1Cache;

    I2cAddr RANGE1ADDRESS = new I2cAddr(0x2a);
    public static final int RANGE1_REG_START = 0x04;
    public static final int RANGE1_READ_LENGTH = 2;

    public I2cDevice RANGE1;
    public I2cDeviceSynch RANGE1Reader;

    //Back Range Sensor
    byte[] range2Cache;

    I2cAddr RANGE2ADDRESS = new I2cAddr(0x28);
    public static final int RANGE2_REG_START = 0x04;
    public static final int RANGE2_READ_LENGTH = 2;

    public I2cDevice RANGE2;
    public I2cDeviceSynch RANGE2Reader;

    @Override
    public void init(){

        left = hardwareMap.dcMotor.get("left");
        right = hardwareMap.dcMotor.get("right");

        left.setDirection(DcMotorSimple.Direction.REVERSE);

        RANGE1 = hardwareMap.i2cDevice.get("f");
        RANGE1Reader = new I2cDeviceSynchImpl(RANGE1, RANGE1ADDRESS, false);
        RANGE1Reader.engage();

        RANGE2 = hardwareMap.i2cDevice.get("b");
        RANGE2Reader = new I2cDeviceSynchImpl(RANGE2, RANGE2ADDRESS, false);
        RANGE2Reader.engage();

    }
    @Override
    public void loop(){

        range1Cache = RANGE1Reader.read(0x04, 2);
        range2Cache = RANGE1Reader.read(0x04, 2);

        int FrontUlt = range1Cache[0] & 0xFF;
        int BackUlt = range2Cache[0] & 0xFF;

        telemetry.addData("Front Ultra", FrontUlt);
        telemetry.addData("Back Ultra", BackUlt);


    }

}
