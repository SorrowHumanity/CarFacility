package remote.model.shipment;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import remote.model.part.IPart;

public interface IShipment extends Remote {

	/**
	 * @return the id value
	 * @throws RemoteException
	 **/
	int getId() throws RemoteException;

	/**
	 * @return a list of parts belonging to the shipment
	 * @throws RemoteException
	 **/
	List<IPart> getShippedParts() throws RemoteException;

	/**
	 * @return the reciever first name value
	 * @throws RemoteException
	 **/
	String getReceiverFirstName() throws RemoteException;

	/**
	 * @return the reciever last name value
	 * @throws RemoteException
	 **/
	String getReceiverLastName() throws RemoteException;
}
