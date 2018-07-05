package remote.domain.part;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import dto.part.PartDTO;

public class RemotePart extends UnicastRemoteObject implements IPart {

	private static final long serialVersionUID = 1L;

	private int id;
	private String name;
	private double weightKg;

	public RemotePart(int id, String name, double weightKg) throws RemoteException {
		this.id = id;
		this.name = name;
		this.weightKg = weightKg;
	}

	// used for when creating car objects before a car is registered for the first time
	public RemotePart(String name, double weightKg) throws RemoteException {
		this(-1, name, weightKg);
	}

	public RemotePart(PartDTO partDto) throws RemoteException {
		this(partDto.getId(), partDto.getName(), partDto.getWeightKg());
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
	public String getType() throws RemoteException {
		return name.substring(name.lastIndexOf(" ") + 1);
	}

	@Override
	public String toString() {
		return "RemotePart [id=" + id + ", name=" + name + ", weightKg=" + weightKg + "]";
	}

}
