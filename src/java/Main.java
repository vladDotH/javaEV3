import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        jEV3 bot = new jEV3(1);

        String key = "";

        Scanner cin = new Scanner(System.in);

        while ( !key.equals("q") ){
            key = cin.next();

            if( key.equals("w") ){
                System.out.println("up");
            }
            if( key.equals("a") ) {
                System.out.println("left");
            }
            if( key.equals("s") ) {
                System.out.println("down");
            }
            if( key.equals("d") ){
                System.out.println("right");
            }
        }
    }
}
