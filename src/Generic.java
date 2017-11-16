import java.util.Arrays;
import java.util.List;

public class Generic {

	public static void main(String[] args) {
		try {
			if (args[0].equals("us")) {
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

}
