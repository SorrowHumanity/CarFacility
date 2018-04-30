package service.dismantle_station;

import java.rmi.RemoteException;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;
import dto.car.CarDTO;
import dto.pallet.PalletDTO;
import dto.part.PartDTO;
import remote.base.dismantle.RemoteDismantleBaseLocator;
import remote.base.dismantle.IDismantleBase;
import remote.model.car.RemoteCar;
import remote.model.pallet.IPallet;
import remote.model.part.IPart;
import util.CollectionUtils;

@WebService
public class DismantleStationService implements IDismantleStationService {

	private IDismantleBase dismantleBase;

	public DismantleStationService() throws RemoteException {
		try {
			dismantleBase = RemoteDismantleBaseLocator.lookupBase();
		} catch (Exception e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

	@WebMethod
	@Override
	public PartDTO[] dismantleCar(CarDTO car) throws RemoteException {
		List<IPart> remoteParts = dismantleBase.dismantleCar(new RemoteCar(car));
		PartDTO[] partDtos = CollectionUtils.toDTOArray(remoteParts); 
		return partDtos;
	}

	@WebMethod
	@Override
	public PartDTO registerPart(String carChassisNumber, String name, double weight) throws RemoteException {
		IPart part = dismantleBase.registerPart(carChassisNumber, name, weight);
		PartDTO partDto = new PartDTO(part); 
		return partDto;
	}

	@WebMethod
	@Override
	public PartDTO[] getParts(String carChassisNumber) throws RemoteException {
		List<IPart> remoteParts = dismantleBase.getParts(carChassisNumber);
		PartDTO[] partDtos = CollectionUtils.toDTOArray(remoteParts); 
		return partDtos;
	}

	@WebMethod
	@Override
	public PartDTO[] getPalletParts(int palletId) throws RemoteException {
		List<IPart> remoteParts = dismantleBase.getParts(palletId);
		PartDTO[] partDtos = CollectionUtils.toDTOArray(remoteParts); 
		return partDtos;
	}

	@WebMethod
	@Override
	public PartDTO[] getAllParts() throws RemoteException {
		List<IPart> remoteParts = dismantleBase.getAllParts();
		PartDTO[] partDtos = CollectionUtils.toDTOArray(remoteParts); 
		return partDtos;
	}

	@WebMethod
	@Override
	public PalletDTO registerPallet(String palletType, PartDTO[] parts) throws RemoteException {
		List<IPart> remoteParts = CollectionUtils.toRemoteList(parts);
		IPallet pallet = dismantleBase.registerPallet(palletType, remoteParts);
		PalletDTO palletDto = new PalletDTO(pallet); 
		return palletDto;
	}

	@WebMethod
	@Override
	public PalletDTO getPallet(int palletId) throws RemoteException {
		IPallet pallet = dismantleBase.getPallet(palletId);
		PalletDTO palletDto = new PalletDTO(pallet); 
		return palletDto;
	}

	@WebMethod
	@Override
	public PalletDTO[] getAllPallets() throws RemoteException {
		List<IPallet> remotePallets = dismantleBase.getAllPallets();
		PalletDTO[] allPallets = CollectionUtils.toPalletDTOArray(remotePallets);
		return allPallets;
	}

}
