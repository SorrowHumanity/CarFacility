package remote.base.dismantle_station;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import dto.pallet.PalletDTO;
import dto.part.PartDTO;
import remote.model.car.ICar;
import remote.model.part.IPart;

public interface IDismantleBase extends Remote {

	PartDTO[] dismantleCar(ICar car) throws RemoteException;

	PartDTO registerPart(String carChassisNumber, String name, double weight) throws RemoteException;

	PartDTO[] getParts(String carChassisNumber) throws RemoteException;

	PartDTO[] getParts(int palletId) throws RemoteException;

	PartDTO[] getAllParts() throws RemoteException;

	PalletDTO registerPallet(String palletType, List<IPart> parts) throws RemoteException;

	PalletDTO getPallet(int id) throws RemoteException;

	PalletDTO[] getAllPallets() throws RemoteException;

	boolean addToPallet(IPart carParts) throws RemoteException;

}
