package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class MecanumDrive extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        // Declare our motors
        // Make sure your ID's match your configuration
        DcMotor frontLeftMotor = hardwareMap.dcMotor.get("left_front");
        DcMotor backLeftMotor = hardwareMap.dcMotor.get("left_back");
        DcMotor frontRightMotor = hardwareMap.dcMotor.get("right_front");
        DcMotor backRightMotor = hardwareMap.dcMotor.get("right_back");

        DcMotor leftArmMotor = hardwareMap.dcMotor.get("");
        DcMotor rightArmMotor = hardwareMap.dcMotor.get("");
        DcMotor armRotator = hardwareMap.dcMotor.get("");

        // Reverse the right side motors. This may be wrong for your setup.
        // If your robot moves backwards when commanded to go forwards,
        // reverse the left side instead.
        // See the note about this earlier on this page.
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
            double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = gamepad1.right_stick_x;

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio,
            // but only if at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;
            double leftArmPower = gamepad2.left_trigger;
            double rightArmPower = gamepad2.right_trigger;
            double armRotationPower = gamepad2.right_stick_y;


            frontLeftMotor.setPower(frontLeftPower);
            backLeftMotor.setPower(backLeftPower);
            frontRightMotor.setPower(frontRightPower);
            backRightMotor.setPower(backRightPower);

            leftArmMotor.setPower(leftArmPower);
            rightArmMotor.setPower(rightArmPower);
            armRotator.setPower(armRotationPower);
/*
            double tgtPower = 0;
            tgtPower = -this.gamepad1.left_stick_y;
            motorTest.setPower(tgtPower);
            // check to see if we need to move the servo.
            if (gamepad1.y) {
                // move to 0 degrees.
                servoTest.setPosition(0);
            } else if (gamepad1.x) {
                // move to 135 degrees.
                servoTest.setPosition(0.75);
            }
            telemetry.addData("Servo Position", servoTest.getPosition());
            telemetry.addData("Target Power", tgtPower);
            telemetry.addData("Motor Power", motorTest.getPower());
            telemetry.addData("Status", "Running");
            telemetry.update();

 */

        }

        /*while (opModeIsActive()) {
            double y2 = -gamepad2.left_stick_y;
            double x2 = gamepad2.left_stick_x * 1.1;

            double denominator = Math.max(Math.abs(y2) = Math.abs(x2), 1);
            double  = (y2 + x2) / denominator;
            double  = (y2 - x2) / denominator;
            double  = (y2 - x2) / denominator;
            double  = (y2 + x2) / denominator;
            */

    }
}