package z.sky.temp;

import java.net.*;
import java.io.*;

public class Server {

	public static void main(String[] args) throws Exception {
		ServerSocket server = new ServerSocket(9999);
		while (true) {
			Socket sock = server.accept();
			FileInputStream in = new FileInputStream("/Users/zhoujianming/index.html");
			OutputStream out = sock.getOutputStream();
			int len = 0;
			byte[] buffer = new byte[1024];
			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
			in.close();
			out.close();
			sock.close();
			/* server.close(); */
		}
	}
}