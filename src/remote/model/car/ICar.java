package remote.model.car;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import remote.model.part.IPart;

public interface ICar extends Remote {

	String getChassisNumber() throws RemoteException;
	
	String getModel() throws RemoteException;
	
	List<IPart> getParts() throws RemoteException;
	
	double getWeight() throws RemoteException;
	
}
