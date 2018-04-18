package remote.model.car;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.List;

import dto.car.CarDTO;
import remote.model.part.IPart;
import util.Utils;

public class RemoteCar extends UnicastRemoteObject implements ICar {

	private static final long serialVersionUID = 1L;

	private String chassisNumber, model;
	private List<IPart> parts;

	public RemoteCar(String chassisNumber, String model, List<IPart> parts) throws RemoteException {
		this.chassisNumber = chassisNumber;
		this.model = model;
		this.parts = parts;
	}

	public RemoteCar(CarDTO carDTO) throws RemoteException {
		this(carDTO.getChassisNumber(), carDTO.getModel(), Utils.toRemotePartsList(carDTO.getParts()));
	}

	@Override
	public String getChassisNumber() throws RemoteException {
		return chassisNumber;
	}

	@Override
	public String getModel() throws RemoteException {
		return model;
	}

	@Override
	public List<IPart> getParts() throws RemoteException {
		return parts;
	}

	@Override
	public double getWeightKg() throws RemoteException {
		double totalWeight = 0;

		for (IPart part : parts)
			totalWeight += part.getWeightKg();

		return totalWeight;
	}

	@Override
	public String toString() {
		return "RemoteCar [chassisNumber=" + chassisNumber + ", model=" + model + ", parts=" + parts + "]";
	}

}
