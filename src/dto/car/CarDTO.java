package dto.car;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;

import dto.part.PartDTO;
import remote.model.car.ICar;
import util.CarFacilityUtils;

public class CarDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String chassisNumber, model;
	private List<PartDTO> parts;

	public CarDTO(String chassisNumber, String model, List<PartDTO> parts) {
		this.chassisNumber = chassisNumber;
		this.model = model;
		this.parts = parts;
	}

	public CarDTO(ICar remoteCar) throws RemoteException {
		this(remoteCar.getChassisNumber(), remoteCar.getModel(), CarFacilityUtils
																.toDTOParts(remoteCar.getParts()));
	}

	public String getChassisNumber() {
		return chassisNumber;
	}

	public String getModel() {
		return model;
	}

	public List<PartDTO> getParts() {
		return parts;
	}
	
	public double getWeight() {
		double totalWeight = 0;
		
		for (PartDTO part : parts) 
			totalWeight += part.getWeightKg();
		
		return totalWeight;
	}

	@Override
	public String toString() {
		return "CarDTO [chassisNumber=" + chassisNumber + ", model=" + model + ", parts=" + parts + "]";
	}

}
