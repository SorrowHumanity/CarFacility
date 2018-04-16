package remote.base.part;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import remote.pallet.IPallet;
import remote.part.IPart;

public interface DismantleBase extends Remote {

	IPart registerPart(String name, double weight) throws RemoteException;

	List<IPart> getParts(String chassisNumber) throws RemoteException;

	IPallet registerPallet(int id, String palletType, List<IPart> parts) throws RemoteException;

	IPallet getPallet(int id) throws RemoteException;

}
