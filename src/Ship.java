import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Ship {

    final double[] origXPts = {14, -10, -6, -10}, origYPts = {0, -8, 0, 8};
    final int radius = 6;
    double x, y, angle, x_predkosc, y_predkosc, acceleration,
            velocityDecay, rotationalSpeed;
    boolean turnLeft, turnRight, accelerating, active,isShielded;
    int[] xPts, yPts, flameXPts, flameYPts;
    int shotDelay, shotDelayLeft;

    public Ship(double x, double y, double angle, double acceleration, double velocityDecay, double rotationalSpeed, int shotDelay) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.acceleration = acceleration;
        this.velocityDecay = velocityDecay;
        this.rotationalSpeed = rotationalSpeed;
        x_predkosc = 0;
        y_predkosc = 0;
        turnLeft = false;
        turnRight = false;
        accelerating = false;
        isShielded=false;
        active = true;
        xPts = new int[4];
        yPts = new int[4];
        flameXPts = new int[3];
        flameYPts = new int[3];
        this.shotDelay = shotDelay;
        shotDelayLeft = 0;
    }

    public void draw(Graphics g) {

        for (int i = 0; i < 4; i++) {
            xPts[i] = (int) (origXPts[i]*Math.cos(angle) - origYPts[i]*Math.sin(angle) + x + 0.5);
            yPts[i] = (int) (origXPts[i]*Math.sin(angle) - origYPts[i] * Math.cos(angle) + y + 0.5);
        }

        if(isShielded)
        {
            g.setColor(Color.cyan);
            g.drawOval((int)x - 20,(int)y - 20,40,40);
        }

        g.setColor(Color.darkGray);
        Polygon statek = new Polygon(xPts,yPts,4);
        g.drawPolygon(statek);
        g.setColor(Color.LIGHT_GRAY);
        g.fillPolygon(statek);


    }

    public void move(int scrnWidth, int scrnHeight) {
        if (shotDelayLeft > 0)
            shotDelayLeft--;
        if (turnLeft)
            angle -= rotationalSpeed;
        if(turnRight)
            angle += rotationalSpeed;
        if (angle > (2 * Math.PI))
            angle -= (2 * Math.PI);
        if (angle < 0)
            angle += (2 * Math.PI);

        if (accelerating) {
            x_predkosc += acceleration * Math.cos(angle);
            y_predkosc += acceleration * Math.sin(angle);
        }

        x += x_predkosc;
        y += y_predkosc;

        //System.out.println(x);
        //System.out.println(y);
        //System.out.println(angle);
        //System.out.println(x_predkosc);
        //System.out.println(y_predkosc);

        x_predkosc *= velocityDecay;
        y_predkosc *= velocityDecay;

        if (x < 0)
            x += scrnWidth;
        else if (x > scrnWidth)
            x -= scrnWidth;
        if (y < 0)
            y += scrnHeight;
        else if (y > scrnHeight)
            y -= scrnHeight;
    }

    public Shot fire()
    {
        shotDelayLeft=shotDelay;
        return new Shot(x,y,angle,x_predkosc,y_predkosc,30);
    }

    public void setAccelerating(boolean accelerating)
    {
        this.accelerating=accelerating;
    }

    public void setTurnLeft(boolean turnLeft)
    {
        this.turnLeft = turnLeft;
    }

    public void setTurnRight(boolean turnRight)
    {
        this.turnRight = turnRight;
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public double getRadius()
    {
        return radius;
    }

    public boolean isActive()
    {
        return active;
    }

    public boolean canShoot()
    {
        if(shotDelayLeft>0)
            return false;
        else
            return true;
    }





}
