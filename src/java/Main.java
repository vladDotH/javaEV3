import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
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

        bot = new jEV3(4);
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
                if( e.getKeyChar() == 'w' )
                    bot.move(jEV3.MOTOR_A, 50);
                else if( e.getKeyChar() == 'd' )
                    bot.move(jEV3.MOTOR_A, -50);
            }

            public void keyReleased(KeyEvent e){
                bot.stop(jEV3.MOTOR_A, jEV3.STOP_FLOAT);
            }

        });

        panel.add(label, BorderLayout.CENTER);

        setPreferredSize(new Dimension(200, 200));
        getContentPane().add(panel);
    }
}