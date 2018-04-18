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
import remote.model.pallet.IPallet;
import remote.model.part.IPart;
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
		List<IPart> remoteParts = dismantleBase.dismantleCar(new RemoteCar(car));
		return Utils.toDTOPartsArray(remoteParts);
	}

	@WebMethod
	public PartDTO registerPart(String carChassisNumber, String name, double weight) throws RemoteException {
		return new PartDTO(dismantleBase.registerPart(carChassisNumber, name, weight));
	}

	@WebMethod
	public PartDTO[] getParts(String carChassisNumber) throws RemoteException {
		List<IPart> remoteParts = dismantleBase.getParts(carChassisNumber);
		return Utils.toDTOPartsArray(remoteParts);
	}

	@WebMethod
	public PartDTO[] getParts(int palletId) throws RemoteException {
		List<IPart> remoteParts = dismantleBase.getParts(palletId); 
		return Utils.toDTOPartsArray(remoteParts);
	}

	@WebMethod
	public PartDTO[] getAllParts() throws RemoteException {
		List<IPart> remoteParts = dismantleBase.getAllParts(); 
		return Utils.toDTOPartsArray(remoteParts);
	}

	@WebMethod
	public PalletDTO registerPallet(String palletType, List<PartDTO> parts) throws RemoteException {
		return new PalletDTO(dismantleBase.registerPallet(palletType, Utils.toRemotePartsList(parts)));
	}

	@WebMethod
	public PalletDTO getPallet(int id) throws RemoteException {
		return new PalletDTO(dismantleBase.getPallet(id));
	}

	@WebMethod
	public PalletDTO[] getAllPallets() throws RemoteException {
		List<IPallet> remotePallets = dismantleBase.getAllPallets();
		return Utils.toPalletDTOArray(remotePallets);
	}

	@WebMethod
	public boolean addToPallet(PartDTO part) throws RemoteException {
		return dismantleBase.addToPallet(new RemotePart(part));
	}

}
