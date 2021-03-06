package c.vlad.serialtest2;

import android.util.Log;

public class EV3 extends BTConnecter{

    private static final String TAG = "EV3";

    private static class MessageType {
        static class Mode {
            public byte first, second;
        }

        static Mode speed, stop, start;

        static {
            speed = new Mode();
            speed.first = 10;
            speed.second = -91;

            start = new Mode();
            start.first = 8;
            start.second = -90;

            stop = new Mode();
            stop.first = 9;
            stop.second = -93;
        }
    }

    private static class MotorCodes {
        static byte A = 0b1,
                B = 0b10,
                C = 0b100,
                D = 0b1000;
    }

    private static class Stop {
        static byte Float = 0,
                Break = 1;
    }

    public class Motor {
        private byte code;
        private byte speed = 0;
        private boolean active = false;

        public Motor(int code) {
            this.code = (byte) code;
        }

        public void setSpeed(int speed) {
            if (speed != this.speed)
                EV3.this.setSpeed(code, speed);
        }

        public void start() {
            if (!active) {
                EV3.this.start(code);
                active = true;
            }
        }

        public void stopFloat() {
            if (active) {
                EV3.this.stop(code, Stop.Float);
                active = false;
            }
        }

        public void stopBreak() {
            if (active) {
                EV3.this.stop(code, Stop.Break);
                active = false;
            }
        }
    }

    public final Motor A = new Motor(MotorCodes.A),
            B = new Motor(MotorCodes.B),
            C = new Motor(MotorCodes.C),
            D = new Motor(MotorCodes.D);

    private Motor left = B, right = C;

    public EV3() {
        Log.d(TAG, "EV3 running...");
        super.connect("EV3");
    }

    public void setLR(Motor left, Motor right) {
        this.left = left;
        this.right = right;
    }

    public void ride(int left, int right) {
        this.left.setSpeed(left);
        this.right.setSpeed(right);

        this.left.start();
        this.right.start();

        if (left == 0)
            this.left.stopFloat();
        if (right == 0)
            this.right.stopFloat();
    }

    private void setSpeed(byte motor, int speed) {
        Log.d(TAG, "Speed setting on " + motor + " as " + speed);

        byte[] message = new byte[12];
        byte[] defaultPack = packMessage(MessageType.speed);

        for (int i = 0; i < defaultPack.length; i++) {
            message[i] = defaultPack[i];
        }

        message[9] = motor;
        message[10] = -127;
        message[11] = (byte) speed;

        //System.out.println( "speed" + Arrays.toString( message ));
        send(message);
    }

    private void start(byte motor) {
        Log.d(TAG, "Start on " + motor);

        byte[] message = new byte[10];
        byte[] defaultPack = packMessage(MessageType.start);

        for (int i = 0; i < defaultPack.length; i++) {
            message[i] = defaultPack[i];
        }

        message[9] = motor;

        //System.out.println( "start" + Arrays.toString( message ));
        send(message);
    }

    private void stop(byte motor, byte stopMode) {
        Log.d(TAG, "Stop on " + motor);

        byte[] message = new byte[11];
        byte[] defaultPack = packMessage(MessageType.stop);

        for (int i = 0; i < defaultPack.length; i++) {
            message[i] = defaultPack[i];
        }

        message[9] = motor;
        message[10] = stopMode;

        //System.out.println( "stop" + Arrays.toString( message ));
        send(message);
    }

    private byte[] packMessage(MessageType.Mode mode) {
        byte[] msg = new byte[9];
        msg[0] = mode.first;
        msg[7] = mode.second;
        msg[4] = -128;

        return msg;
    }

    private void send(byte[] message) {
        super.write(message);
    }

    public void close(){
        super.close();
    }
}
