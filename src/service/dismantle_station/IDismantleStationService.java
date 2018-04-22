package service.dismantle_station;

import java.rmi.RemoteException;
import java.util.List;

import dto.car.CarDTO;
import dto.pallet.PalletDTO;
import dto.part.PartDTO;

public interface IDismantleStationService {

	PartDTO[] dismantleCar(CarDTO car) throws RemoteException;

	PartDTO registerPart(String carChassisNumber, String name, double weight) throws RemoteException;

	PartDTO[] getParts(String carChassisNumber) throws RemoteException;

	PartDTO[] getParts(int palletId) throws RemoteException;

	PartDTO[] getAllParts() throws RemoteException;

	PalletDTO registerPallet(String palletType, List<PartDTO> parts) throws RemoteException;

	PalletDTO getPallet(int palletId) throws RemoteException;

	PalletDTO[] getAllPallets() throws RemoteException;

}
