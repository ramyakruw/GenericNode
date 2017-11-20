import java.util.Arrays;
import java.util.List;

public class Generic {

	public static void main(String[] args) {
		try {
			if (args.length == 0) {
				printhelp();
			} else if (args[0].equals("us")) {
				int port = Integer.parseInt(args[1]);
				UDPServer udpServer = new UDPServer(port);
				udpServer.start();
			} else if (args[0].equals("uc")) {
				String server = args[1];
				int port = Integer.parseInt(args[2]);
				String message = String.join(" ", Arrays.copyOfRange(args, 3, args.length));
				UDPClient udpClient = new UDPClient(server, port);
				List<String> received = udpClient.sendMessage(message);
				for (String line : received) {
					System.out.println(line);
				}
			} else {
				throw new RuntimeException("Invalid Argument - Give us or uc");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void printhelp() {
		System.out.println("Usage:\r\n" + "Client:\r\n"
				+ "uc/tc <address> <port> put <key> <msg>  UDP/TCP CLIENT: Put an object into store\r\n"
				+ "uc/tc <address> <port> get <key>  UDP/TCP CLIENT: Get an object from store by key\r\n"
				+ "uc/tc <address> <port> del <key>  UDP/TCP CLIENT: Delete an object from store by key\r\n"
				+ "uc/tc <address> <port> store  UDP/TCP CLIENT: Display object store\r\n"
				+ "uc/tc <address> <port> exit  UDP/TCP CLIENT: Shutdown server\r\n"
				+ "rmic <address> put <key> <msg>  RMI CLIENT: Put an object into store\r\n"
				+ "rmic <address> get <key>  RMI CLIENT: Get an object from store by key\r\n"
				+ "rmic <address> del <key>  RMI CLIENT: Delete an object from store by key\r\n"
				+ "rmic <address> store  RMI CLIENT: Display object store\r\n"
				+ "rmic <address> exit  RMI CLIENT: Shutdown server\r\n" + "Server:\r\n"
				+ "us/ts <port>  UDP/TCP/TCP-and-UDP SERVER: run server on <port>.\r\n"
				+ "tus <tcpport> <udpport>  TCP-and-UDP SERVER: run servers on <tcpport> and <udpport> sharing same key-value store.\r\n"
				+ "alls <tcpport> <udpport>  TCP, UDP, and RMI SERVER: run servers on <tcpport> and <udpport> sharing same key-value store.\r\n"
				+ "rmic  RMI Server.\r\n" + "");
	}

}
