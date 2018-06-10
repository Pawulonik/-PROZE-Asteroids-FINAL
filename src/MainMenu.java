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
import java.awt.event.*;
import java.io.IOException;
import java.net.ConnectException;

import static javax.swing.UIManager.getSystemLookAndFeelClassName;
import static javax.swing.UIManager.setLookAndFeel;


public class MainMenu extends JFrame{


    private int window_height;
    private int window_width;
    private ImageIcon title_icon;
    private JLabel tytul;
    private JButton start;
    private JButton scores;
    private JButton options;
    private JButton howtoplay;
    private JButton exit;
    private boolean isOnline;
    Client client;

    public MainMenu(){

        client = new Client();

        Object[] dialog_options = {"Yes","No"};
        int n = JOptionPane.showOptionDialog(this,"Do you want to play online?","Asteroids - Online",JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, dialog_options, dialog_options[1]);
        System.out.println(n);
        if(n==0) {
            client.setIpconfig();
            client.clientStart();
            if(!client.isConnected())
            {
                JOptionPane.showMessageDialog(this,
                        "Cannot connect to the server.");
            }
        }

        isOnline=client.isConnected();

        parseWindowSize();
        setTitle("Asteroids");
        setSize(window_width,window_height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Box box = Box.createVerticalBox();
        getContentPane().setBackground(Color.BLACK);
        setMinimumSize(new Dimension(530,420));

        box.add(Box.createVerticalStrut(40));


        tytul = new JLabel();
        title_icon = new ImageIcon("logo2.png");
        tytul.setIcon(title_icon);
        tytul.setAlignmentX(CENTER_ALIGNMENT);
        box.add(tytul);

        box.add(Box.createVerticalStrut(30));

        start = new JButton("START");
        start.setForeground(Color.YELLOW);
        start.setBackground(Color.BLACK);
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFrame game_frame = new JFrame("Asteroids");
                GameWindow new_game_window = new GameWindow(getHeight(),getWidth(),client);
                game_frame.setContentPane(new_game_window.getContentPane());
                game_frame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                game_frame.setSize(getWidth(),getHeight());
                game_frame.setLocation(getX(),getY());
                game_frame.setVisible(true);
                ukryjOkno();
                game_frame.addKeyListener((KeyListener) new_game_window.game);
                game_frame.addMouseListener((MouseListener) new_game_window.game);
                game_frame.addWindowListener(new WindowListener() {
                    @Override
                    public void windowOpened(WindowEvent e) {

                    }

                    @Override
                    public void windowClosing(WindowEvent e) {
                        new_game_window.setNickname();
                        new_game_window.saveScoresFile();
                        if(isOnline) client.updateHighscores(new_game_window.getNickname(),new_game_window.game.returnScore());
                        game_frame.setVisible(false);
                        setSize(game_frame.getSize());
                        setLocation(game_frame.getLocation());
                        wyswietlOkno();
                    }

                    @Override
                    public void windowClosed(WindowEvent e) {

                    }

                    @Override
                    public void windowIconified(WindowEvent e) {

                    }

                    @Override
                    public void windowDeiconified(WindowEvent e) {

                    }

                    @Override
                    public void windowActivated(WindowEvent e) {

                    }

                    @Override
                    public void windowDeactivated(WindowEvent e) {

                    }
                });

            }
        });
        start.setActionCommand("gameStarted");
        start.setAlignmentX(CENTER_ALIGNMENT);
        box.add(start);

        box.add(Box.createVerticalStrut(10));

        scores = new JButton("HIGH SCORES");
        scores.setForeground(Color.yellow);
        scores.setBackground(Color.black);
        scores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(isOnline) client.getHighscores();
                ukryjOkno();
                ScoresWindow scores_frame = new ScoresWindow();
                scores_frame.setLocation(getLocation());
                scores_frame.setSize(getSize());
                scores_frame.addWindowListener(new WindowListener() {
                    @Override
                    public void windowOpened(WindowEvent e) {

                    }

                    @Override
                    public void windowClosing(WindowEvent e) {


                    }

                    @Override
                    public void windowClosed(WindowEvent e) {

                        setSize(scores_frame.getSize());
                        setLocation(scores_frame.getLocation());
                        wyswietlOkno();
                    }

                    @Override
                    public void windowIconified(WindowEvent e) {

                    }

                    @Override
                    public void windowDeiconified(WindowEvent e) {

                    }

                    @Override
                    public void windowActivated(WindowEvent e) {

                    }

                    @Override
                    public void windowDeactivated(WindowEvent e) {

                    }
                });

            }
        });
        scores.setAlignmentX(CENTER_ALIGNMENT);
        box.add(scores);

        box.add(Box.createVerticalStrut(10));

        options = new JButton("OPTIONS");
        options.setBackground(Color.black);
        options.setForeground(Color.yellow);
        options.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                ukryjOkno();
                OptionsWindow options_frame = new OptionsWindow();
                options_frame.setLocation(getX(),getY());
                options_frame.setSize(getSize());
                options_frame.addWindowListener(new WindowListener() {
                    @Override
                    public void windowOpened(WindowEvent e) {

                    }

                    @Override
                    public void windowClosing(WindowEvent e) {

                    }

                    @Override
                    public void windowClosed(WindowEvent e) {
                        options_frame.saveConfigFile();
                        setSize(options_frame.getSize());
                        setLocation(options_frame.getLocation());
                        wyswietlOkno();
                    }

                    @Override
                    public void windowIconified(WindowEvent e) {

                    }

                    @Override
                    public void windowDeiconified(WindowEvent e) {

                    }

                    @Override
                    public void windowActivated(WindowEvent e) {

                    }

                    @Override
                    public void windowDeactivated(WindowEvent e) {

                    }
                });

            }
        });
        options.setAlignmentX(CENTER_ALIGNMENT);
        box.add(options);

        box.add(Box.createVerticalStrut(10));

        howtoplay = new JButton("HOW TO PLAY");
        howtoplay.setForeground(Color.yellow);
        howtoplay.setBackground(Color.black);
        howtoplay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(isOnline) client.getInstruction();
                ukryjOkno();
                HowToPlayWindow instruction_frame = new HowToPlayWindow();
                instruction_frame.setLocation(getLocation());
                instruction_frame.setSize(getSize());
                instruction_frame.setVisible(true);
                instruction_frame.addWindowListener(new WindowListener() {
                    @Override
                    public void windowOpened(WindowEvent e) {

                    }

                    @Override
                    public void windowClosing(WindowEvent e) {

                    }

                    @Override
                    public void windowClosed(WindowEvent e) {

                        setSize(instruction_frame.getSize());
                        setLocation(instruction_frame.getLocation());
                        wyswietlOkno();

                    }

                    @Override
                    public void windowIconified(WindowEvent e) {

                    }

                    @Override
                    public void windowDeiconified(WindowEvent e) {

                    }

                    @Override
                    public void windowActivated(WindowEvent e) {

                    }

                    @Override
                    public void windowDeactivated(WindowEvent e) {

                    }
                });
            }
        });
        howtoplay.setAlignmentX(CENTER_ALIGNMENT);
        box.add(howtoplay);

        box.add(Box.createVerticalStrut(10));

        exit = new JButton("EXIT");
        exit.setBackground(Color.black);
        exit.setForeground(Color.yellow);
        exit.addActionListener(new ActionListener() {
            @Override

            public void actionPerformed(ActionEvent e) {

                if(isOnline) client.disconnect();
                System.exit(0);
            }
        });
        exit.setAlignmentX(CENTER_ALIGNMENT);
        box.add(exit);

        add(box, BorderLayout.CENTER);

        setVisible(true);
    }


    public void wyswietlOkno()
    {
        this.setVisible(true);
    }

    public void ukryjOkno()
    {
       this.setVisible(false);
    }

    public void parseWindowSize()
    {
        if(isOnline) client.getConfigFile();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse("config.xml");
            NodeList width_list = doc.getElementsByTagName("width");
            NodeList height_list = doc.getElementsByTagName("height");

            if(width_list.getLength() == 1)
            {
                Node n = width_list.item(0);
                Element width = (Element) n;
                window_width = Integer.parseInt(width.getTextContent());
            }

            if(height_list.getLength() == 1)
            {
                Node n = height_list.item(0);
                Element height = (Element) n;
                window_height = Integer.parseInt(height.getTextContent());
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


 }

