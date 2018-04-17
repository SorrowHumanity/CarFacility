package remote.base.dismantle_station;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import remote.model.car.ICar;
import remote.model.pallet.IPallet;
import remote.model.part.IPart;

public interface IDismantleBase extends Remote {

	List<IPart> dismantleCar(ICar car) throws RemoteException;
	
	IPart registerPart(String carChassisNumber, String name, double weight) throws RemoteException;

	List<IPart> getParts(String carChassisNumber) throws RemoteException;
	
	List<IPart> getParts(int palletId) throws RemoteException;
	
	List<IPart> getAllParts() throws RemoteException;

	IPallet registerPallet(String palletType, List<IPart> parts) throws RemoteException;

	IPallet getPallet(int id) throws RemoteException;
	
	List<IPallet> getAllPallets() throws RemoteException;
	
	boolean addToPallet(IPart carParts) throws RemoteException;

}
