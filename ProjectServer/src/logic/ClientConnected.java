package logic;

import java.util.Objects;

import ocsf.server.ConnectionToClient;

public class ClientConnected {
	// Class variables *************************************************
	private String ipAddress;
	private String host;
	private String status;
	//******************************************************************
	
	/**
	 * class for saving each client that connect to the server, only have getters and setters 
	 * 
	 */
	//kfir
	
	
	
	
	
	public String getIpAddress() {
		return ipAddress;
	}

	@Override
	public int hashCode() {
		return Objects.hash(host, ipAddress);
	}

	public boolean equals(ConnectionToClient client) {
		if(this.host == client.getInetAddress().getHostName() && this.ipAddress == client.getInetAddress().getHostAddress()) {
			return true;
		}
		return false;
		
		
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
	
	public ClientConnected(String ipAddress, String host, String status) {
		super();
		this.ipAddress = ipAddress;
		this.host = host;
		this.status = status;
	}

	@Override
	public String toString() {
		return "ClientConnected [ipAddress=" + ipAddress + ", host=" + host + ", status=" + status + "]";
	}
	


}
