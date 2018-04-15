package dto.part;

import java.io.Serializable;
import java.rmi.RemoteException;

import model.part.IPart;

public class PartDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private double weight;

	public PartDTO(String name, double weight) {
		this.name = name;
		this.weight = weight;
	}

	public PartDTO(IPart part) throws RemoteException {
		this(part.getName(), part.getWeight());
	}

	public String getName() {
		return name;
	}

	public double getWeight() {
		return weight;
	}

	@Override
	public String toString() {
		return "PartDTO [name=" + name + ", weight=" + weight + "]";
	}

}
