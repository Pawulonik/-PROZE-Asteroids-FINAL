@ECHO OFF
set path="C:\Program Files\Java\jdk1.8.0_161\bin";
set CLASSPATH="out\production\Asteroids\Server.class"
TITLE ASTEROIDS-SERVER

javac "src\\Server.java" 
javac "src\\ServerThread.java"
javac "src\\ServerCommands.java"


PAUSE 
ECHO. 

jar cvfm Server.jar src\\META-INF\\SERVERMANIFEST.MF out\\production\\Asteroids\\Server.class out\\production\\Asteroids\\ServerThread.class out\\production\\Asteroids\\ServerCommands.class
PAUSE 
ECHO. 
java -jar Server.jar 
PAUSE 