package pureJavaEV3;


import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame.setDefaultLookAndFeelDecorated(true);
                TestFrame frame = new TestFrame();
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}


class TestFrame extends JFrame {

    private JLabel label;

    private jEV3 bot;

    public TestFrame() {
        super("Test frame");
        createGUI();

        int port = (new Scanner(System.in)).nextInt();

        bot = new jEV3("COM" + port);

    }

    public void createGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setFocusable(true);

        label = new JLabel();
        label.setFont(new Font("Calibri", Font.PLAIN, 20));
        label.setHorizontalAlignment(JLabel.CENTER);

        panel.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if( e.getKeyChar() == 'w' ){
                    bot.setSpeed(jEV3.Motor.A, 100);
                    bot.start(jEV3.Motor.A);

                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }

                    bot.setSpeed(jEV3.Motor.A, -100);
                    bot.start(jEV3.Motor.A);

                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }

                else if( e.getKeyChar() == 'd' ){
                    bot.setSpeed(jEV3.Motor.A, -100);
                    bot.start(jEV3.Motor.A);
                }

            }

            public void keyReleased(KeyEvent e){
                bot.stop( jEV3.Motor.A, jEV3.Stop.Break);
            }

        });

        panel.add(label, BorderLayout.CENTER);

        setPreferredSize(new Dimension(200, 200));
        getContentPane().add(panel);
    }
}