import java.io.IOException;
import java.net.*;
import java.io.*;
import java.util.*;

public class ChatServer {
	//Socket s = null;
	boolean started = false;
	ServerSocket ss = null;
	List<Client> clients = new ArrayList<Client>();	

	public static void main(String[] args) {
		new ChatServer().start();
	}
	public void start() {
			try {
				ss = new ServerSocket(8888);
				started = true;
			} catch(BindException e) {
				System.out.println("端口已被使用");
				System.out.println("请关闭相关程序并重新启动服务器");
				System.exit(0);
		    } catch (IOException e) {
				e.printStackTrace();
			}
		 
			try {
				while(started) {
					Socket s = ss.accept();
					Client c = new Client(s);
					new Thread(c).start();
					clients.add(c);
				}
			} catch (IOException e) {
				System.out.println("Client is exit");
			} finally {
				try {
					ss.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
}
	class Client implements Runnable {
		
		Socket s;
		DataInputStream dis;
		DataOutputStream dos;
		boolean bconnected = false; 
	
		public Client(Socket s) {
			this.s = s;	
			try {
				dis = new DataInputStream(s.getInputStream());
				dos = new DataOutputStream(s.getOutputStream());
				bconnected = true;
			} catch (IOException e) {
				e.printStackTrace();
			}		
		}
		
		public void send(String str) {
			try {
				dos.writeUTF(str);
			} catch (IOException e) {
				clients.remove(this);
				System.out.println("无用户");
			}
		}
		
		@Override
		public void run() {
			try {
				while(bconnected) {	
					String str = dis.readUTF();
					for(int i=0; i<clients.size(); i++) {
						Client c = clients.get(i);
						c.send(str);
					}
				}
			} catch (IOException e1) {
				System.out.println("client is exit");
			} finally {
					try {
						if(dos != null) dos.close();
						if(dis != null) dis.close();
						if(s != null) s.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			   }
		}
			
	}
}