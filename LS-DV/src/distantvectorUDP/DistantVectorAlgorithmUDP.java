package distantvectorUDP;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class DistantVectorAlgorithmUDP {

	int id;
	double[] dv;
	int[] next;
	List<Neighbour> neighbours = new ArrayList<Neighbour>();
	int serverPort;
	static DatagramSocket datagramSocket;
	int noOfNodes = -1;
	Map<Integer, Double> costMap = new Hashtable<Integer, Double>();
	static DatagramSocket socket;
	String filename;
	int sleepTime=5000;
	static Timer timer = null;
	static boolean initiatorFlag = false;

	public DistantVectorAlgorithmUDP(int i, int serverP, String file) {
		id = i;
		serverPort = serverP;
		filename = file;
	}

	private void intitializeDv(int n) {
		dv = new double[n + 1];
		next = new int[n + 1];
		for (int i = 1; i <= n; i++) {
			dv[i] = Integer.MAX_VALUE;
			next[i] = -1;
		}
		next[id] = id;
		dv[id] = 0;
		if(initiatorFlag) {
		for (Iterator<Neighbour> iterator = neighbours.iterator(); iterator
				.hasNext();) {
			Neighbour type = (Neighbour) iterator.next();
			int id = type.getId();
			double cost = type.getCost();
			dv[id] = cost;
			next[id] = id;
		}
		}
	}

	void initialize() {
		BufferedReader br;
		File file = new File(filename);
		try {
			br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				{
					if (noOfNodes == -1) {
						noOfNodes = Integer.parseInt(line); // take first line
					} else {
						String[] parts = line.split(" ");
						int dstn = Integer.parseInt(parts[0]); // destination
						double wgt = Double.parseDouble(parts[1]); // Weight
						String ipAd = parts[2];
						int port = Integer.parseInt(parts[3]); // destination
						costMap.put(dstn, wgt);
						neighbours.add(new Neighbour(dstn, ipAd, port, wgt));
					}
				}

			}
		} catch (IOException e) {
			System.out.println("Error in processing file");
		}
		intitializeDv(noOfNodes);
		printDV();
		listen();
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		sendDistantVectorToNeighbours();
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimeOut(),1000,1000);
		//timer.schedule(new TimeOut(), 1000);
	}

	public void printDV() {
		// for (int i = 1; i < dv.length; i++) {
		// System.out.println("Distance to" + i +" is:" + dv[i]);
		// System.out.println("Next Node to "+i +"is:" + next[i]);
		// }
		System.out.println("");
		System.out.println("Routing Table at " + id);
		System.out
				.println("-----------------------------------------------------------------------");
		System.out.println("Destination  NextHop Cost");
		String disvec = null;
		for (int i = 1; i < dv.length; i++) {
			if(i!=id) {
			if (dv[i] == Integer.MAX_VALUE) {
				disvec = "INF";
			} else {
				disvec = String.valueOf(dv[i]);
			}
			if (i > 9 && next[i] > 9) {
				System.out.println(i + "            " + next[i] + "     "
						+ disvec);
			} else if (i > 9) {
				System.out.println(i + "            " + next[i] + "      "
						+ disvec);
			} else if (next[i] > 9) {
				System.out.println(i + "             " + next[i] + "     "
						+ disvec);
			} else {
				System.out.println(i + "             " + next[i] + "      "
						+ disvec);
			}
		}
	}
		System.out
				.println("-----------------------------------------------------------------------");
	}

	private synchronized void sendDistantVectorToNeighbours() {
		try {
			socket = new DatagramSocket();
		} catch (SocketException e1) {
			System.out.println("Socket failed");
		}
		byte[] distntVector = getDistanceVectorInBytes();
		for (Iterator<Neighbour> iterator = neighbours.iterator(); iterator
				.hasNext();) {
			Neighbour type = (Neighbour) iterator.next();
			InetAddress IP = null;
			try {
				IP = InetAddress.getByName(type.getAddress());
			} catch (UnknownHostException e) {
				System.out
						.println("Conversion Failed for " + type.getAddress());
			}
			DatagramPacket ackPacket = new DatagramPacket(distntVector, 0,
					distntVector.length, IP, type.getPort());
			try {
				socket.send(ackPacket);
			} catch (IOException e) {
				System.out.println("Error Sending");
			}
		}
	}

	private byte[] getDistanceVectorInBytes() {
		String str = String.valueOf(id) + ",";

		for (int i = 1; i < dv.length; i++) {
			String inter = String.valueOf(dv[i]);
			str = str + inter + ",";
		}
		String result = str.substring(0, str.length() - 1);
		return result.getBytes();
	}

	public static void main(String[] args) {
		int id = Integer.parseInt(args[0]);
		int port = Integer.parseInt(args[1]);
		String file = args[2];
		DistantVectorAlgorithmUDP node = new DistantVectorAlgorithmUDP(id, port, file);
		if (args.length == 4) {
			initiatorFlag =  Boolean.parseBoolean(args[3]);
		}
		node.initialize();
	}

	private void listen() {
		try {
			datagramSocket = new DatagramSocket(serverPort);
		} catch (SocketException e) {
			System.out.println("Error Starting the socket");
		}
		SocketReceiver socketReceiver = new SocketReceiver();
		socketReceiver.start();
	}

	/**
	 * The Class DataReceiver.
	 */
	private class SocketReceiver extends Thread {

		public void run() {
			boolean isRunning = true;
			DatagramPacket inPacket = null;
			byte[] inBuffer = null;
			inBuffer = new byte[2048];
			inPacket = new DatagramPacket(inBuffer, inBuffer.length);
			while (isRunning) {
				try {
					datagramSocket.receive(inPacket);
					DataReceiver dataReceiever = new DataReceiver(inPacket);
					dataReceiever.start();
				} catch (Exception e) {
					System.out
							.println("Error Processing packets at the receiver\n");
				}
			}
		}

	}

	/**
	 * The Class DataReceiver.
	 */
	private class DataReceiver extends Thread {

		/** The data received. */
		byte[] dataReceived;

		/** The address. */
		byte[] address = new byte[4];

		/** The port. */
		int portRe;

		int idOfNeighbour;

		/**
		 * Instantiates a new data receiver.
		 * 
		 * @param packetRecieved
		 *            the packet recieved
		 */
		public DataReceiver(DatagramPacket packetRecieved) {
			address = packetRecieved.getAddress().getAddress();
			portRe = packetRecieved.getPort();
			dataReceived = new byte[packetRecieved.getLength()];
			System.arraycopy(packetRecieved.getData(), 0, dataReceived, 0,
					dataReceived.length);
		}

		public void run() {
			synchronized (dv) {
			double[] dvReceived = extractDV(dataReceived);
			boolean changed = false;
			for (int i = 1; i <= noOfNodes; i++) {
				if (dv[i] > (dvReceived[i] + costMap.get(idOfNeighbour))) {
					dv[i] = dvReceived[i] + costMap.get(idOfNeighbour);
					next[i] = idOfNeighbour;
					changed = true;
				}
			}
			if (changed) {
				System.out.println("Distance Vector is changed after an update from " + idOfNeighbour);
				System.out.println("Sending updates to neighbour.");
				printDV();
				sendDistantVectorToNeighbours();
			}
			}
		}

		public double[] extractDV(byte[] dataReceived) {
			double[] dvRec = new double[noOfNodes + 1];
			String data = new String(dataReceived);
			String[] split = data.split(",");
			idOfNeighbour = Integer.parseInt(split[0]);
			for (int i = 1; i < split.length; i++) {
				dvRec[i] = Double.parseDouble(split[i]);
			}
			return dvRec;
		}

	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the dv
	 */
	public double[] getDv() {
		return dv;
	}

	/**
	 * @param dv
	 *            the dv to set
	 */
	public void setDv(double[] dv) {
		this.dv = dv;
	}

	/**
	 * @return the next
	 */
	public int[] getNext() {
		return next;
	}

	/**
	 * @param next
	 *            the next to set
	 */
	public void setNext(int[] next) {
		this.next = next;
	}

	public void setNeighbourNode(DistantVectorAlgorithmUDP node, double wgt) {
		next[node.getId()] = node.getId();
		dv[node.getId()] = wgt;
	}

	
	/**
	 * @return the sleepTime
	 */
	public int getSleepTime() {
		return sleepTime;
	}

	/**
	 * @param sleepTime the sleepTime to set
	 */
	public void setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DistantVectorAlgorithmUDP other = (DistantVectorAlgorithmUDP) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	private class TimeOut extends TimerTask {
        public void run() {
        	sendDistantVectorToNeighbours();
        }
    }
}
