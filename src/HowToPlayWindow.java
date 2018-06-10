import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HowToPlayWindow extends JFrame{

    private JLabel tytul;
    private JTextArea content;
    private String instruction;
    private JButton back;

    public HowToPlayWindow()
    {
        setTitle("Asteroids - How To Play");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tytul = new JLabel("HOW TO PLAY");
        tytul.setForeground(Color.yellow);
        tytul.setFont(new Font("Impact", Font.PLAIN,50));
        setMinimumSize(new Dimension(530,420));

        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("instruction.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                if (sb.length() > 0) {
                    sb.append("\n");
                }
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        instruction = sb.toString();
        content = new JTextArea(instruction);
        content.setWrapStyleWord(true);
        content.setLineWrap(true);
        content.setBackground(Color.black);
        content.setSize(getSize());

        Box box = Box.createVerticalBox();
        getContentPane().setBackground(Color.black);

        box.add(Box.createVerticalStrut(10));

        tytul.setAlignmentX(CENTER_ALIGNMENT);
        box.add(tytul);

        box.add(Box.createVerticalStrut(50));

        content.setAlignmentX(CENTER_ALIGNMENT);
        content.setForeground(Color.yellow);
        content.setFont(new Font("Impact", Font.PLAIN,getHeight()/40));
        content.setEditable(false);

        back = new JButton("BACK");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        back.setAlignmentX(CENTER_ALIGNMENT);


        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                content.setFont(new Font("Impact", Font.PLAIN,getHeight()/40));

            }

            @Override
            public void componentMoved(ComponentEvent e) {

            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        });

        box.add(content);

        box.add(Box.createVerticalStrut(20));

        box.add(back);

        box.add(Box.createVerticalStrut(20));


        add(box, BorderLayout.CENTER);

        setVisible(true);
    }
}
