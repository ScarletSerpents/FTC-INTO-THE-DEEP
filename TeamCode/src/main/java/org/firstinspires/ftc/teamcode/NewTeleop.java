package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class NewTeleop extends LinearOpMode {
    private DcMotor frontLeftMotor;
    public DcMotor frontRightMotor;
    public DcMotor backRightMotor;
    public DcMotor backLeftMotor;
    public Servo intakeWheel;
    public DcMotor arm;
    public Servo intakeLeft;
    public Servo intakeRight;

    private boolean slowMode = true;
    private boolean intaking = false;

    private int[] armPositions = {0,2580,2100,2100,2000};
    int target = 0;
    // a, b, x, y

    private double[] wristPositions = {0,0.35,0.6,0.3};

    public int level = 0;
    int offest = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        frontLeftMotor = hardwareMap.dcMotor.get("left_front");
        frontRightMotor = hardwareMap.dcMotor.get("right_front");
        backLeftMotor = hardwareMap.dcMotor.get("left_back");
        backRightMotor = hardwareMap.dcMotor.get("right_back");
        arm = hardwareMap.dcMotor.get("arm");
        intakeWheel = hardwareMap.servo.get("intakeWheel");
        intakeLeft = hardwareMap.servo.get("intakeLeft");
        intakeRight = hardwareMap.servo.get("intakeRight");


        //dt
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        //arm
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        arm.setTargetPosition(0);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
       // intakeRight.setPosition(0.15);
        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {

            updateDrivetrain();
            updateGamepad1();
            updateGamepad2();
            updateTelemetry();
            updateArmPosition();
        }
    }
    private void updateGamepad1(){
            if(gamepad1.right_trigger > 0.1) slowMode = false;
            else slowMode = true;

    }
    private void updateGamepad2(){
        if(gamepad1.dpad_up){
            offest+=3;
        }else if(gamepad1.dpad_down){
            offest-=3;
        }else{
            offest+=0;
        }
//        if(gamepad2.a) level = 0;
//        if(gamepad2.b) level = 1;
//        if(gamepad2.x) level = 2;
//        if(gamepad2.y) level = 3;
        if(gamepad2.x){
            target = armPositions[0];
            intakeLeft.setPosition(wristPositions[0]);
            intakeRight.setPosition(wristPositions[0]);

        }
        if(gamepad2.b){
            target = armPositions[1];
            intakeLeft.setPosition(wristPositions[0]);
            intakeRight.setPosition(wristPositions[0]);
        }

        if(gamepad2.y){
            target = armPositions[2];
            intakeLeft.setPosition(wristPositions[2]);
            intakeRight.setPosition(wristPositions[2]);
        }
        if(gamepad2.left_bumper){
            target = 2700;
            intakeLeft.setPosition(.5);
            intakeRight.setPosition(.5)
        }
        if(gamepad2.right_trigger >= 0.5){
            intakeLeft.setPosition(wristPositions[1]);
            intakeRight.setPosition(wristPositions[1]);
            if(gamepad2.left_trigger <=0.5){
                intakeRight.setPosition(0.15);
                intakeLeft.setPosition(0.15);
            }
            target = 2650;
            if(gamepad2.left_trigger >=0.5) {
                intakeRight.setPosition(0);
                intakeLeft.setPosition(0);
            }
            intaking = true;
        }
        else{
            if(gamepad2.left_trigger <=0.5){
                intakeRight.setPosition(0.15);
                intakeLeft.setPosition(0.15);
            }
            if(gamepad2.left_trigger >=0.5){
                intakeRight.setPosition(0);
                intakeLeft.setPosition(0);
            }
            if(intaking) {
                intakeLeft.setPosition(wristPositions[0]);
                intakeRight.setPosition(wristPositions[0]);
                intaking = false;
            }
        }
        if(gamepad1.x) intakeWheel.setPosition(0);
    }
    private void updateTelemetry(){
        telemetry.addData("Slow Mode", slowMode);
        telemetry.addData("level", level);
        telemetry.update();
    }
    private void updateArmPosition(){
            arm.setTargetPosition(target - offest);
            arm.setPower(0.7);
//            intakeLeft.setPosition(wristPositions[level]);
    }
    private void updateDrivetrain(){
        double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
        double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
        double rx = gamepad1.right_stick_x;
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (y + x + rx) / denominator;
        double backLeftPower = (y - x + rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower = (y + x - rx) / denominator;

        if(slowMode){
            frontLeftMotor.setPower(0.6*frontLeftPower);
            backLeftMotor.setPower(0.6*backLeftPower);
            frontRightMotor.setPower(0.6*frontRightPower);
            backRightMotor.setPower(0.6*backRightPower);
        }else{
            frontLeftMotor.setPower(frontLeftPower);
            backLeftMotor.setPower(backLeftPower);
            frontRightMotor.setPower(frontRightPower);
            backRightMotor.setPower(backRightPower);
        }


    }
}
