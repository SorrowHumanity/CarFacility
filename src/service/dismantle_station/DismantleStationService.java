package service.dismantle_station;

import java.rmi.RemoteException;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import dto.car.CarDTO;
import dto.pallet.PalletDTO;
import dto.part.PartDTO;
import remote.base.dismantle_station.DismantleBaseLocator;
import remote.base.dismantle_station.IDismantleBase;
import remote.model.car.RemoteCar;
import remote.model.part.RemotePart;
import util.Utils;

@WebService
public class DismantleStationService {

	private IDismantleBase dismantleBase;

	public DismantleStationService() throws RemoteException {
		dismantleBase = DismantleBaseLocator.lookupBase(DismantleBaseLocator.DISMANTLE_BASE_ID);
	}

	@WebMethod
	public PartDTO[] dismantleCar(CarDTO car) throws RemoteException {
		return dismantleBase.dismantleCar(new RemoteCar(car));
	}

	@WebMethod
	public PartDTO registerPart(String carChassisNumber, String name, double weight) throws RemoteException {
		return dismantleBase.registerPart(carChassisNumber, name, weight);
	}

	@WebMethod
	public PartDTO[] getParts(String carChassisNumber) throws RemoteException {
		return dismantleBase.getParts(carChassisNumber);
	}

	@WebMethod
	public PartDTO[] getParts(int palletId) throws RemoteException {
		return dismantleBase.getParts(palletId);
	}

	@WebMethod
	public PartDTO[] getAllParts() throws RemoteException {
		return dismantleBase.getAllParts();
	}

	@WebMethod
	public PalletDTO registerPallet(String palletType, List<PartDTO> parts) throws RemoteException {
		return dismantleBase.registerPallet(palletType, Utils.toRemotePartsList(parts));
	}

	@WebMethod
	public PalletDTO getPallet(int id) throws RemoteException {
		return dismantleBase.getPallet(id);
	}

	@WebMethod
	public PalletDTO[] getAllPallets() throws RemoteException {
		return dismantleBase.getAllPallets();
	}

	@WebMethod
	public boolean addToPallet(PartDTO part) throws RemoteException {
		return dismantleBase.addToPallet(new RemotePart(part));
	}

}
