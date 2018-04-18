package remote.base.dismantle_station;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import dto.pallet.PalletDTO;
import dto.part.PartDTO;
import remote.dao.pallet.IPalletDAO;
import remote.dao.part.IPartDAO;
import remote.model.car.ICar;
import remote.model.pallet.IPallet;
import remote.model.pallet.RemotePallet;
import remote.model.part.IPart;
import remote.model.part.RemotePart;
import util.Utils;

public class RemoteDismantleBase extends UnicastRemoteObject implements IDismantleBase {

	private static final long serialVersionUID = 1L;

	private Map<Integer, IPart> partCache = new HashMap<>();
	private Map<Integer, IPallet> palletCache = new HashMap<>();

	private IPartDAO partDao;
	private IPalletDAO palletDao;

	public RemoteDismantleBase(IPartDAO partDao, IPalletDAO palletDao) throws RemoteException {
		this.partDao = partDao;
		this.palletDao = palletDao;
	}

	@Override
	public IPart registerPart(String carChassisNumber, String name, double weightKg) throws RemoteException {
		// create part in the database
		PartDTO partDto = partDao.create(carChassisNumber, name, weightKg);

		// cache & return
		partCache.put(partDto.getId(), new RemotePart(partDto));

		return partCache.get(partDto.getId());
	}

	@Override
	public List<IPart> getParts(String carChassisNumber) throws RemoteException {
		// read all parts from the database
		Collection<PartDTO> allParts = partDao.read(carChassisNumber);

		// create output collection
		LinkedList<IPart> matchingParts = new LinkedList<>();

		// go through all parts
		for (PartDTO part : allParts) {

			// cache, if it is not already cached
			if (!partCache.containsKey(part.getId())) 
				partCache.put(part.getId(), new RemotePart(part));

			// add to output collection
			matchingParts.add(partCache.get(part.getId()));
		}

		return matchingParts;
	}

	@Override
	public List<IPart> getParts(int palletId) throws RemoteException {
		// read all parts from the database
		Collection<PartDTO> allParts = partDao.read(palletId);

		// create output collection
		LinkedList<IPart> matchingParts = new LinkedList<>();

		// go through all parts
		for (PartDTO part : allParts) {

			// cache, if it is not already cached
			if (!partCache.containsKey(part.getId())) 
				partCache.put(part.getId(), new RemotePart(part));

			// add to output collection
			matchingParts.add(partCache.get(part.getId()));
		}

		return matchingParts;
	}

	@Override
	public List<IPart> getAllParts() throws RemoteException {
		// read all parts from the database
		Collection<PartDTO> allParts = partDao.readAll();

		// create output collection
		LinkedList<IPart> matchingParts = new LinkedList<>();

		// go through all parts
		for (PartDTO part : allParts) {

			// cache, if it is not already cached
			if (!partCache.containsKey(part.getId()))
				partCache.put(part.getId(), new RemotePart(part));

			// add to output collection
			matchingParts.add(partCache.get(part.getId()));
		}

		return matchingParts;
	}

	@Override
	public IPallet registerPallet(String palletType, List<IPart> parts) throws RemoteException {
		// create pallet in the database
		PalletDTO palletDto = palletDao.create(palletType, Utils.toDTOArray(parts));

		// create remote pallet and cache it
		palletCache.put(palletDto.getId(), new RemotePallet(palletDto));

		return palletCache.get(palletDto.getId());
	}

	@Override
	public IPallet getPallet(int palletId) throws RemoteException {
		// cache, if it is not already cached
		if (!palletCache.containsKey(palletId)) {

			// read pallet from database
			PalletDTO palletDto = palletDao.read(palletId);

			// cache pallet
			palletCache.put(palletDto.getId(), new RemotePallet(palletDto));
		}

		return palletCache.get(palletId);
	}

	@Override
	public List<IPallet> getAllPallets() throws RemoteException {
		// read all parts from the database
		Collection<PalletDTO> allPallets = palletDao.readAll();

		// create output collection
		LinkedList<IPallet> matchingParts = new LinkedList<>();

		// go through all parts
		for (PalletDTO pallet : allPallets) {

			// cache, if it is not already cached
			if (!palletCache.containsKey(pallet.getId()))
				palletCache.put(pallet.getId(), new RemotePallet(pallet));

			// add to output collection
			matchingParts.add(palletCache.get(pallet.getId()));
		}

		return matchingParts;
	}

	@Override
	public List<IPart> dismantleCar(ICar car) throws RemoteException {
		// create output collection
		LinkedList<IPart> carParts = new LinkedList<>();

		// register all parts
		for (IPart part : car.getParts()) {

			// register part
			IPart remotePart = registerPart(part.getCarChassisNumber(), part.getName(), part.getWeightKg());

			// add part to output list
			carParts.add(remotePart);

			// add part to pallet
			addToPallet(remotePart);
		}

		return carParts;
	}
	
	private boolean addToPallet(IPart part) throws RemoteException {
		// part is too heavy for standart pallets
		if (isOverweight(part)) return false;
		
		// get all available pallets
		getAllPallets();
	
		// attempt to add to existing pallet
		for (Map.Entry<Integer, IPallet> entry : palletCache.entrySet()) {
			IPallet pallet = entry.getValue();
	
			if (pallet.fits(part)) {
				// add part to pallet
				pallet.addPart(part);
				
				// update database
				palletDao.update(new PalletDTO(pallet));
		
				// update cache
				palletCache.put(pallet.getId(), pallet);
				return true;
			}
		}
	
		// if there is no extinsting pallet that fits, create a new one
		LinkedList<IPart> palletParts = new LinkedList<>();
		palletParts.add((part));
		registerPallet(part.getType(), palletParts);
	
		return true;
	}

	private boolean isOverweight(IPart part) throws RemoteException {
		boolean isOverweight = Double.compare(part.getWeightKg(), PalletDTO.MAX_PALLET_WEIGHT_KG) <= 0;
		return isOverweight;
	}

}
