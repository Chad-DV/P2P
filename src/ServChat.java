
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Chad
 */
public class ServChat implements Runnable{
  private ServerSocket servSock = null;
  private Thread thread = null;
  private ClientThread client = null;
  private int clientID = -1;
  
  public static void main(String args[])
  {
    System.out.println("main Thread ID : "+ Thread.currentThread().getId());
    new ServChat(6066);
  }

  public ServChat (int port)
  {
    String line = null;
    try {
      servSock = new ServerSocket(port);
      JOptionPane.showMessageDialog(null, "Server Started : " + servSock);
      start();
      Socket sock = servSock.accept();
      System.out.println("does it reach" + sock + " is my socket" );
      DataInputStream in = new DataInputStream(sock.getInputStream());
      DataOutputStream out = new DataOutputStream(sock.getOutputStream());
      boolean done = false;
      while (!done)
      {  
//from Client to Server
        line = in.readUTF();
        String outString = JOptionPane.showInputDialog("Client @ "+client.clientID+" : " +line);
        done = line.equals("end") || line.equals("new");
        
//server to client        
        if (outString.equals("end"))
        {
          out.writeUTF("Server Terminated session");
          out.flush();
          sock.close();
        }
        else if (outString.equals("new"))
        {
          newClientSocket();
        }
        else
        {
          out.writeUTF(outString);
          out.flush();
        }
      } 
    } 
    catch (IOException ex) {
    System.out.println(ex);
    }
  }
  public void run() 
  {
    newClientSocket();
  }
  public void newClientThread(Socket sock) throws IOException
  {
    System.out.println("Client @ "+ clientID + " accepted: " + sock);
    client = new ClientThread(this, sock);
    try {
      client.open();
      client.start();
    } catch (IOException ex) {
      System.out.println(ex);
    }
  }
  public void start()
  {  
    if(thread == null)
    { thread = new Thread(this); 
      thread.start();
    }
  }
  public void stop()
  {  
    if(thread != null)
    { thread.stop(); 
      thread = null;
    }
  }
  public void newClientSocket ()
  {
      try {
     // while(true)
      //{
        Socket clientSocket = new Socket("localhost", servSock.getLocalPort());
        System.out.println("Awaiting connection.." +clientSocket);
        clientID = clientSocket.getLocalPort();
        System.out.println("CID : "+clientID);
        newClientThread(clientSocket);
      //}
    } catch (IOException ex) {
      System.out.println(ex);
    }
  }
}
