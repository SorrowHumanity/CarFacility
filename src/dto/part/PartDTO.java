package dto.part;

import java.io.Serializable;
import java.rmi.RemoteException;

import remote.model.part.IPart;

public class PartDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String carChassisNumber, name;
	private double weightKg;

	public PartDTO(int id, String chassisNumber, String name, double weightKg) {
		this.id = id;
		this.carChassisNumber = chassisNumber;
		this.name = name;
		this.weightKg = weightKg;
	}
	
	public PartDTO(String chassisNumber, String name, double weightKg) {
		this(0, chassisNumber, name, weightKg);
	}

	public PartDTO(IPart remotePart) throws RemoteException {
		this(remotePart.getId(), remotePart.getCarChassisNumber(), remotePart.getName(),
																	remotePart.getWeightKg());
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public double getWeightKg() {
		return weightKg;
	}

	public String getCarChassisNumber() {
		return carChassisNumber;
	}

	@Override
	public String toString() {
		return "PartDTO [id=" + id + ", carChassisNumber=" + carChassisNumber + 
				", name=" + name + ", weightKg=" + weightKg + "]";
	}

}
