package pureJavaEV3;


import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Scanner;

public class Example {
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
        bot.setLR(bot.B, bot.C);

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
                    bot.ride(50, 50);
                }

                else if( e.getKeyChar() == 'd' ){
                    bot.ride(-50, - 50);
                }

                else if( e.getKeyChar() == 'k' ){
                    new Thread( () -> {
                        bot.A.setSpeed(50);
                        bot.A.start();

                        try {
                            Thread.sleep((long) 400);
                        } catch (InterruptedException exception) { }

                        bot.A.setSpeed(-50);

                        try {
                            Thread.sleep((long) 500);
                        } catch (InterruptedException exception) { }

                        bot.A.stopBreak();
                    } ).run();
                }

            }

            public void keyReleased(KeyEvent e){
                bot.ride(0,0);
            }

        });

        panel.add(label, BorderLayout.CENTER);

        setPreferredSize(new Dimension(200, 200));
        getContentPane().add(panel);
    }
}