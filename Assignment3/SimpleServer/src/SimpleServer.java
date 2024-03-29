import java.io.*;
import java.net.*;
 
public class SimpleServer
{
    public static void main(String args[])
    {
        DatagramSocket sock = null;
         
        try
        {
            sock = new DatagramSocket(2000);
            byte[] buffer = new byte[65536];
            DatagramPacket inPakcet = new DatagramPacket(buffer, buffer.length);
            System.out.println("Server socket created. Waiting for incoming data...");
             
            //receving datapacket
            while(true)
            {
                sock.receive(inPakcet);
                byte[] data = inPakcet.getData();
                String s = new String(data, 0, inPakcet.getLength());
                 
                //print outthe details of incoming data - client ip : client port - client message
                System.out.println("Client IP: " + inPakcet.getAddress().getHostAddress() + " Client port : " + inPakcet.getPort() + " Message: " + s);
            }
        }
         
        catch(IOException e)
        {
            System.err.println("IOException " + e);
        }
    }
     
}