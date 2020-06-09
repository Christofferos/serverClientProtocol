import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;
import javax.swing.border.LineBorder;

public class KeyEventListener {
    DataOutputStream dataOut;

    public KeyEventListener (DataOutputStream dataOut) {
        this.dataOut = dataOut;

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }

                JFrame frame = new JFrame("Testing");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.add(new TestPane());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    public class TestPane extends JPanel {

        public TestPane() {

            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 1;
            gbc.gridy = 0;
            add(new KeyPane(KeyEvent.VK_UP, 0), gbc);

            gbc.gridy = 2;
            add(new KeyPane(KeyEvent.VK_DOWN, 0), gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            add(new KeyPane(KeyEvent.VK_LEFT, 0), gbc);

            gbc.gridx = 2;
            add(new KeyPane(KeyEvent.VK_RIGHT, 0), gbc);

            gbc.gridx = 1;
            add(new KeyPane(KeyEvent.VK_SPACE, 0), gbc);

        }
    }

    public class KeyPane extends JPanel {

        public KeyPane(int keyCode, int modifier) {

            setBorder(new LineBorder(Color.DARK_GRAY));

            InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
            ActionMap am = getActionMap();

            im.put(KeyStroke.getKeyStroke(keyCode, modifier, false), "keyPressed");
            im.put(KeyStroke.getKeyStroke(keyCode, modifier, true), "keyReleased");

            am.put("keyPressed", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setBackground(Color.RED);
                    try {
                        // Send key input to the server in string (UTF-8) format.
                        dataOut.writeUTF(Integer.toString(keyCode));
                    } catch(IOException ioe) {
                        System.out.println("IO exception.");
                    }
                    
                }
            });

            am.put("keyReleased", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setBackground(UIManager.getColor("Panel.background"));
                }
            });

        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(20, 20);
        }
    }

}