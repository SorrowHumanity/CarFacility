package remote.model.part;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import dto.part.PartDTO;

public class RemotePart extends UnicastRemoteObject implements IPart {

	private static final long serialVersionUID = 1L;

	private int id;
	private String carChassisNumber, name;
	private double weightKg;

	public RemotePart(int id, String carChassisNumber, String name, double weightKg) throws RemoteException {
		this.id = id;
		this.carChassisNumber = carChassisNumber;
		this.name = name;
		this.weightKg = weightKg;
	}

	public RemotePart(PartDTO partDto) throws RemoteException {
		this(partDto.getId(), partDto.getCarChassisNumber(), partDto.getName(), partDto.getWeightKg());
	}

	@Override
	public int getId() throws RemoteException {
		return id;
	}

	@Override
	public String getName() throws RemoteException {
		return name;
	}

	@Override
	public double getWeightKg() throws RemoteException {
		return weightKg;
	}

	@Override
	public String getCarChassisNumber() throws RemoteException {
		return carChassisNumber;
	}

	@Override
	public String getType() throws RemoteException {
		return name.substring(name.lastIndexOf(" ") + 1);
	}

	@Override
	public String toString() {
		return "RemotePart [id=" + id + ", carChassisNumber=" + carChassisNumber + ", name=" + name + ", weightKg="
				+ weightKg + "]";
	}
	
}
