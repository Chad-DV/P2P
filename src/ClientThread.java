
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Dictionary;
import java.util.Hashtable;
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
public class ClientThread extends Thread{
  private Socket sock = null;
  private ServChat server = null;
  private DataInputStream input = null;
  private DataOutputStream out = null;
  private String line = null;
  
  
  public int clientID = 0;
  
  public ClientThread (ServChat myServer, Socket mySock)
  {
    server = myServer;
    sock = mySock;
    clientID = mySock.getLocalPort();
  }
  @Override
  public void run()
  {
    com();
  }
  public void open(/*String conMessage*/) throws IOException
  {  
    String string = "Connection to server succesful";//conMessage;
    InputStream inputStream = new ByteArrayInputStream(string.getBytes(Charset.forName("UTF-8")));
    input = new DataInputStream(inputStream);
  }
  public void close() throws IOException
  {  
    sock.close();
    input.close();
  }
  
  public void com()
  {
    try {   
      System.out.println("client run method");
      line = input.readLine();  
      while (true)
      {
        if(!line.equals(null))
        {
          //if(!line.equals("end")){
          out = new DataOutputStream(sock.getOutputStream()); 
          out.writeUTF(line);
          out.flush();//}
//--------------------------------------------------------------------------         
        }
        String in = new DataInputStream(sock.getInputStream()).readUTF();
        String outString = JOptionPane.showInputDialog("Server : " + in/*, "Client " + clientID*/);
        line = outString;
         
        if(outString.equals("end"))
        {
          Socket preSock = sock;
          System.out.println("presock is : " + preSock.getLocalPort());
          for(int i=sock.getLocalPort()-1; i > 0 ; i--)
          {
            if(server.myThreadDictionary.containsKey(i))
            {
              sock = server.myThreadDictionary.get(i).sock;
              System.out.println("sock is : " + sock.getLocalPort());
              //JOptionPane.showMessageDialog(null, "Client " +preSock.getLocalPort()+ " Disconnected");
              out.writeUTF(preSock.getLocalPort() + " Disconnected");
              out.flush();
              i = 0;
            }
          }
          preSock.close();
          server.myThreadDictionary.remove(preSock.getLocalPort());
          server.mySockDictionary.remove(preSock.getLocalPort());
        }
        
      }
    } catch (IOException ex) {
      System.out.println("cl " + ex);
    }
  }
}
