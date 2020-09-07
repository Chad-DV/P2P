
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.Charset;
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
public class Client {
  
public static void main(String[] args)throws IOException {
//int Port =Integer.parseInt(JOptionPane.showInputDialog("Input Your Port : "));
//String IP = JOptionPane.showInputDialog("Input Your IP Server : ");
Socket Sock = new Socket("localhost", 6066);
System.out.println("connected : " + Sock);
String string = JOptionPane.showInputDialog("Waiting for connection : ");
InputStream inputStream = new ByteArrayInputStream(string.getBytes(Charset.forName("UTF-8")));
DataInputStream in = new DataInputStream(inputStream);
DataOutputStream out = new DataOutputStream(Sock.getOutputStream());
String line = in.readLine();

while (!line.equals("end")){
  try{
    if(!line.equals(null))
  {
//From Server to Client
    out.writeUTF(line);
    out.flush();
  }
  }
  catch(NullPointerException e){
    System.out.println("NULL P " + e);
  }
//client to server
  String outString = new DataInputStream(Sock.getInputStream()).readUTF();
  String newLine = JOptionPane.showInputDialog("Server : " + outString);
  line = newLine;
}
Sock.close();
}
}
