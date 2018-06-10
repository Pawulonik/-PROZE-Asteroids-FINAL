import java.awt.*;

public class Asteroid {

    double x,y,x_predkosc,y_predkosc,radius;
    int hitsLeft, numSplit;
    double yRatio,xRatio;

    public Asteroid(double x, double y,double radius,double min_predkosc,double max_predkosc,int hitsLeft,int numSplit)
    {
        this.x=x;
        this.y=y;
        this.radius=radius;
        this.hitsLeft=hitsLeft;
        this.numSplit=numSplit;


        double  predkosc=min_predkosc + Math.random()*(max_predkosc-min_predkosc),
                direction=2*Math.PI*Math.random();
        x_predkosc=predkosc*Math.cos(direction);
        y_predkosc=predkosc*Math.sin(direction);
    }

    public void move(int scrnWidth, int scrnHeight)
    {
        x+=x_predkosc;
        y+=y_predkosc;

        if(x<0-radius*xRatio)
            x+=scrnWidth+2*radius*xRatio;
        else if(x>scrnWidth+radius*xRatio)
            x-=scrnWidth+radius*xRatio;
        if(y<0-radius*yRatio)
            y+=scrnHeight+2*radius*yRatio;
        else if(y>scrnHeight+radius*yRatio)
            y-=scrnHeight+2*radius*yRatio;
    }

    public void draw(Graphics g)
    {
        g.setColor(Color.darkGray);
        g.fillOval((int)(x-radius+0.5),(int)(y-radius+0.5),(int)(2*radius*xRatio),(int)(2*radius*yRatio));
    }

    public int getHitsLeft()
    {
        return hitsLeft;
    }

    public int getNumSplit()
    {
        return numSplit;
    }

    public boolean shipCollision(Ship ship)
    {
        if(Math.pow(radius*xRatio+ship.getRadius(),2) > Math.pow(ship.getX()-x,2) + Math.pow(ship.getY()-y,2))
            return true;
        return false;

    }

    public boolean shotCollision(Shot shot)
    {
        if(Math.pow(radius*xRatio,2) > Math.pow(shot.getX()-x,2)+Math.pow(shot.getY()-y,2))
            return true;
        return false;
    }

    public Asteroid splitAsteroid(double min_predkosc, double max_predkosc)
    {
        return new Asteroid(x,y,radius/Math.sqrt(numSplit),min_predkosc,max_predkosc,hitsLeft-1,numSplit);
    }

    public void resize(int scrnWidth, int scrnHeight,int old_scrnWidth, int old_scrnHeight)
    {
        xRatio=(double)scrnWidth/old_scrnWidth;
        yRatio=(double)scrnHeight/old_scrnHeight;
    }

    public void relocate(int scrnWidth, int scrnHeight,int old_scrnWidth, int old_scrnHeight)
    {
        x+=scrnWidth-old_scrnWidth;
        y+=scrnHeight-old_scrnWidth;
    }


}
