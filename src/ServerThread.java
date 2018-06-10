import java.io.*;
import java.net.Socket;


public class ServerThread implements Runnable {

    private Socket socket;
    private boolean connected=true;

    public ServerThread(Socket socket){
        this.socket=socket;
    }

    @Override
    public void run() {
        try {
            while (connected) {
                InputStream inputStream = socket.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                OutputStream os = socket.getOutputStream();
                PrintWriter pw = new PrintWriter(os, true);
                String fromClient = br.readLine();
                if(fromClient!=null) {
                    System.out.println(fromClient);
                    String serverMessage = ServerCommands.serverCommand(fromClient);
                    System.out.println("[SERWER]" + serverMessage);

                    if(serverMessage.equals("CLOSE"))
                    {
                        os.close();
                        pw.close();
                        br.close();
                        socket.close();
                        connected=false;
                    }
                    else {
                        pw.println(serverMessage);
                        pw.flush();
                        os.flush();
                    }
                }
                Thread.sleep(1000);                                             //dodane w celu optymalizacji
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
