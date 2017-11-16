import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class UDPServer implements Server {
	private DatagramSocket socket;
	private boolean running;
	private byte[] buf = new byte[1024];
	KeyValueStore keyValueStore;

	public UDPServer(int port) throws IOException {
		keyValueStore = new KeyValueStore();
		socket = new DatagramSocket(port);
	}

	@Override
	public void start() throws IOException {
		running = true;

		while (running) {
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			socket.receive(packet);

			InetAddress clientAddress = packet.getAddress();
			int clientPort = packet.getPort();
			
			String received = new String(packet.getData(), 0, packet.getLength());
			System.out.println("received : "+ received);
			String[] command = received.split(" ");

			String operation = command[0];
			if (operation.equals("put")) {
				keyValueStore.put(command[1], command[2]);
			} else if (operation.equals("get")) {
				String value = keyValueStore.get(command[1]);
				DatagramPacket outpacket = new DatagramPacket(value.getBytes(), value.getBytes().length, clientAddress, clientPort);
				socket.send(outpacket);
			} else if (operation.equals("delete")) {
				keyValueStore.delete(command[1]);
			} else if (operation.equals("store")) {
				HashMap<String, String> store = keyValueStore.store();
				for (Map.Entry<String, String> entry : store.entrySet()) {
					String value = entry.getKey() + "," + entry.getValue();
					DatagramPacket outpacket = new DatagramPacket(value.getBytes(), value.getBytes().length, clientAddress,
							clientPort);
					socket.send(outpacket);
				}
			} else if (operation.equals("exit")) {
				this.exit();
			} else {
				throw new RuntimeException("Operation should be of type : put,get,delete,store,exit");
			}
			
			socket.send(new DatagramPacket("EOM".getBytes(), "EOM".getBytes().length, clientAddress, clientPort));
		}
		socket.close();
	}

	@Override
	public void exit() {
		running = false;
		System.out.println("UDP Server exiting ...");
	}
}
