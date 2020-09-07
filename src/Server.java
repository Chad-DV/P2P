
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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
public class Server {
  
  public static void main(String[] args) throws IOException{
    //int Port = Integer.parseInt(JOptionPane.showInputDialog("Input Your Port : "));
     ServerSocket serverSock = new ServerSocket(6066);
     Socket Sock = serverSock.accept();
     DataInputStream in = new DataInputStream(Sock.getInputStream());
     DataOutputStream out = new DataOutputStream(Sock.getOutputStream());
    boolean done = false;
      while (!done)
      {  
//from Client to Server
        String line = in.readUTF();
        String outString = JOptionPane.showInputDialog("Client : " +line);
        done = line.equals("end");
        
//server to client
        out.writeUTF(outString);
        out.flush();
      }
    Sock.close();   
  }
}
