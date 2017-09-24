import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import java.io.*;
public class ChatClient extends Frame {
	TextField tf = new TextField();
	TextArea ta = new TextArea();
	Socket s;
	DataOutputStream dos;
	DataInputStream dis;
	boolean bconnected = false;
	public static void main(String[] args) {
		new ChatClient().launchFrame();
	}

	public void launchFrame() {
		setBounds(300, 300, 500, 500);
		setVisible(true);
		add(ta, BorderLayout.NORTH);
		add(tf, BorderLayout.SOUTH);
		pack();
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				disconnect();
				setVisible(false);
				System.exit(0);
			}
		});
		tf.addActionListener(new TFListener());
		connect();
		
		new Thread(new ServerThread()).start();
	}  
	
	public void connect() {
		try {
			s = new Socket("127.0.0.1",8888);
			System.out.println("connected!");
			dos = new DataOutputStream(s.getOutputStream());	
			dis = new DataInputStream(s.getInputStream());
			bconnected = true;
		} catch (UnknownHostException e) {
			System.out.println("未找到服务器");
		} catch (BindException e) {
			System.out.println("端口已被使用");
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public void disconnect() {
		
		try {
			dos.close();
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	class TFListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			//tf = (TextField)e.getSource();
			try {
				dos.writeUTF(tf.getText());
				dos.flush();
				//dos.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//ta.append("Client: " + tf.getText() + "\n" );
			tf.setText("");
		}
		
	}
	
	class ServerThread implements Runnable {
		@Override
		public void run() {			
			try {
				while(bconnected) {
					String str = dis.readUTF();
					ta.append("Client:" + str + "\n");
				}
			} catch (IOException e) {
				System.out.println("");
				System.exit(0);
			}
		
				
			
		}
		
	}

}

