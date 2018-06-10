import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private Socket clientSocket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private int port = 1234;
    private String ip = "127.0.0.1";
    private String levelPath = "";

    public void clientStart()
    {
        try
        {
            clientSocket = new Socket(ip,port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public boolean isConnected()
    {
        if(clientSocket == null) return false;
        return clientSocket.isConnected();
    }

    public void setIpconfig()
    {
        try {
            in = new BufferedReader(new FileReader("ClientFiles\\ipconfig.txt"));
            ip = in.readLine();
            port = Integer.parseInt(in.readLine());
            System.out.println(ip + " " + port);

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void getLevelConfig(int level_param)
    {
        try{

            out.println("GET_LEVEL_CONFIG:" + level_param);
            String level_config = in.readLine();
            System.out.println(level_config);
            String[] config_table=level_config.split(" ");

            switch (level_param) {

                case 0:
                    levelPath="easy_level.xml";
                    break;
                case 1:
                    levelPath="normal_level.xml";
                    break;
                case 2:
                    levelPath="hard_level.xml";
                    break;

            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            try{
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(levelPath);

                Node asteroidspeed = doc.getElementsByTagName("asteroidspeed").item(0);
                Node enemiescount = doc.getElementsByTagName("enemiescount").item(0);
                Node pointsmultiplier = doc.getElementsByTagName("pointsmultiplier").item(0);

                asteroidspeed.setTextContent(config_table[0]);
                pointsmultiplier.setTextContent(config_table[1]);
                enemiescount.setTextContent(config_table[2]);

                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(new File(levelPath));
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
            out.flush();
        }
        catch (IOException e){
            System.out.println("Bad connection. File transfer failed.");
            System.out.println(e);
        }

    }

    public void getConfigFile()
    {
        try{

            out.println("GET_CONFIG:");
            String settings = in.readLine();
            System.out.println(settings);
            String[] msg=settings.split(" ");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            try{
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse("config.xml");

                Node width = doc.getElementsByTagName("width").item(0);
                Node heigth = doc.getElementsByTagName("height").item(0);


                Node option_easy_parent = doc.getElementsByTagName("option").item(0);
                Element opt_easyElement = (Element) option_easy_parent;
                Node opt_easy = opt_easyElement.getElementsByTagName("isSelected").item(0);

                Node option_normal_parent = doc.getElementsByTagName("option").item(1);
                Element opt_normalElement = (Element) option_normal_parent;
                Node opt_normal = opt_normalElement.getElementsByTagName("isSelected").item(0);

                Node option_hard_parent = doc.getElementsByTagName("option").item(2);
                Element opt_hardElement = (Element) option_hard_parent;
                Node opt_hard = opt_hardElement.getElementsByTagName("isSelected").item(0);

                width.setTextContent(msg[0]);
                heigth.setTextContent(msg[1]);

                if(Integer.parseInt(msg[2])==0) {
                    opt_easy.setTextContent("1");
                    opt_normal.setTextContent("0");
                    opt_hard.setTextContent("0");
                }

                else if(Integer.parseInt(msg[2])==1) {
                    opt_easy.setTextContent("0");
                    opt_normal.setTextContent("1");
                    opt_hard.setTextContent("0");
                }

                else if(Integer.parseInt(msg[2])==2) {
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
        catch (IOException e){
            System.out.println("Bad connection. File transfer failed.");
            System.out.println(e);
        }

    }

    public void disconnect()
    {
        out.println("DISCONNECT:");
        System.out.println("Disconnected from server.");
        out.close();
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getHighscores()
    {
        try {
            out.println("GET_HIGHSCORES:");
            String highscores = in.readLine();
            System.out.println(highscores);
            String[] highscores_table=highscores.split(" ");
            int[] scoresTable = new int[5];
            String[] nameTable = new String[5];

            int j=0;

            for(int i=0;i<highscores_table.length-1;i++)
            {

                nameTable[j]=highscores_table[i];
                i++;
                j++;
            }

            j=0;

            for(int i=1;i<highscores_table.length+1;i++)
            {

                scoresTable[j]=Integer.parseInt(highscores_table[i]);
                i++;
                j++;
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            try {
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse("highscores.xml");
                NodeList score_list = doc.getElementsByTagName("scoreID");

                j=0;

                for(int i=0;i<scoresTable.length;i++)
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
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void updateHighscores(String nick, int score)
    {
        out.println("UPDATE_HIGHSCORES:" + nick + " " + score);

        try{

            String isOnHighscores = in.readLine();
            if(Integer.parseInt(isOnHighscores) == 1) System.out.println("Dodano najlepszy wynik!");
            else
                System.out.println("Wynik niedostateczny aby dostać się na listę!");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public void getInstruction()
    {
        try {
            out.println("GET_INSTRUCTION:");
            String instruction = in.readLine();
            PrintWriter out = new PrintWriter("instruction.txt");
            out.println(instruction);
            out.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}


