import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket = null;
    private Socket utilitySocket = null;
    private BufferedReader in = null;
    private int port = 1234;

    public Server()
    {
    }

    public void serverStart()
    {
        try {
            setPort();
            System.out.println("Nasluchiwanie na porcie: " + port);
            serverSocket = new ServerSocket(port);

            while(true)
            {
                utilitySocket = serverSocket.accept();
                new Thread(new ServerThread(utilitySocket)).start();

            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public void setPort()
    {
        try {
            in = new BufferedReader(new FileReader("ServerFiles\\serverconfig.txt"));
            port = Integer.parseInt(in.readLine());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {

        Server server = new Server();
        server.serverStart();

    }
}



