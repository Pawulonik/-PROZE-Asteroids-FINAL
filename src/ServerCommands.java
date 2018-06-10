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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ServerCommands {



    public static String serverCommand(String command)
    {
        StringBuilder sb = new StringBuilder();
        String[] cmd = command.split(":");
        switch (cmd[0]) {
            case "GET_CONFIG": {

                int window_width = 0, window_height = 0, level_option = 0;
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                try {
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document doc = builder.parse("ServerFiles\\config.xml");
                    NodeList width_list = doc.getElementsByTagName("width");
                    NodeList height_list = doc.getElementsByTagName("height");
                    NodeList option_list = doc.getElementsByTagName("option");

                    if (width_list.getLength() == 1) {
                        Node n = width_list.item(0);
                        Element width = (Element) n;
                        window_width = Integer.parseInt(width.getTextContent());
                    }

                    if (height_list.getLength() == 1) {
                        Node n = height_list.item(0);
                        Element height = (Element) n;
                        window_height = Integer.parseInt(height.getTextContent());
                    }

                    for (int i = 0; i < option_list.getLength(); i++) {
                        Node opt = option_list.item(i);
                        if (opt.getNodeType() == Node.ELEMENT_NODE) {
                            Element option = (Element) opt;
                            NodeList childList = option.getChildNodes();
                            switch (option.getAttribute("id")) {
                                case "0":

                                    for (int j = 0; j < childList.getLength(); j++) {
                                        Node n = childList.item(j);
                                        if (n.getNodeType() == Node.ELEMENT_NODE) {
                                            Element optn = (Element) n;
                                            if (Integer.parseInt(optn.getTextContent()) == 1) {
                                                level_option = 0;
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
                                                level_option = 1;
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
                                                level_option = 2;
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

                String message = window_width + " " + window_height + " " + level_option;
                sb.append(message);


                break;
            }

            case "GET_INSTRUCTION": {
                try (BufferedReader br = new BufferedReader(new FileReader("ServerFiles\\instruction.txt"))) {
                    String currentLine;
                    while ((currentLine = br.readLine()) != null) {
                        sb.append(currentLine);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }

            case "GET_LEVEL_CONFIG": {

                int levelParam = Integer.parseInt(cmd[1]);
                String filepath;

                switch (levelParam) {
                    case 0:
                        filepath = "ServerFiles\\easy_level.xml";
                        break;
                    case 1:
                        filepath = "ServerFiles\\normal_level.xml";
                        break;
                    case 2:
                        filepath = "ServerFiles\\hard_level.xml";
                        break;
                    default:
                        filepath = "ServerFiles\\normal_level.xml";
                        break;
                }

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                try {

                    String message;
                    int asteroidspeed = 0, pointsmultiplier = 0, enemiescount = 0;

                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document doc = builder.parse(filepath);
                    NodeList speed_list = doc.getElementsByTagName("asteroidspeed");
                    NodeList multiplier_list = doc.getElementsByTagName("pointsmultiplier");
                    NodeList enemies_list = doc.getElementsByTagName("enemiescount");

                    if (speed_list.getLength() == 1) {
                        Node n = speed_list.item(0);
                        Element speed = (Element) n;
                        asteroidspeed = Integer.parseInt(speed.getTextContent());
                    }

                    if (multiplier_list.getLength() == 1) {
                        Node n = multiplier_list.item(0);
                        Element multiplier = (Element) n;
                        pointsmultiplier = Integer.parseInt(multiplier.getTextContent());
                    }

                    if (enemies_list.getLength() == 1) {
                        Node n = enemies_list.item(0);
                        Element enemies = (Element) n;
                        enemiescount = Integer.parseInt(enemies.getTextContent());
                    }

                    message = asteroidspeed + " " + pointsmultiplier + " " + enemiescount;
                    sb.append(message);

                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                }

                break;
            }

            case "UPDATE_HIGHSCORES": {
                int isOnHighscores = 0;
                String[] highscoredata = cmd[1].split(" ");
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                try {
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document doc = builder.parse("ServerFiles\\highscores.xml");
                    NodeList score_list = doc.getElementsByTagName("scoreID");
                    NodeList name_list = doc.getElementsByTagName("name");
                    int[] scoresTable = new int[score_list.getLength() + 1];
                    String[] nameTable = new String[name_list.getLength() + 1];


                    for (int i = 0; i < score_list.getLength(); i++) {
                        Node scr = score_list.item(i);
                        if (scr.getNodeType() == Node.ELEMENT_NODE) {
                            Element score = (Element) scr;
                            scoresTable[i] = Integer.parseInt(score.getElementsByTagName("score").item(0).getTextContent());
                            nameTable[i] = ((Element) scr).getElementsByTagName("name").item(0).getTextContent();
                        }
                    }

                    int new_score = Integer.parseInt(highscoredata[1]);
                    scoresTable[5] = new_score;
                    nameTable[5] = highscoredata[0];
                    int tmpscr;
                    String tmpname;

                    for (int i = 0; i < scoresTable.length - 1; i++) {
                        for (int j = 0; j < scoresTable.length - 1; j++) {
                            if (scoresTable[j] > scoresTable[j + 1]) {
                                tmpscr = scoresTable[j];
                                scoresTable[j] = scoresTable[j + 1];
                                scoresTable[j + 1] = tmpscr;

                                tmpname = nameTable[j];
                                nameTable[j] = nameTable[j + 1];
                                nameTable[j + 1] = tmpname;
                            }
                        }
                    }

                    int j = 0;


                    for (int k = scoresTable.length - 1; k >= 1; k--) {
                        if (nameTable[k] == highscoredata[0] && scoresTable[k] == Integer.parseInt(highscoredata[1])) {
                            isOnHighscores = 1;
                        }
                    }

                    sb.append(isOnHighscores);

                    for (int i = scoresTable.length - 1; i > 0; i--) {

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
                    StreamResult result = new StreamResult(new File("ServerFiles\\highscores.xml"));
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
                } catch (NumberFormatException e) {

                }
                break;
            }

            case "GET_HIGHSCORES": {

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                try {
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document doc = builder.parse("ServerFiles\\highscores.xml");
                    NodeList score_list = doc.getElementsByTagName("scoreID");
                    String[] scoresTable = new String[score_list.getLength()];

                    for (int i = 0; i < score_list.getLength(); i++) {
                        Node scr = score_list.item(i);
                        if (scr.getNodeType() == Node.ELEMENT_NODE) {
                            Element score = (Element) scr;
                            scoresTable[i] = score.getElementsByTagName("name").item(0).getTextContent() + " " + score.getElementsByTagName("score").item(0).getTextContent();
                        }
                    }

                    for(int i=0;i<scoresTable.length;i++)
                    {
                        if(i==scoresTable.length-1)
                        {
                            sb.append(scoresTable[i]);
                        }
                        else {
                            sb.append(scoresTable[i] + " ");
                        }
                    }

                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                }
                break;
            }

            case "DISCONNECT": {
                String message = "CLOSE";
                sb.append(message);
                break;
            }

            default:sb.append("Invalid Command");




        }

        return sb.toString();
    }


}
