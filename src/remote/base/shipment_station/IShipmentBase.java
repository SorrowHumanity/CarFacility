package remote.base.shipment_station;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import dto.part.PartDTO;
import remote.model.shipment.IShipment;

public interface IShipmentBase extends Remote {

	IShipment registerShipment(List<PartDTO> parts, String receiverFirstName, String receiverLastName)
			throws RemoteException;

	IShipment getShipment(int id) throws RemoteException;

	List<IShipment> getAllShipments() throws RemoteException;
}
