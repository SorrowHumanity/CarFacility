package remote.base.dismantle;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import remote.pallet.IPallet;
import remote.part.IPart;

public interface IDismantleBase extends Remote {

	IPart registerPart(String carChassisNumber, String name, double weight) throws RemoteException;

	List<IPart> getParts(String carChassisNumber) throws RemoteException;

	IPallet registerPallet(int id, String palletType, List<IPart> parts) throws RemoteException;

	IPallet getPallet(int id) throws RemoteException;

}
