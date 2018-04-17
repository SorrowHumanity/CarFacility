package remote.model.part;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IPart extends Remote {

	int getId() throws RemoteException;
	
	String getCarChassisNumber() throws RemoteException;
	
	String getName() throws RemoteException;

	double getWeightKg() throws RemoteException;
	
	String getType() throws RemoteException;

}
