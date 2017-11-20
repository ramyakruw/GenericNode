import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UDPServer implements Server {
	private DatagramSocket socket;
	private boolean running;
	private byte[] buf = new byte[1024];
	KeyValueStore keyValueStore;

	public UDPServer(int port) throws IOException {
		keyValueStore = new KeyValueStore();
		socket = new DatagramSocket(port);
		System.out.println("Server listening on port: " + socket.getLocalPort());
	}

	@Override
	public void start() throws IOException {
		running = true;

		while (running) {
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			socket.receive(packet);
			//processMessage(packet);
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						processMessage(packet);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			
			t.start();
		}
		socket.close();
	}

	private void processMessage(DatagramPacket packet) throws IOException {
		InetAddress clientAddress = packet.getAddress();
		int clientPort = packet.getPort();
		
		String received = new String(packet.getData(), 0, packet.getLength());
		String[] command = received.split(" ");

		System.out.println("Server received : "+ received);
		System.out.println("Server sending back response on port: " + clientPort);
		
		String operation = command[0];
		if (operation.equals("put")) {
			keyValueStore.put(command[1], command[2]);
			String value = "server response:put key="+command[1];
			DatagramPacket outpacket = new DatagramPacket(value.getBytes(), value.getBytes().length, clientAddress, clientPort);
			socket.send(outpacket);
		} else if (operation.equals("get")) {
			String value = String.format("server response:get key=%s get val=%s", command[1],keyValueStore.get(command[1]));
			DatagramPacket outpacket = new DatagramPacket(value.getBytes(), value.getBytes().length, clientAddress, clientPort);
			socket.send(outpacket);
		} else if (operation.equals("delete")) {
			keyValueStore.delete(command[1]);
			String value = "server delete key="+command[1];
			DatagramPacket outpacket = new DatagramPacket(value.getBytes(), value.getBytes().length, clientAddress, clientPort);
			socket.send(outpacket);
		} else if (operation.equals("store")) {
			ConcurrentHashMap<String,String> store = keyValueStore.store();
			String value1 = "server Response";
			DatagramPacket outpacket1 = new DatagramPacket(value1.getBytes(), value1.getBytes().length, clientAddress,
					clientPort);
			socket.send(outpacket1);
			for (Map.Entry<String, String> entry : store.entrySet()) {
				String value = String.format("key:%s:value:%s", entry.getKey(),entry.getValue());
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

	@Override
	public void exit() {
		running = false;
		System.out.println("UDP Server exiting ...");
	}
}
