package pureJavaEV3;


import jssc.*;

import java.util.Arrays;

public class jEV3 implements SerialPortEventListener, AutoCloseable {

    static class MessageType {
        static class Mode{
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

    static class Motor {
        static byte A = 0b1,
                B = 0b10,
                C = 0b100,
                D = 0b1000;
    }

    static class Stop {
        static byte Float = 0, Break = 1;
    }

    private SerialPort ev3;

    public jEV3(String portName) {
        ev3 = new SerialPort(portName);

        try {
            ev3.openPort();

            ev3.setParams(SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            ev3.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN |
                    SerialPort.FLOWCONTROL_RTSCTS_OUT);

            ev3.addEventListener(this, SerialPort.MASK_RXCHAR);

            Thread.sleep(2000);
        } catch (SerialPortException ex) {
            System.out.println(ex);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setSpeed(byte motor, int speed) {
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

    public void start(byte motor) {
        byte[] message = new byte[10];
        byte[] defaultPack = packMessage(MessageType.start);

        for (int i = 0; i < defaultPack.length; i++) {
            message[i] = defaultPack[i];
        }

        message[9] = motor;

        //System.out.println( "start" + Arrays.toString( message ));
        send(message);
    }

    public void stop(byte motor, byte stopMode) {
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

    private byte[] packMessage( MessageType.Mode mode ){
        byte [] msg = new byte[9];
        msg[0] = mode.first;
        msg[7] = mode.second;
        msg[4] = -128;

        return msg;
    }

    private void send(byte[] message){
        try {
            ev3.writeBytes(message);
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {

    }

    @Override
    public void close() throws Exception {
        try {
            ev3.closePort();
        } catch (SerialPortException ex){
            System.out.println(ex);
        }
    }
}
