package bananabank.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.StringTokenizer;



public class WorkerThread extends Thread{
    private Socket socket ;
	private Server server;
	public WorkerThread(Server server, Socket socket){
        super();
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run(){

        try {
            System.out.println("Thread is entering socket");
            Socket cs = socket;
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(cs.getInputStream()));
            PrintStream ps =
                    new PrintStream(cs.getOutputStream(), true);
            String inputLine;
            String outputLine;
            
            Server.workingThreads.put(this.getId(),this);
            
            //outputLine = "800";
            //ps.println(outputLine);
                        
			while (((inputLine = br.readLine())!= "SHUTDOWN")&&
			(inputLine != null)) { 
                System.out.println("Client: " + inputLine);
                StringTokenizer st = new StringTokenizer(inputLine); // Break string into tokens
                int amount = Integer.parseInt(st.nextToken());
                int srcAccountNumber = Integer.parseInt(st.nextToken());
                int dstAccountNumber = Integer.parseInt(st.nextToken());

                if (amount < 0 ||
                        server.bananaBank.getAccount(srcAccountNumber) == null ||
                   this.server.bananaBank.getAccount(dstAccountNumber) == null) {
                    outputLine = "Invalid input! No changes!";
                    ps.println(outputLine);
                    System.err.println("The AccountBlance is invalid or Amount is negative");
                    System.exit(1);
                }
                Account srcAccount = this.server.bananaBank.getAccount(srcAccountNumber);
                Account dstAccount = this.server.bananaBank.getAccount(dstAccountNumber);
                srcAccount.transferTo(amount, dstAccount);
                
                System.out.println("Server: " + amount + " transferred from account "
                        + srcAccountNumber + " to account " + dstAccountNumber);

                outputLine = "Finished updating account";
                ps.println(outputLine);
            }

            
            outputLine = "Bank shutdown!";
            ps.println(outputLine);
            
            this.server.serverSocket.close();
            
            Server.workingThreads.remove(this.getId()); // Except our thread
            for (WorkerThread w : Server.workingThreads.values()){
                try {
                    w.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.server.bananaBank.save("accounts.txt");
            outputLine = "800";
            ps.println(outputLine);
            //System.out.println("800");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
