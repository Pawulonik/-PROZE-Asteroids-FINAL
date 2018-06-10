import java.awt.*;

public class Shot {

    final double shotSpeed=12;
    double x,y,x_predkosc,y_predkosc;
    int lifeLeft;

    public Shot(double x, double y, double angle, double shipXVel,double shipYVel, int lifeLeft)
    {
        this.x=x;
        this.y=y;
        x_predkosc=shotSpeed*Math.cos(angle)+shipXVel;
        y_predkosc=shotSpeed*Math.sin(angle)+shipYVel;
        this.lifeLeft=lifeLeft;
    }

    public void move(int scrnWidth, int scrnHeight)
    {
        lifeLeft--;
        x+=x_predkosc;
        y+=y_predkosc;

        if(x<0)
            x+=scrnWidth;
        else if(x>scrnWidth)
            x-=scrnWidth;
        if(y<0)
            y+=scrnHeight;
        if(y>scrnHeight)
            y-=scrnHeight;
        }

        public void draw(Graphics g)
        {
            g.setColor(Color.magenta);
            g.fillOval((int)(x-0.5),(int)(y-0.5),3,3);
        }

        public double getX()
        {
            return x;
        }

        public double getY()
        {
            return y;
        }
        public int getLifeLeft()
        {
            return lifeLeft;
        }
}
