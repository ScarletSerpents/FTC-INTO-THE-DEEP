package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class IntoTheDeepOpMode extends LinearOpMode {
    private DcMotor frontLeftMotor;
    private DcMotor frontRightMotor;
    private DcMotor backRightMotor;
    private DcMotor backLeftMotor;
    private DcMotor arm; // Motor controlling arm up and down motion
    private CRServo intakeWheel; // Servo spinning the intake
    private Servo intakeExtension;
    private DcMotor lin_act;

    private boolean slowMode = true;

    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize motors and servo
        frontLeftMotor = hardwareMap.dcMotor.get("left_front");
        frontRightMotor = hardwareMap.dcMotor.get("right_front");
        backLeftMotor = hardwareMap.dcMotor.get("left_back");
        backRightMotor = hardwareMap.dcMotor.get("right_back");
        arm = hardwareMap.dcMotor.get("arm");
        intakeWheel = hardwareMap.crservo.get("intakeWheel");
        intakeExtension = hardwareMap.servo.get("intakeExtension");
        lin_act = hardwareMap.dcMotor.get("lin_act");

        // Drivetrain settings
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        // Arm settings
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//        arm.setTargetPosition(0);
//        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            updateDrivetrain();
            updateGamepad1();
            updateGamepad2();
            updateTelemetry();
        }
    }

    private void updateGamepad1() {
        slowMode = gamepad1.right_trigger <= 0.1;
    }

    private void updateGamepad2() {
        // Picking Up: Intake fully extended, arm down
    }

    private void updateTelemetry() {
        telemetry.addData("Slow Mode: ", slowMode);
        telemetry.addData("Arm Position: ", arm.getCurrentPosition());
        telemetry.update();
    }

    private void updateDrivetrain() {
        double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
        double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
        double rx = gamepad1.right_stick_x;
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1.0);
        double frontLeftPower = (y + x + rx) / denominator;
        double backLeftPower = (y - x + rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower = (y + x - rx) / denominator;

        if (slowMode) {
            frontLeftMotor.setPower(0.6 * frontLeftPower);
            backLeftMotor.setPower(0.6 * backLeftPower);
            frontRightMotor.setPower(0.6 * frontRightPower);
            backRightMotor.setPower(0.6 * backRightPower);
        } else {
            frontLeftMotor.setPower(frontLeftPower);
            backLeftMotor.setPower(backLeftPower);
            frontRightMotor.setPower(frontRightPower);
            backRightMotor.setPower(backRightPower);
        }

        if (gamepad2.a) {
            arm.setPower(1.0);
        }
        // Transit: Intake retracted, arm down
        else if (gamepad2.b) {
            arm.setPower(-1.0);
        } else {
            arm.setPower(0.0);
        }

        if (gamepad2.x) {
            intakeWheel.setPower(1.0);
        }
        else if (gamepad2.y) {
            intakeWheel.setPower(-1.0);
        } else {
            intakeWheel.setPower(0.0);
        }
        
        if (gamepad2.dpad_up) {
            intakeExtension.setPosition(0.0);
        } else if (gamepad2.dpad_down) {
            intakeExtension.setPosition(1.0);
        }
        
        if (gamepad2.dpad_left) {
            lin_act.setPower(1.0);
        } else if (gamepad2.dpad_right) {
            lin_act.setPower(-1.0);
        } else {
            lin_act.setPower(0.0);
        }

        telemetry.addData("Slow Mode: ", slowMode);
        telemetry.addData("Arm Position: ", arm.getCurrentPosition());
        telemetry.addData("Intake Position", intakeExtension);
        telemetry.update();
    }
}
