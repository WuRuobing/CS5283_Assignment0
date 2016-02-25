package bananabank.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;


public class Server {
    private static final int PORT = 5000;
    private static final String IP_ADDR = "127.0.0.1";
    //protected static boolean listening = true;
    protected static HashMap<Long, WorkerThread> workingThreads = new HashMap<Long, WorkerThread>();
        
    BananaBank bananaBank;
	ServerSocket serverSocket;
	Thread mainThread;
	
    Server(int PORT , String filename)throws IOException{
    	this.bananaBank = new BananaBank(filename);
    	this.serverSocket = new ServerSocket(PORT);
    	this.mainThread = Thread.currentThread(); 	
    }

    public void acceptLoop(){
    	try{
    		while(true){
    				System.out.println(mainThread.toString() + " Wating for Client connection");
    				
    				Socket cs = serverSocket.accept();
    				
    				System.out.print(mainThread.toString() +" Thread connected");
    				
    				new WorkerThread(this, cs).start();    //start Threads,transfer 
    				                                       //bananabank serversocket mainthread
    				
    			}
    		}catch(IOException e){                 // Close the server socket
    			if(serverSocket.isClosed()){             
    				System.out.println("Server socket closed");    				
    			}else {
    				System.out.println("Error in acceptLoop, shutting down");
    				try{serverSocket.close();}
    				catch(IOException ignored){};
    			}
    		}
    	}
        	 		
    	    	    
    
    public static void main(String[] args) throws IOException {
       System.setProperty("java.net.preferIPv4Stack" , "true");
        // All accounts info on .txt are be loaded into BananaBank class
        // Note bank is ref, so there still only one bank obj when executing
       // BananaBank bank = new BananaBank("accounts.txt");
           	
        try {
        	Server server = new Server(PORT, "accounts.txt");
        	
            System.out.println("Server side set up");
            server.acceptLoop();
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }    

    } 
}
