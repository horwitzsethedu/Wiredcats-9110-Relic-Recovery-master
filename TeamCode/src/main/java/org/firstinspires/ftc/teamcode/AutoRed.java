package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * Created by Juan Velasquez on 9/18/2017.
 */
@Autonomous
public class AutoRed extends WiredcatsLinearOpMode {

    public void runOpMode() throws InterruptedException {

        initialize();
        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate");

        while (!isStarted()) {

            idle();

        }
        while (opModeIsActive()) {

            relicTrackables.activate();
            RelicRecoveryVuMark image = RelicRecoveryVuMark.from(relicTemplate);
            telemetry();


            if (image == RelicRecoveryVuMark.RIGHT){

                knockJewel("red");
                sleep(50);
                encDTL(0.3, 0.3, 1000, 1000, 3);
                sleep(50);
                driveToRedCryptobox();
                sleep(50);
                outtake();
                sleep(300);
                stopAll();
                sleep(50);
                left(0.3);
                sleep(300);
                stopAll();
                sleep(25000);

            }
            else if (image == RelicRecoveryVuMark.CENTER){

                knockJewel("red");
                sleep(50);
                encDTL(0.3, 0.3, 1000, 1000, 3);
                sleep(50);
                driveToRedCryptobox();
                sleep(50);
                encLR(0.2, 0.2, 300, 300, 4);
                sleep(50);
                outtake();
                sleep(300);
                stopAll();
                sleep(25000);

            }
            else{

                knockJewel("red");
                sleep(50);
                encDTL(0.3, 0.3, 1000, 1000, 3);
                sleep(50);
                driveToRedCryptobox();
                sleep(50);
                encLR(0.2, 0.2, 600, 600, 4);
                sleep(50);
                outtake();
                sleep(300);
                stopAll();
                sleep(50);
                right(0.3);
                sleep(300);
                stopAll();
                sleep(25000);

            }
            idle();
            sleep(50);

        }

    }

    String format(OpenGLMatrix transformationMatrix) {
        return (transformationMatrix != null) ? transformationMatrix.formatAsTransform() : "null";
    }

}


