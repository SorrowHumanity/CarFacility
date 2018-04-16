package dto.part;

import java.io.Serializable;
import java.rmi.RemoteException;

import remote.part.IPart;

public class PartDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String name;
	private double weight;

	public PartDTO(int id, String name, double weight) {
		this.id = id;
		this.name = name;
		this.weight = weight;
	}

	public PartDTO(IPart remotePart) throws RemoteException {
		this(remotePart.getId(), remotePart.getName(), remotePart.getWeight());
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public double getWeight() {
		return weight;
	}

	@Override
	public String toString() {
		return "PartDTO [id=" + id + ", name=" + name + ", weight=" + weight + "]";
	}

}
