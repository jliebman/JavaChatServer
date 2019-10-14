package hw1;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ListClient {

	Socket socket;
	String serverHostName = "localhost";
	int serverPortNumber = 4444;
	ServerListener sl;
	String name;

	ListClient() {
		
		// 1. CONNECT TO THE SERVER
		try {
			socket = new Socket(serverHostName, serverPortNumber);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		Scanner s = new Scanner(System.in);
		System.out.println("Enter your Name: (After giving input 'your name', need to press Enter)");
		name = s.next() + ":";
		System.out.println("Enter a Single Line Message: ");
		String msg = "";
		// 2. SPAWN A LISTENER FOR THE SERVER. THIS WILL KEEP RUNNING
		// when a message is received, an appropriate method is called
		
		PrintWriter out;
		
			while(socket.isConnected()) {	
				sl = new ServerListener(this, socket);
				Thread t = new Thread(sl);
				t.start();

			 
			
					
					try {
						msg = s.nextLine();
						out = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()));
						out.println(name + msg);
						out.flush();
					
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.exit(-1);
					}
					
				
			}
			s.close();
			
		}

	public void handleMessage(String name, String s) {
			//if(name != this.name)
		if(s != "")
			System.out.println(name + s);
		
	}

	public static void main(String[] args) {
		ListClient lc = new ListClient();
	} // end of main method

} // end of ListClient

class ServerListener implements Runnable {
	ListClient lc;
	Scanner in; // this is used to read which is a blocking call

	ServerListener(ListClient lc, Socket s) {
		try {
			this.lc = lc;
			in = new Scanner(new BufferedInputStream(s.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (true) { // run forever
			//System.out.println("Client - waiting to read");
			String name = in.next();
			String s = in.nextLine();
			lc.handleMessage(name, s);
		}

	}
}
