
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class AsteroidsGame extends JPanel implements Runnable, KeyListener,MouseListener {

    Ship ship;
    Shot[] shots;
    Asteroid[] asteroids;
    Bonus[] bonuses;
    int numBonuses;
    int numAsteroids;
    int numLifes;
    int pointsmultiplier;
    int score;
    int sumDuration;
    double AstRadius,minAst_predkosc,maxAst_predkosc;
    int AstNumHits,AstNumSplit;
    int scrnHeight,scrnWidth,oldscrnHeight,oldscrnWidth;
    int numShots;
    boolean isShooting;
    boolean mouseButtonPressed;
    boolean paused;
    boolean isDead;
    boolean youwin;
    int x, y;
    Thread thread;
    long startTime, endTime, framePerdiod;
    Dimension dim;


    AsteroidsGame(int width,int height)
    {
        this.scrnHeight=height;
        this.scrnWidth=width;

    }

    public void init() {


        dim=this.getSize();
        ship = new Ship(scrnHeight/2,scrnWidth/2,0,.35,.98,.1,12);


        paused = false;
        isDead=false;
        mouseButtonPressed=false;
        youwin=false;
        shots = new Shot[31];
        numShots=0;
        numAsteroids=0;
        numBonuses=0;
        AstRadius=20;
        sumDuration=0;
        minAst_predkosc=0.5;                                                                        //maksymalna predkosc odczytywana z pliku i przypisywana metodą
        AstNumHits=3;
        AstNumSplit=2;
        numLifes=3;
        oldscrnHeight=480;
        oldscrnWidth=640;

        numAsteroids = 3;
        score=0;


        asteroids=new Asteroid[numAsteroids*(int)Math.pow(AstNumSplit,AstNumHits-1)+1];             //alokowanie maksymalnej ilości asteroid
        bonuses=new Bonus[numAsteroids*(int)Math.pow(AstNumSplit,AstNumHits-1)+1];                  //bonosów moze byc mx tyle ile asteroid



        for(int i=0;i<numAsteroids;i++)
        {
            asteroids[i]=new Asteroid(Math.random()*dim.width,Math.random()*dim.height,AstRadius,minAst_predkosc,maxAst_predkosc,AstNumHits,AstNumSplit);
        }

        isShooting=false;
        x = 0;
        y = 0;
        startTime = 0;
        endTime = 0;
        framePerdiod = 25;

        thread = new Thread(this);
        thread.start();

    }

    public void paint(Graphics g) {

        g.setColor(Color.black);
        g.fillRect(0, 0, dim.width, dim.height);

        if(isDead)
        {
            g.setColor(Color.yellow);
            g.drawString("YOU DIED!",dim.width/2 -  30,dim.height/2);
            g.drawString("Your score:",dim.width/2 - 30,dim.height/2 + 10);
            g.drawString(Integer.toString(score),dim.width/2,dim.height/2 + 20);
            return;
        }

        if(youwin)
        {
            g.setColor(Color.yellow);
            g.drawString("YOU WIN!",dim.width/2 -  30,dim.height/2);
            g.drawString("Your score:",dim.width/2 - 30,dim.height/2 + 10);
            g.drawString(Integer.toString(score),dim.width/2,dim.height/2 + 20);
            return;
        }
        ship.draw(g);
        for(int i=0;i<numShots;i++)
        {
            shots[i].draw(g);
        }

        for(int i=0;i<numAsteroids;i++)
        {
            asteroids[i].draw(g);

        }

        for(int i=0;i<numBonuses;i++)
        {
            bonuses[i].draw(g);
        }

        g.setColor(Color.YELLOW);
        g.drawString("Lifes:",20,20);
        g.setColor(Color.red);
        for(int i=0;i<numLifes;i++)
        {
            g.fillOval(25 + i*20,25,20,20);
        }
        g.setColor(Color.yellow);
        g.drawString("Score:",20,60);
        g.drawString(Integer.toString(score),60,60);

    }



    public void run() {
        for (;;) {
            startTime = System.currentTimeMillis();

            if (isDead) {
                paused = true;
                return;
            }

            if (!paused) {
                ship.move(dim.width, dim.height);
                for (int i = 0; i < numShots; i++) {
                    shots[i].move(dim.width, dim.height);
                    if (shots[i].getLifeLeft() <= 0) {
                        deleteShot(i);
                        i--;
                    }
                }

                updateAsteroids();

                if (isShooting && ship.canShoot()) {
                    shots[numShots] = ship.fire();
                    numShots++;
                }

                repaint();
            }

            try {

                endTime = System.currentTimeMillis();
                if (framePerdiod - (endTime - startTime) > 0)
                    Thread.sleep(framePerdiod - (endTime - startTime));

            } catch (InterruptedException e) {
            }
        }

    }

    public void setPointsmultiplier(int value)
    {
        pointsmultiplier = value;

    }

    public void setMaxAst_predkosc(int speed)
    {
        maxAst_predkosc = speed;
    }

    private void deleteAsteroid(int index)
    {
        numAsteroids--;
        for(int i=index;i<numAsteroids;i++)
        {
            asteroids[i]=asteroids[i+1];
        }
        asteroids[numAsteroids]=null;
    }

    private void addAsteroid(Asteroid new_ast)
    {
        asteroids[numAsteroids]=new_ast;
        numAsteroids++;
    }

    private void addBonus(Bonus new_bonus)
    {
        bonuses[numBonuses]=new_bonus;
        numBonuses++;
    }

    private void deleteBonus()
    {
        numBonuses--;
        for(int i=0;i<numBonuses;i++)
        {
            bonuses[i]=bonuses[i+1];
        }
        bonuses[numBonuses]=null;
    }

    private void updateAsteroids()
    {


            for (int i = 0; i < numBonuses; i++) {
                if (bonuses[i].shipCollision(ship)) {
                    ship.isShielded = true;
                    sumDuration+=bonuses[i].duration;
                    deleteBonus();
                }
            }

            if(sumDuration>0)
                sumDuration--;

            if(sumDuration<=0)
            {
                ship.isShielded=false;
            }

            //System.out.println(sumDuration);

            if(numAsteroids<=0)
            {
                youwin=true;
            }

            for (int i = 0; i < numAsteroids; i++) {
                asteroids[i].move(dim.width, dim.height);
                asteroids[i].resize(dim.width,dim.height,640,480);

                if(!ship.isShielded) {
                    if (asteroids[i].shipCollision(ship)) {
                        if (numLifes == 0) {
                            isDead = true;
                        } else {
                            ship = new Ship(scrnWidth/2, scrnHeight/2, 0, .35, .98, .1, 12);
                            numShots = 0;
                            numLifes--;
                        }

                        return;
                    }
                }

                for (int j = 0; j < numShots; j++) {

                    if (asteroids[i].shotCollision(shots[j])) {

                        deleteShot(j);

                        if(Math.random()>0.8)
                        {
                            addBonus(new Bonus(asteroids[i].x,asteroids[i].y,1,400,15));
                        }

                        if (asteroids[i].getHitsLeft() > 1) {
                            for (int k = 0; k < asteroids[i].getNumSplit(); k++) {
                                addAsteroid(asteroids[i].splitAsteroid(minAst_predkosc, maxAst_predkosc));
                            }
                        }
                        deleteAsteroid(i);
                        j = numShots;
                        score += (pointsmultiplier * 1000);
                        i--;                                    //aby uwzglednic asteroide utworzoną na nowej pozycji
                    }
                }


            }

    }

    public void deleteShot(int index)
    {
        numShots--;
        for(int i=index;i<numShots;i++)
            shots[i]=shots[i+1];
        shots[numShots]=null;
    }

    public void pause()
    {
        if(paused)
            paused=false;
        else if(!paused)
            paused=true;
    }

    @Override
    public void keyTyped(KeyEvent e) {



    }

    public void keyPressed(KeyEvent e)
    {
        if(e.getKeyCode()==KeyEvent.VK_P)
            pause();
        if(paused || !ship.isActive())
            return;
        else if(e.getKeyCode()==KeyEvent.VK_UP)
            ship.setAccelerating(true);
        else if(e.getKeyCode()==KeyEvent.VK_LEFT)
            ship.setTurnLeft(true);
        else if(e.getKeyCode()==KeyEvent.VK_RIGHT)
            ship.setTurnRight(true);
        else if(e.getKeyCode()==KeyEvent.VK_SPACE)
            isShooting=true;





    }

    public void keyReleased(KeyEvent e)
    {

        if(e.getKeyCode()==KeyEvent.VK_UP)
            ship.setAccelerating(false);
        else if(e.getKeyCode()==KeyEvent.VK_LEFT)
            ship.setTurnLeft(false);
        else if(e.getKeyCode()==KeyEvent.VK_RIGHT)
            ship.setTurnRight(false);
        else if(e.getKeyCode()==KeyEvent.VK_SPACE)
            isShooting=false;



    }

    public void autoResize(int width,int height)
    {
            scrnWidth = width;
            scrnHeight = height;
            this.setSize(scrnWidth, scrnHeight);
            dim = this.getSize();
            for (int i = 0; i < numAsteroids; i++) {
                asteroids[i].resize(scrnWidth, scrnHeight,640, 480);
                asteroids[i].relocate(scrnWidth, scrnHeight,640, 480);

            }
            repaint();

    }

    int returnScore()
    {
        return score;
    }

    public void setPause(boolean value)
    {
        paused=value;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}

