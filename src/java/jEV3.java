public class jEV3 {

    static{
        System.loadLibrary("jEV3");
    }

    public static final byte
        MOTOR_A = 0x01,
        MOTOR_B = 0x02,
        MOTOR_C = 0x04,
        MOTOR_D = 0x08,

        PORT_1 = 0x00,
        PORT_2 = 0x01,
        PORT_3 = 0x02,
        PORT_4 = 0x03,

        PORT_A = 0x10,
        PORT_B = 0x11,
        PORT_C = 0x12,
        PORT_D = 0x13,

        STOP_FLOAT = 0,
        STOP_BRAKE = 1;

    private long EV3pointer;
    native private long _connect( int port );
    native private void _move( byte motor, int speed, long pointer );
    native private void _stop( byte motor, int type, long pointer );

    public jEV3( int port ){
        EV3pointer = _connect(port);
    }

    public void move( byte motor, int speed ){
        _move( motor, speed, EV3pointer );
    }

    public void stop( byte motor, int type ){
        _stop( motor, type, EV3pointer);
    }

}
