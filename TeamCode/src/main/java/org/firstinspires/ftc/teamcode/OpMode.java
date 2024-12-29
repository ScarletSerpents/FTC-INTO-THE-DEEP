package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class OpMode extends LinearOpMode {
    private DcMotor frontLeftMotor;
    private DcMotor frontRightMotor;
    private DcMotor backRightMotor;
    private DcMotor backLeftMotor;
    private DcMotor arm; // Motor controlling arm up and down motion
    private DcMotor intakeExtension; // Motor controlling intake extension/retraction
    private CRServo intakeWheel; // Servo spinning the intake
    private double servoPower = 1.0;

    private boolean slowMode = true;

    final double ARM_TICKS = 28*(250047.0/4913.0)*(100.0/20.0)*(1/360.0);
    // Preset positions for the arm (in encoder ticks)
    private double armPickupPosition = 0 * ARM_TICKS; // Arm down
    private double armTransitPosition = 0 * ARM_TICKS; // Arm down
    private double armScoringPosition = 160 * ARM_TICKS; // Arm up for scoring

    // Preset positions for the intake extension (in encoder ticks)
    private int intakeExtendedPosition = 1000; // Intake fully extended
    private int intakeRetractedPosition = 0; // Intake fully retracted

    private int offset = 0; // Adjustable offset for fine-tuning arm position

    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize motors and servo
        frontLeftMotor = hardwareMap.dcMotor.get("left_front");
        frontRightMotor = hardwareMap.dcMotor.get("right_front");
        backLeftMotor = hardwareMap.dcMotor.get("left_back");
        backRightMotor = hardwareMap.dcMotor.get("right_back");
        arm = hardwareMap.dcMotor.get("arm");
        intakeExtension = hardwareMap.crservo.get("intakeExtension");
        intakeWheel = hardwareMap.crservo.get("intakeWheel");

        // Drivetrain settings
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        // Arm settings
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        arm.setTargetPosition(0);
//        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Intake extension settings
        /*
        intakeExtension.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeExtension.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intakeExtension.setTargetPosition(0);
        intakeExtension.setMode(DcMotor.RunMode.RUN_TO_POSITION);*/

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
        telemetry.addData("Slow Mode", slowMode);
        telemetry.addData("Arm Power", arm.getPower());
        telemetry.addData("Intake Position", intakeExtension.getCurrentPosition());
        telemetry.addData("Offset", offset);
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
        if (gamepad2.b) {
            arm.setPower(-1.0);
        }

        if (gamepad2.x) {
            intakeExtension.setTargetPosition(0);
            intakeExtension.setPower(-0.5);
        }
        // Scoring: Arm extended up, intake extended
        else if (gamepad2.y) {
            intakeExtension.setTargetPosition(intakeExtendedPosition);
            intakeExtension.setPower(0.5);
        }

        // Update offset for fine-tuning arm height
        if (gamepad2.dpad_up) {
            offset += 5;
        } else if (gamepad2.dpad_down) {
            offset -= 5;
        }

        if (gamepad2.x) {
            servoPower = -servoPower; // Flip direction
            sleep(200); // Small delay to avoid multiple toggles from a single press
        }

        // Set servo position based on power
        // For continuous rotation servos, 0.0 is full reverse, 0.5 is stop, and 1.0 is full forward
        intakeWheel.setPower(1.0);

    }
}