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

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.IOException;

public class GameWindow extends JFrame implements ComponentListener{


    private String nickname = "";
    private Client client;
    AsteroidsGame game;
    private String filepath;
    private int asteroidspeed;
    private int pointsmultiplier;
    private int enemiescount;
    private boolean easySelected;
    private boolean normalSelected;
    private boolean hardSelected;

    GameWindow(int height, int width,Client main_client)
    {
       this.setSize(height,width);
        parseOptionsFile();
        addComponentListener(this);
        client=main_client;

        if(easySelected)
        {
            filepath = "easy_level.xml";
            if(client.isConnected())client.getLevelConfig(0);
        }
        if(normalSelected)
        {
            filepath = "normal_level.xml";
            if(client.isConnected())client.getLevelConfig(1);

        }
        if(hardSelected)
        {
            filepath = "hard_level.xml";
            if(client.isConnected())client.getLevelConfig(2);

        }


        parseLevelConfig();

        game = new AsteroidsGame(this.getWidth(),this.getHeight());
        game.addComponentListener(this);
        game.setMaxAst_predkosc(asteroidspeed);
        game.setPointsmultiplier(pointsmultiplier);
        getContentPane().add(game);
        game.init();



        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        printLevelConfig();


    }

    public void parseLevelConfig()
    {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(filepath);
            NodeList speed_list = doc.getElementsByTagName("asteroidspeed");
            NodeList multiplier_list = doc.getElementsByTagName("pointsmultiplier");
            NodeList enemies_list = doc.getElementsByTagName("enemiescount");

            if(speed_list.getLength() == 1)
            {
                Node n = speed_list.item(0);
                Element speed = (Element) n;
                asteroidspeed = Integer.parseInt(speed.getTextContent());
            }

            if(multiplier_list.getLength() == 1)
            {
                Node n = multiplier_list.item(0);
                Element multiplier = (Element) n;
                pointsmultiplier = Integer.parseInt(multiplier.getTextContent());
            }

            if(enemies_list.getLength() == 1)
            {
                Node n = enemies_list.item(0);
                Element enemies = (Element) n;
                enemiescount = Integer.parseInt(enemies.getTextContent());
            }

        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (SAXException e) {
            e.printStackTrace();
        }
    }

    public void printLevelConfig()
    {
        System.out.println("Asteroid speed: " + asteroidspeed);
        System.out.println("Points multiplier: " + pointsmultiplier);
        System.out.println("Enemies count: " + enemiescount);
    }

    public void parseOptionsFile()
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

    String getNickname()
    {
        return nickname;
    }

    public void setNickname()
    {
        game.setPause(true);
        nickname = JOptionPane.showInputDialog("Please type your nickname: ");

        if(nickname.isEmpty()) {
            nickname="UnknownPlayer";
        }
        if(nickname!=null) {
            nickname = nickname.replaceAll("\\s+", "");      //usuwanie spacji i innych znaków białych aby nie naruszyc protokolu
        }

    }

    public void saveScoresFile()
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try{
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse("highscores.xml");
            NodeList score_list = doc.getElementsByTagName("scoreID");
            NodeList name_list = doc.getElementsByTagName("name");
            int[] scoresTable = new int[score_list.getLength() + 1];
            String[] nameTable = new String[name_list.getLength() + 1];


            for(int i=0;i<score_list.getLength();i++)
            {
                Node scr = score_list.item(i);
                if(scr.getNodeType()==Node.ELEMENT_NODE) {
                    Element score = (Element) scr;
                    scoresTable[i] = Integer.parseInt(score.getElementsByTagName("score").item(0).getTextContent());
                    nameTable[i] = ((Element) scr).getElementsByTagName("name").item(0).getTextContent();
                }
            }

            int new_score = game.returnScore();
            scoresTable[5]=new_score;
            nameTable[5]=nickname;
            int tmpscr;
            String tmpname;

            for(int i=0;i<scoresTable.length-1;i++)
            {
                for(int j=0;j<scoresTable.length-1;j++)
                {
                    if(scoresTable[j]>scoresTable[j+1])
                    {
                        tmpscr=scoresTable[j];
                        scoresTable[j]=scoresTable[j+1];
                        scoresTable[j+1]=tmpscr;

                        tmpname=nameTable[j];
                        nameTable[j]=nameTable[j+1];
                        nameTable[j+1]=tmpname;
                    }
                }
            }

            int j=0;

            for(int i=scoresTable.length-1;i>0;i--)
            {

                Node scr = score_list.item(j);
                Element scr_elem = (Element) scr;
                Node scr_value = scr_elem.getElementsByTagName("score").item(0);
                Node scr_name = scr_elem.getElementsByTagName("name").item(0);

                scr_value.setTextContent(Integer.toString(scoresTable[i]));
                scr_name.setTextContent(nameTable[i]);
                j++;


            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("highscores.xml"));
            transformer.transform(source, result);





        } catch (ParserConfigurationException e) {
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

    @Override
    public void componentResized(ComponentEvent e) {
        game.autoResize(getContentPane().getWidth(),getContentPane().getHeight());

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
}







