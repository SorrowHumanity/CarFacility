package dto.car;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Arrays;
import dto.part.PartDTO;
import remote.model.car.ICar;
import util.CollectionUtils;

public class CarDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String chassisNumber, model;
	private PartDTO[] parts;

	public CarDTO() {}

	public CarDTO(String chassisNumber, String model, PartDTO[] parts) {
		setChassisNumber(chassisNumber);
		setModel(model);
		setParts(parts);
	}

	public CarDTO(ICar remoteCar) throws RemoteException {
		this(remoteCar.getChassisNumber(), remoteCar.getModel(), CollectionUtils.toDTOArray(remoteCar.getParts()));
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

	public void setChassisNumber(String chassisNumber) {
		this.chassisNumber = chassisNumber;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public void setParts(PartDTO[] parts) {
		this.parts = parts;
	}

	@Override
	public String toString() {
		return "CarDTO [chassisNumber=" + chassisNumber + ", model=" + model + ", parts=" + Arrays.toString(parts)
				+ "]";
	}

}
