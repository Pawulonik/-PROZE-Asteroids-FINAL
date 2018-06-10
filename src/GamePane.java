import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GamePane extends JPanel {

    private String asteroid_texture1;
    private String asteroid_texture2;
    private String asteroid_texture3;
    private String player_texture;
    private String background_texture;
    private int game_width;
    private int game_height;


    public GamePane(String texture1,String texture2, String texture3, String texture4, String background, int height, int width)
    {

        asteroid_texture1 = texture1;
        asteroid_texture2 = texture2;
        asteroid_texture3 = texture3;
        player_texture = texture4;
        background_texture = background;
        game_height = height;
        game_width = width;

    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        try
        {

            BufferedImage background = resizeImage(ImageIO.read(new File(background_texture)),getWidth(),getHeight(),5);
            BufferedImage ship = ImageIO.read(new File(player_texture));
            BufferedImage asteroid1 = ImageIO.read(new File(asteroid_texture1));
            BufferedImage asteroid2 = ImageIO.read(new File(asteroid_texture2));
            BufferedImage asteroid3 = ImageIO.read(new File(asteroid_texture3));

            g.drawImage(background,0,0,null);
            g.drawImage(ship,getWidth()/2,getHeight()/2,this);
            g.drawImage(asteroid1,getWidth()/2 - 200,getHeight()/2 - 200,this);
            g.drawImage(asteroid2,getWidth()/2 + 100,getHeight()/2 + 100, this);
            g.drawImage(asteroid3,getWidth()/2 + 100,getHeight()/2 - 50,this);
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }


    }

    private static BufferedImage resizeImage(BufferedImage originalImage,Integer img_width, Integer img_height,int type)
    {
        BufferedImage resizedImage = new BufferedImage(img_width, img_height,type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, img_width, img_height, null);
        g.dispose();

        return resizedImage;
    }






}
