import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class UDPClient implements Client {

	private DatagramSocket socket;
	private byte[] buf = new byte[1024];
	private String server;
	private int port;

	public UDPClient(String server, int port) {
		this.server = server;
		this.port = port;
	}

	@Override
	public List<String> sendMessage(String message) throws IOException {
		socket = new DatagramSocket();

		DatagramPacket sendPacket = new DatagramPacket(message.getBytes(), message.getBytes().length,
				InetAddress.getByName(server), port);
		socket.send(sendPacket);

		List<String> response = new ArrayList<>();
		while (true) {
			DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);
			socket.receive(receivePacket);
			String received = new String(receivePacket.getData(), 0, receivePacket.getLength());
			if("EOM".equals(received)) {
				break;
			}
			response.add(received);
		}
		return response;
	}

}
