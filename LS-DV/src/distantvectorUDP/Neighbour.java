package distantvectorUDP;
import java.net.InetAddress;


public class Neighbour {

	int id;
	String Ipaddress;
	int port;
	double cost;
	
	
	public Neighbour(int id, String ipaddress, int port, double cost) {
		this.id = id;
		Ipaddress = ipaddress;
		this.port = port;
		this.cost = cost;
	}
	/**
	 * @return the cost
	 */
	public double getCost() {
		return cost;
	}
	/**
	 * @param cost the cost to set
	 */
	public void setCost(double cost) {
		this.cost = cost;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return Ipaddress;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.Ipaddress = address;
	}
	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}
	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}
	
}
