import java.awt.*;

public class Bonus {
    double x,y;
    int type,duration,radius;

    public Bonus(double x, double y,int type,int duration,int radius)
    {
        this.x = x;
        this.y = y;
        this.type = type;
        this.duration = duration;
        this.radius = radius;
    }

    public void draw(Graphics g)
    {
        g.setColor(Color.cyan);
        g.fillOval((int)(x-0.5),(int)(y-0.5),2*radius,2*radius);
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public int getDurationLeft()
    {
        return duration;
    }

    public boolean shipCollision(Ship ship)
    {
        if(Math.pow(radius+ship.getRadius(),2) > Math.pow(ship.getX()-x,2) + Math.pow(ship.getY()-y,2))
            return true;
        return false;

    }
}
