package dto.part;

import java.io.Serializable;
import java.rmi.RemoteException;

import remote.model.part.IPart;

public class PartDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String carChassisNumber, name;
	private double weightKg;

	public PartDTO() {}

	public PartDTO(int id, String chassisNumber, String name, double weightKg) {
		setId(id);
		setCarChassisNumber(chassisNumber);
		setName(name);
		setWeightKg(weightKg);
	}

	public PartDTO(String chassisNumber, String name, double weightKg) {
		this(-1, chassisNumber, name, weightKg);
	}

	public PartDTO(IPart remotePart) throws RemoteException {
		this(remotePart.getId(), remotePart.getCarChassisNumber(), remotePart.getName(), remotePart.getWeightKg());
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
	
	public String getType() {
		return name.substring(name.lastIndexOf(" ") + 1);
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setCarChassisNumber(String carChassisNumber) {
		this.carChassisNumber = carChassisNumber;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setWeightKg(double weightKg) {
		this.weightKg = weightKg;
	}

	@Override
	public String toString() {
		return "PartDTO [id=" + id + ", carChassisNumber=" + carChassisNumber + ", name=" + name + ", weightKg="
				+ weightKg + "]";
	}

}
