package hw1;

// GOALS
//
// 1. to show sample Server code
// Note that the server is running on LOCALHOST (which is THIS computer) and the 
// port number associated with this server program is 4444.
// The accept() method just WAITS until some client program tries to access this server
//
// 2. to show how a thread is created to handle client request
//

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class MyServer {

	public static void main(String[] args) throws IOException {
		LinkedList<String> outputs = new LinkedList<String>();
		ServerSocket serverSocket = null;
		int clientNum = 0; // keeps track of how many clients were created
		
		// 1. CREATE A NEW SERVERSOCKET
		try {
			serverSocket = new ServerSocket(4444); // provide MYSERVICE at port
													// 4444
			System.out.println(serverSocket);
		} catch (IOException e) {
			System.out.println("Could not listen on port: 4444");
			System.exit(-1);
		}

		// 2. LOOP FOREVER - SERVER IS ALWAYS WAITING TO PROVIDE SERVICE!
		while (true) {
			Socket clientSocket = null;
			try {

				// 2.1 WAIT FOR CLIENT TO TRY TO CONNECT TO SERVER
				clientSocket = serverSocket.accept();
				clientNum++;
				
				// 2.2 SPAWN A THREAD TO HANDLE CLIENT REQUEST
				Thread t = new Thread(new ClientHandler(clientSocket, clientNum, outputs));
				t.start();

			} catch (IOException e) {
				System.out.println("Accept failed: 4444");
				System.exit(-1);
			}

		} // end while loop

	} // end of main method

} // end of class MyServer

class ClientHandler implements Runnable {
	Socket s; // this is socket on the server side that connects to the CLIENT
	int num; // keeps track of its number just for identifying purposes
	LinkedList<String> outputs = new LinkedList<String>();
	
	ClientHandler(Socket s, int n, LinkedList<String> outputs) {
		this.s = s;
		num = n;
		this.outputs = outputs;
	}

	// This is the client handling code
	public void run() {
		//printSocketInfo(s);// just print some information at the server side about the connection
		
		Scanner in;
		PrintWriter out;
		OutputStream r;
		while(s.isConnected()) {
		try {
			// 1. USE THE SOCKET TO READ WHAT THE CLIENT IS SENDING
			in = new Scanner(new BufferedInputStream(s.getInputStream())); 
			String clientMessage = in.nextLine();
			
			// 2. PRINT WHAT THE CLIENT SENT
			System.out.println(clientMessage);
			r = s.getOutputStream();
			out = new PrintWriter(new BufferedOutputStream(r));
			
			outputs.add(clientMessage);
			for(int i = 0; i < outputs.size(); i++) {
			out.println(outputs.get(i));
			out.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
		// This handling code dies after doing all the printing
	} // end of method run()

	void printSocketInfo(Socket s) {
		System.out.print("Socket on Server " + Thread.currentThread() + " ");
		System.out.print("Server socket Local Address: " + s.getLocalAddress()
				+ ":" + s.getLocalPort());
		System.out.println("  Server socket Remote Address: "
				+ s.getRemoteSocketAddress());
	} // end of printSocketInfo
	
} // end of class ClientHandler
