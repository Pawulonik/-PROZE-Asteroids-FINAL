import com.sun.xml.internal.ws.api.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.ContentHandlerFactory;

public class OptionsWindow extends JFrame{


    private JLabel tytul;
    private JLabel tytul2;
    private JLabel opcja1;
    private JLabel opcja2;
    private JLabel opcja3;
    private JRadioButton easy;
    private JRadioButton normal;
    private JRadioButton hard;
    private JButton back;
    private boolean easySelected = false;
    private boolean normalSelected = true;
    private boolean hardSelected = false;


    public OptionsWindow()
    {

        this.setTitle("Asteroids - Options");
        setMinimumSize(new Dimension(530,420));

        this.setMinimumSize(new Dimension(516,420));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tytul = new JLabel("OPTIONS");
        tytul.setForeground(Color.yellow);
        tytul.setFont(new Font("Impact", Font.PLAIN,50));

        tytul2 = new JLabel("DIFFICULTY:");
        tytul2.setForeground(Color.yellow);

        easy = new JRadioButton();

        opcja1 = new JLabel("EASY");
        opcja1.setForeground(Color.yellow);

        normal = new JRadioButton();

        opcja2 = new JLabel("NORMAL");
        opcja2.setForeground(Color.yellow);

        hard = new JRadioButton();

        opcja3 = new JLabel("HARD");
        opcja3.setForeground(Color.yellow);

        ButtonGroup przyciski = new ButtonGroup();
        przyciski.add(easy);
        przyciski.add(normal);
        przyciski.add(hard);


        Box box = Box.createVerticalBox();
        getContentPane().setBackground(Color.black);

        box.add(Box.createVerticalStrut(10));

        tytul.setAlignmentX(CENTER_ALIGNMENT);
        box.add(tytul);

        box.add(Box.createVerticalStrut(25));

        tytul2.setAlignmentX(CENTER_ALIGNMENT);
        box.add(tytul2);

        box.add(Box.createVerticalStrut(10));

        opcja1.setAlignmentX(CENTER_ALIGNMENT);
        box.add(opcja1);

        box.add(Box.createVerticalStrut(5));

        easy.setAlignmentX(CENTER_ALIGNMENT);
        easy.setOpaque(false);
        box.add(easy);

        box.add(Box.createVerticalStrut(10));

        opcja2.setAlignmentX(CENTER_ALIGNMENT);
        box.add(opcja2);

        box.add(Box.createVerticalStrut(5));

        normal.setAlignmentX(CENTER_ALIGNMENT);
        normal.setOpaque(false);
        box.add(normal);

        box.add(Box.createVerticalStrut(10));

        opcja3.setAlignmentX(CENTER_ALIGNMENT);
        box.add(opcja3);

        box.add(Box.createVerticalStrut(5));

        hard.setAlignmentX(CENTER_ALIGNMENT);
        hard.setOpaque(false);
        box.add(hard);

        box.add(Box.createVerticalStrut(30));

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

        sprawdzOpcje();

        setVisible(true);


    }

    public void sprawdzOpcje()          //Tutaj parsuje plik kofiguracyjny
    {
        parseConfigFile();
        if(easySelected)
            {
                easy.setSelected(true);
            }
        else if(normalSelected)
            {
                normal.setSelected(true);
            }
        else if(hardSelected)
            {
                hard.setSelected(true);
            }
        else
            {
            normal.setSelected(true);
            }

    }

    public void parseConfigFile()
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try{
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse("config.xml");
            NodeList option_list = doc.getElementsByTagName("option");
            for(int i=0;i<option_list.getLength();i++)
            {
                Node opt = option_list.item(i);
                if(opt.getNodeType()==Node.ELEMENT_NODE) {
                    Element option = (Element) opt;
                    NodeList childList = option.getChildNodes();
                    switch (option.getAttribute("id")) {
                        case "0":

                            for (int j = 0; j < childList.getLength(); j++) {
                                Node n = childList.item(j);
                                if (n.getNodeType() == Node.ELEMENT_NODE) {
                                    Element optn = (Element) n;
                                    if (Integer.parseInt(optn.getTextContent()) == 1) {
                                        easySelected = true;
                                        normalSelected = false;
                                        hardSelected = false;
                                    }
                                }
                            }
                            break;
                        case "1":

                            for (int j = 0; j < childList.getLength(); j++) {
                                Node n = childList.item(j);
                                if (n.getNodeType() == Node.ELEMENT_NODE) {
                                    Element optn = (Element) n;
                                    if (Integer.parseInt(optn.getTextContent()) == 1) {
                                        normalSelected = true;
                                        easySelected = false;
                                        hardSelected = false;
                                    }
                                }
                            }
                            break;

                        case "2":

                            for (int j = 0; j < childList.getLength(); j++) {
                                Node n = childList.item(j);
                                if (n.getNodeType() == Node.ELEMENT_NODE) {
                                    Element optn = (Element) n;
                                    if (Integer.parseInt(optn.getTextContent()) == 1) {
                                        hardSelected = true;
                                        easySelected = false;
                                        normalSelected = false;
                                    }
                                }
                            }
                            break;
                    }
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    public void saveConfigFile()
    {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try{
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse("config.xml");

            Node option_easy_parent = doc.getElementsByTagName("option").item(0);
            Element opt_easyElement = (Element) option_easy_parent;
            Node opt_easy = opt_easyElement.getElementsByTagName("isSelected").item(0);

            Node option_normal_parent = doc.getElementsByTagName("option").item(1);
            Element opt_normalElement = (Element) option_normal_parent;
            Node opt_normal = opt_normalElement.getElementsByTagName("isSelected").item(0);

            Node option_hard_parent = doc.getElementsByTagName("option").item(2);
            Element opt_hardElement = (Element) option_hard_parent;
            Node opt_hard = opt_hardElement.getElementsByTagName("isSelected").item(0);

            if(easy.isSelected()) {
                opt_easy.setTextContent("1");
                opt_normal.setTextContent("0");
                opt_hard.setTextContent("0");
            }

            else if(normal.isSelected()) {
                opt_easy.setTextContent("0");
                opt_normal.setTextContent("1");
                opt_hard.setTextContent("0");
            }

            else if(hard.isSelected()) {
                opt_easy.setTextContent("0");
                opt_normal.setTextContent("0");
                opt_hard.setTextContent("1");
            }

            else
            {
                opt_easy.setTextContent("0");
                opt_normal.setTextContent("1");
                opt_hard.setTextContent("0");
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("config.xml"));
            transformer.transform(source, result);

            }
          catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }

    }


}
