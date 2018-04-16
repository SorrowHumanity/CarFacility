package remote.model.pallet;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import remote.model.part.IPart;

public interface IPallet extends Remote {

	int getId() throws RemoteException;

	String getPalletType() throws RemoteException;

	List<IPart> getParts() throws RemoteException;
	
	double getTotalWeight() throws RemoteException;

}
