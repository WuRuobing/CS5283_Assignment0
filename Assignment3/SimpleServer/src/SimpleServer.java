import java.io.*;
import java.net.*;
 
public class SimpleServer
{
    public static void main(String args[])
    {
        DatagramSocket sock = null;
         
        try
        {	
        	InetAddress localAddr= InetAddress.getLocalHost();
			System.out.println(localAddr.getHostAddress());
			System.setProperty("java.net.preferIPv4Stack" , "true");
        	
            //1. creating a server socket, parameter is local port number
            sock = new DatagramSocket(2000);
             
            //buffer to receive incoming data
            byte[] buffer = new byte[1000];
            DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
             
            //2. Wait for an incoming data
            echo("Server socket created. Waiting for incoming data...");
             
            //communication loop
            
                sock.receive(incoming);
                byte[] data = incoming.getData();
                String s = new String(data, 0, incoming.getLength());
                 
                //echo the details of incoming data - client ip : client port - client message
                echo(incoming.getAddress().getHostAddress() + " : " + incoming.getPort() + " - " + s);
                 
                DatagramPacket dp = new DatagramPacket(s.getBytes() , s.getBytes().length , incoming.getAddress() , incoming.getPort());
                sock.send(dp);
            }
        
         
        catch(IOException e)
        {
            System.err.println("IOException " + e);
        }
    }
     
    //simple function to echo data to terminal
    public static void echo(String msg)
    {
        System.out.println(msg);
    }
}