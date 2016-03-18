import java.io.*;
import java.net.*;
 
public class SimpleClient
{
    public static void main(String args[])
    {
        DatagramSocket sock = null;
        int port = 2000;
        String s;
         
        BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
         
        try
        {
            sock = new DatagramSocket();
             
            InetAddress host = InetAddress.getByName("ec2-54-200-181-74.us-west-2.compute.amazonaws.com");
             
            while(true)
            {
                //take input and send the packet
            	System.out.println("Enter message to send : ");
                s = (String)cin.readLine();
                byte[] b = s.getBytes();
                 
                DatagramPacket  outPacket = new DatagramPacket(b , b.length , host , port);
                sock.send(outPacket);
            }
        }
         
        catch(IOException e)
        {
            System.err.println("IOException " + e);
        }
    }

   
}