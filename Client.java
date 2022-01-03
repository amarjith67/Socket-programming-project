import java.io.*;
import java.util.Scanner;
import java.net.*;
import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;

public class Client implements ActionListener {
    String t;
    JTextField tf;
    JLabel l1;
    JPanel p1;
    static JFrame f;
    static JPanel ta;
    static JScrollPane sp;
    static Box vertical = Box.createVerticalBox();
    static Socket s;
    static DataInputStream din;
    static DataOutputStream dout;
    static String msg = "";
    static String msg2 = "";

    Client() {
        f = new JFrame();
        JButton b = new JButton("send");
        ta = new JPanel();
        tf = new JTextField();
        l1 = new JLabel("SERVER");
        p1 = new JPanel();
        sp = new JScrollPane(ta);

        l1.setFont(new Font("SANS_SERIF", Font.BOLD, 22));
        l1.setBounds(120, 20, 100, 30);
        p1.setBounds(0, 0, 350, 60);
        p1.setBackground(new Color(23, 23, 21));

        b.setBackground(new Color(23, 23, 21));
        b.setForeground(Color.WHITE);
        b.setFont(new Font("SANS_SERIF", Font.PLAIN, 16));
        b.setBounds(225, 710, 100, 45);
        b.addActionListener(this);

        tf.setBounds(10, 710, 210, 45);

        ta.setBounds(0, 65, 390, 700);
        ta.setBackground(Color.WHITE);
        ta.setFont(new Font("SANS_SERIF", Font.PLAIN, 15));
        ta.setForeground(Color.WHITE);

        f.add(b);
        f.add(tf);
        f.add(p1);
        p1.add(l1);
        f.add(ta);

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().setBackground(Color.WHITE);
        f.setSize(350, 800);
        f.setLocation(400, 0);
        f.setLayout(null);
        f.setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        try {
            t = tf.getText();
            if (t.equals("exit")) {
                System.exit(0);
            }
            JPanel p2 = formatLabel(t);

            ta.setLayout(new BorderLayout());

            JPanel right = new JPanel(new BorderLayout());
            right.add(p2, BorderLayout.LINE_END);
            vertical.add(right);
            vertical.add(Box.createVerticalStrut(15));

            ta.add(vertical, BorderLayout.PAGE_START);
            dout.writeUTF(t);
            tf.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JPanel formatLabel(String out) {
        JPanel p3 = new JPanel();
        p3.setLayout(new BoxLayout(p3, BoxLayout.Y_AXIS));

        JLabel l1 = new JLabel("<html><p style = \"width : 150px\">" + out + "</p></html>");
        l1.setFont(new Font("Tahoma", Font.PLAIN, 10));
        l1.setBackground(new Color(37, 211, 102));
        l1.setOpaque(true);
        l1.setBorder(new EmptyBorder(15, 15, 15, 50));

        p3.add(l1);

        return p3;
    }

    public static void main(String[] args) throws Exception {
        new Client();
        try {
            s = new Socket("localhost", 6666);
            din = new DataInputStream(s.getInputStream());
            dout = new DataOutputStream(s.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (!msg2.equals("exit")) {
            ta.setLayout(new BorderLayout());
            msg = din.readUTF();
            JPanel p2 = formatLabel(msg);
            JPanel left = new JPanel(new BorderLayout());
            left.add(p2, BorderLayout.LINE_START);

            vertical.add(left);
            vertical.add(Box.createVerticalStrut(15));
            ta.add(vertical, BorderLayout.PAGE_START);
            f.validate();
        }
        s.close();
    }
}
