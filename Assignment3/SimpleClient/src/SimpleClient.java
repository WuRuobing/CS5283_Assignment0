import java.io.*;
import java.net.*;
 
public class SimpleClient
{
    public static void main(String args[])
    {
        DatagramSocket sock = null;
        int port = 2000;
        String s;
         
        //BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
         
        try
        {
        	InetAddress localAddr= InetAddress.getLocalHost();
			System.out.println(localAddr.getHostAddress());
			System.setProperty("java.net.preferIPv4Stack" , "true");
        	
        	
            sock = new DatagramSocket();
             
            InetAddress host = InetAddress.getByName("ec2-54-200-181-74.us-west-2.compute.amazonaws.com");
             
            
                //take input and send the packet
                echo("Enter message to send : ");
                s = "test message";
                byte[] b = s.getBytes();
                 
                DatagramPacket  dp = new DatagramPacket(b , b.length , host , port);
                sock.send(dp);
                 
                //now receive reply
                //buffer to receive incoming data
                byte[] buffer = new byte[1000];
                DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                sock.receive(reply);
                
                byte[] data = reply.getData();
                s = new String(data, 0, reply.getLength());
                 
                //echo the details of incoming data - client ip : client port - client message
                echo(reply.getAddress().getHostAddress() + " : " + reply.getPort() + " - " + s);
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