package remote.part;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import dto.part.PartDTO;

public class RemotePart extends UnicastRemoteObject implements IPart {

	private static final long serialVersionUID = 1L;

	private int id;
	private String name;
	private double weight;

	public RemotePart(int id, String name, double weight) throws RemoteException {
		this.id = id;
		this.name = name;
		this.weight = weight;
	}

	public RemotePart(PartDTO partDTO) throws RemoteException {
		this(partDTO.getId(), partDTO.getName(), partDTO.getWeight());
	}

	@Override
	public int getId() throws RemoteException {
		return id;
	}

	@Override
	public String getName() throws RemoteException {
		return name;
	}

	@Override
	public double getWeight() throws RemoteException {
		return weight;
	}

	@Override
	public String toString() {
		return "RemotePart [id=" + id + ", name=" + name + ", weight=" + weight + "]";
	}

}
