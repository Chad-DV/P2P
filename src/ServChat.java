import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
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
  private Socket sock = null; 
  private ClientThread activeClient = null;
  private Thread thread = null;
  private ClientThread client = null;
  private int clientID = 0;
  
  public Hashtable<Integer, Socket> mySockDictionary = new Hashtable<Integer, Socket>();
  public Hashtable<Integer, ClientThread> myThreadDictionary = new Hashtable<Integer, ClientThread>();
  
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
      sock = servSock.accept();
      mySockDictionary.put(sock.getPort(), sock);
      while(true){
        System.out.println("My sockDicktionary B4 in&out : " + mySockDictionary.keySet());
        DataInputStream in = new DataInputStream(sock.getInputStream());
        DataOutputStream out = new DataOutputStream(sock.getOutputStream());
//from Client to Server
        line = in.readUTF();
        if(line.equals("end")){
          line = "Client " + activeClient.clientID + " Active";
        }
        String outString = null;
        if(!line.equals(activeClient.clientID + " Disconnected")){
          outString = JOptionPane.showInputDialog(null, "Client @ "+activeClient.clientID+" : " +line);
        }
        else{
          outString = "";
          JOptionPane.showMessageDialog(null, "Client @ "+activeClient.clientID+" : " +line);
        }
        
//server to client        
        if (outString.equals("end"))
        {
          JOptionPane.showMessageDialog(null, "Session ended");
          servSock.close();
          System.exit(0);
        }
        else if (outString.equals("new"))
        {
          newClientSocket();
          sock = servSock.accept();
          mySockDictionary.put(sock.getPort(), sock);
        }
        else
        {
          System.out.println("WHAT IS outString? : " + outString);
          System.out.println("WHAT IS line? : " + line);
          System.out.println("myDictionary keys" + myThreadDictionary.keySet());
          //if line =  something do something
          if(line.equals( clientID+ " Disconnected"))
          {
            for(int i=clientID-1; i > 0 ; i--)
           {
             if(myThreadDictionary.containsKey(i))
             {
              activeClient = myThreadDictionary.get(i);
              sock = mySockDictionary.get(i);
              out = new DataOutputStream(sock.getOutputStream());
              out.writeUTF(outString);
              out.flush();
              System.out.println("Is ny Thread Alive : " + activeClient.isAlive());
              activeClient.open();
              activeClient.resume();
              i=0;
             }
           }
          }
          else
          {
            out.writeUTF(outString);
            out.flush();
          }
        }
      } 
    }
    catch (IOException ex) {
    System.out.println("SE " + ex);
    }
  }
  public void run() 
  {
    try{
      newClientSocket();
    }
    catch (NullPointerException e){
      System.out.println(e);
    }
  }
  public void newClientThread(Socket sock) throws IOException
  {
    System.out.println("Client @ "+ clientID + " accepted: " + sock);
    client = new ClientThread(this, sock);
    activeClient = client;
    try {
      client.open();
      client.start();
    } catch (IOException ex) {
      System.out.println(ex);
    } 
    myThreadDictionary.put(client.clientID, client);
   
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
        Socket clientSocket = new Socket("localhost", servSock.getLocalPort()); 
        clientID = clientSocket.getLocalPort();
        newClientThread(clientSocket);
      } 
      catch (IOException ex) {
      System.out.println(ex);
      }
  }
}
