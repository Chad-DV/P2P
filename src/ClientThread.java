
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.Charset;
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
public class ClientThread extends Thread{
  private Socket sock = null;
  private ServChat server = null;
  private DataInputStream input = null;
  private DataOutputStream out = null;
  private String line = null;
  
  public int clientID = -1;
  
  public ClientThread (ServChat myServer, Socket mySock)
  {
    server = myServer;
    sock = mySock;
    clientID = mySock.getLocalPort();
  }
  public void run()
  {
    try {
      line = input.readLine();
      
       while (!line.equals("end"))
    {
        if(!line.equals(null))
        {
          DataOutputStream out = new DataOutputStream(sock.getOutputStream()); 
          out.writeUTF(line);
          out.flush();
//--------------------------------------------------------------------------         
          String outString = new DataInputStream(sock.getInputStream()).readUTF();
          String newLine = JOptionPane.showInputDialog("Server : " + outString);
          line = newLine;
        }
    }
      close();
    } catch (IOException ex) {
      System.out.println(ex);
    }
  }
  public void open() throws IOException
  {  
    String string = "Connection from " + "X" + "successful";
    InputStream inputStream = new ByteArrayInputStream(string.getBytes(Charset.forName("UTF-8")));
    input = new DataInputStream(inputStream);
  }
  public void close() throws IOException
  {  
    sock.close();
    input.close();
  }
}
