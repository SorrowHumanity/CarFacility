package dto.car;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Arrays;
import dto.part.PartDTO;
import remote.domain.car.ICar;
import util.CollectionUtils;

public class CarDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String chassisNumber, model;
	private PartDTO[] parts; // DTOs have arrays as parameters, because they are used in the web services

	// no-arg constructor for deserialization (XML to Object)
	public CarDTO() {}

	public CarDTO(String chassisNumber, String model, PartDTO[] parts) {
		setChassisNumber(chassisNumber);
		setModel(model);
		setParts(parts);
	}

	public CarDTO(ICar remoteCar) throws RemoteException {
		this(remoteCar.getChassisNumber(), remoteCar.getModel(), CollectionUtils.toPartDTOArray(remoteCar.getParts()));
	}

	public String getChassisNumber() {
		return chassisNumber;
	}

	public String getModel() {
		return model;
	}

	public PartDTO[] getParts() {
		return parts;
	}

	// avoid passing null values between architectual 
	// tiers by the usage of assertions in the setters.
	// Setters are used in deserialization & the constructors with
	// arguments
	public void setChassisNumber(String chassisNumber) {
		assert chassisNumber != null;
		this.chassisNumber = chassisNumber;
	}

	public void setModel(String model) {
		assert model != null;
		this.model = model;
	}

	public void setParts(PartDTO[] parts) {
		assert parts != null;
		this.parts = parts;
	}
	
	public double getWeightKg() {
		double totalWeight = 0;

		for (PartDTO part : parts)
			totalWeight += part.getWeightKg();

		return totalWeight;
	}

	@Override
	public String toString() {
		return "CarDTO [chassisNumber=" + chassisNumber + ", model=" + model + ", parts=" + Arrays.toString(parts)
				+ "]";
	}

}
