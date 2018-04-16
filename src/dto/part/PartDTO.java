package dto.part;

import java.io.Serializable;
import java.rmi.RemoteException;

import remote.part.IPart;

public class PartDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String carChassisNumber, name;
	private double weight;

	public PartDTO(int id, String chassisNumber, String name, double weight) {
		this.id = id;
		this.carChassisNumber = chassisNumber;
		this.name = name;
		this.weight = weight;
	}

	public PartDTO(IPart remotePart) throws RemoteException {
		this(remotePart.getId(), remotePart.getCarChassisNumber(),
				remotePart.getName(), remotePart.getWeight());
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

	public String getCarChassisNumber() {
		return carChassisNumber;
	}

	@Override
	public String toString() {
		return "PartDTO [id=" + id + ", carChassisNumber=" + carChassisNumber + ", name=" + name + ", weight=" + weight
				+ "]";
	}

}
