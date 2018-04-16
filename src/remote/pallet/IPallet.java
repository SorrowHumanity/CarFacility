package remote.pallet;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import remote.part.IPart;

public interface IPallet extends Remote {

	int getId() throws RemoteException;

	String getPalletType() throws RemoteException;

	List<IPart> getParts() throws RemoteException;

}
