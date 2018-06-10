import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ScoresWindow extends JFrame{

    private JLabel tytul;
    private Box box;
    private String[] scoresTable;
    private JLabel[] labelsTable;
    private JButton back;

    public ScoresWindow()
    {
        setTitle("Asteroids - High Scores");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tytul = new JLabel("HIGH SCORES");
        tytul.setForeground(Color.yellow);
        tytul.setFont(new Font("Impact", Font.PLAIN,50));
        setMinimumSize(new Dimension(530,420));

        box = Box.createVerticalBox();
        getContentPane().setBackground(Color.black);

        box.add(Box.createVerticalStrut(10));

        tytul.setAlignmentX(CENTER_ALIGNMENT);
        box.add(tytul);

        box.add(Box.createVerticalStrut(50));

        parseScoresFile();

        box.add(Box.createVerticalStrut(20));

        back = new JButton("BACK");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        back.setAlignmentX(CENTER_ALIGNMENT);
        box.add(back);

        add(box, BorderLayout.CENTER);

        setVisible(true);
    }


    public void parseScoresFile()
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try{
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse("highscores.xml");
            NodeList score_list = doc.getElementsByTagName("scoreID");
            scoresTable = new String[score_list.getLength()];
            labelsTable = new JLabel[scoresTable.length];

            for(int i=0;i<score_list.getLength();i++)
            {
                Node scr = score_list.item(i);
                if(scr.getNodeType()==Node.ELEMENT_NODE) {
                    Element score = (Element) scr;
                    scoresTable[i] = score.getElementsByTagName("name").item(0).getTextContent() + "    " + score.getElementsByTagName("score").item(0).getTextContent();
                }
            }

            for(int k=0;k<scoresTable.length;k++)
            {
                labelsTable[k] = new JLabel(scoresTable[k]);
                labelsTable[k].setAlignmentX(CENTER_ALIGNMENT);
                labelsTable[k].setForeground(Color.yellow);
                labelsTable[k].setFont(new Font("Impact", Font.PLAIN,30));
                box.add(labelsTable[k]);
                box.add(Box.createVerticalStrut(10));
            }


        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

}


