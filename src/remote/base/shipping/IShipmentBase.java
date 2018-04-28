package remote.base.shipping;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import remote.model.part.IPart;
import remote.model.shipment.IShipment;

public interface IShipmentBase extends Remote {

	/**
	 * Registers a new shipment in the system
	 * 
	 * @param parts
	 *            the parts of the shipment
	 * @param receiverFirstName
	 *            the first name of the receiver
	 * @param receiverLastName
	 *            the last name of the receiver
	 * @return a remote shipment object
	 * @throws RemoteException
	 **/
	IShipment registerShipment(List<IPart> parts, String receiverFirstName, String receiverLastName)
			throws RemoteException;

	/**
	 * Retrieves the shipment with the id passed as a parameter
	 * 
	 * @param shipmentId
	 *            the id of the shipment
	 * @return the shipment
	 * @throws RemoteException
	 **/
	IShipment getShipment(int shipmentId) throws RemoteException;

	/**
	 * Retrieves all shipments registered in the system
	 * 
	 * @return a list of shipments
	 * @throws RemoteException
	 **/
	List<IShipment> getAllShipments() throws RemoteException;
}
