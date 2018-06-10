@ECHO OFF
set path="C:\Program Files\Java\jdk1.8.0_161\bin";
TITLE ASTEROIDS
javac "src\\Asteroid.java" 
javac "src\\AsteroidsGame.java"
javac "src\\Bonus.java"
javac "src\\Client.java"
javac "src\\GamePane.java" 
javac "src\\HowToPlayWindow.java"
javac "src\\GameWindow.java"
javac "src\\main.java"
javac "src\\MainMenu.java"
javac "src\\OptionsWindow.java" 
javac "src\\ScoresWindow.java"
javac "src\\Ship.java" 
javac "src\\Shot.java"

PAUSE 
ECHO. 

jar cvfm Asteroids.jar META-INF\\MANIFEST.MF src\\Asteroid.java src\\AsteroidsGame.java src\\Bonus.java src\\Client.java src\\GamePane.java src\\HowToPlayWindow.java src\\GameWindow.java src\\main.java src\\MainMenu.java src\\OptionsWindow.java src\\ScoresWindow.java src\\Ship.java src\\Shot.java
PAUSE 
ECHO. 
java -jar Asteroids.jar 
PAUSE 