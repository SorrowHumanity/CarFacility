package remote.model.shipment;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import remote.model.part.IPart;

public interface IShipment extends Remote{

	int getId() throws RemoteException;
	
	List<IPart> getShippedParts() throws RemoteException;

	String getReceiverFirstName() throws RemoteException;
	
	String getReceiverLastName() throws RemoteException;
}
