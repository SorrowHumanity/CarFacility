package remote.domain.shipment;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import dto.shipment.ShipmentDTO;
import remote.domain.part.IPart;
import util.CollectionUtils;

public class RemoteShipment extends UnicastRemoteObject implements IShipment {

	private static final long serialVersionUID = 1L;

	private int id;
	private List<IPart> parts;
	private String receiverFirstName, receiverLastName;

	public RemoteShipment(int id, List<IPart> parts, String receiverFirstName, String receiverLastName)
			throws RemoteException {
		this.id = id;
		this.parts = parts;
		this.receiverFirstName = receiverFirstName;
		this.receiverLastName = receiverLastName;
	}

	public RemoteShipment(ShipmentDTO remoteShipment) throws RemoteException {
		this(remoteShipment.getId(), CollectionUtils.toRemotePartList(remoteShipment.getParts()),
				remoteShipment.getReceiverFirstName(), remoteShipment.getReceiverLastName());
	}

	@Override
	public int getId() throws RemoteException {
		return id;
	}

	@Override
	public List<IPart> getShippedParts() throws RemoteException {
		return parts;
	}

	@Override
	public String getReceiverFirstName() throws RemoteException {
		return receiverFirstName;
	}

	@Override
	public String getReceiverLastName() throws RemoteException {
		return receiverLastName;
	}

	@Override
	public String toString() {
		return "RemoteShipment [id=" + id + ", parts=" + parts + ", receiverFirstName=" + receiverFirstName
				+ ", receiverLastName=" + receiverLastName + "]";
	}

}
