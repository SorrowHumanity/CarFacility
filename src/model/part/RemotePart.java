package model.part;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import dto.part.PartDTO;

public class RemotePart extends UnicastRemoteObject implements IPart {

	private static final long serialVersionUID = 1L;

	private String name;
	private double weight;

	public RemotePart(String name, double weight) throws RemoteException {
		this.name = name;
		this.weight = weight;
	}

	public RemotePart(PartDTO partDTO) throws RemoteException {
		this(partDTO.getName(), partDTO.getWeight());
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
		return "RemotePart [name=" + name + ", weight=" + weight + "]";
	}

}
