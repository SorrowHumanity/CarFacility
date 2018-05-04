package remote.base.dismantle;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
import util.CollectionUtils;

public class RemoteDismantleBase extends UnicastRemoteObject implements IDismantleBase {
	// the methods here are not synchronized, because some of them are using eachother
	
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

		IPart remotePart = new RemotePart(partDto);
		
		// get all pallets, in case there is an existing pallet that fits the part
		getAllPallets();
		
		// distribute part to a pallet
		addToPallet(remotePart);
		
		// cache & return
		partCache.put(remotePart.getId(), remotePart);
		
		return remotePart;
	}

	@Override
	public List<IPart> getParts(String carChassisNumber) throws RemoteException {
		// read all parts from the database
		Collection<PartDTO> allParts = partDao.readCarParts(carChassisNumber);

		// create output collection
		int size = allParts.size();
		ArrayList<IPart> matchingParts = new ArrayList<>(size); // avoid arraylist resizing

		for (PartDTO part : allParts) {
			// cache, if part is not already cached
			if (!partCache.containsKey(part.getId())) 
				partCache.put(part.getId(), new RemotePart(part));

			matchingParts.add(partCache.get(part.getId()));
		}

		return matchingParts;
	}

	@Override
	public List<IPart> getParts(int palletId) throws RemoteException {
		// read all parts from the database
		Collection<PartDTO> allParts = partDao.readPalletParts(palletId);

		// create output collection
		int size = allParts.size();
		ArrayList<IPart> matchingParts = new ArrayList<>(size); // avoid arraylist resizing

		for (PartDTO part : allParts) {
			// cache, if part is not already cached
			if (!partCache.containsKey(part.getId())) 
				partCache.put(part.getId(), new RemotePart(part));

			matchingParts.add(partCache.get(part.getId()));
		}

		return matchingParts;
	}

	@Override
	public List<IPart> getAllParts() throws RemoteException {
		// read all parts from the database
		Collection<PartDTO> allParts = partDao.readAll();

		// create output collection
		int size = allParts.size();
		ArrayList<IPart> partList = new ArrayList<>(size);

		for (PartDTO part : allParts) {
			// cache, if it is not already cached
			if (!partCache.containsKey(part.getId()))
				partCache.put(part.getId(), new RemotePart(part));

			// add to output collection
			partList.add(partCache.get(part.getId()));
		}

		return partList;
	}

	@Override
	public IPallet registerPallet(String palletType, List<IPart> parts) throws RemoteException {
		// create pallet in the database
		PalletDTO palletDto = palletDao.create(palletType, CollectionUtils.toDTOList(parts));
		
		// cache and return
		palletCache.put(palletDto.getId(), new RemotePallet(palletDto));

		return palletCache.get(palletDto.getId());
	}

	@Override
	public IPallet getPallet(int palletId) throws RemoteException {
		// check if pallet is already cached
		if (!palletCache.containsKey(palletId)) {
			
			// read pallet from database
			PalletDTO palletDto = palletDao.read(palletId);

			// avoid caching null values
			if (palletDto == null) 
				throw new IllegalArgumentException("Pallet with id " + palletId + " does not exist!");
			
			palletCache.put(palletDto.getId(), new RemotePallet(palletDto));
		}

		return palletCache.get(palletId);
	}

	@Override
	public List<IPallet> getAllPallets() throws RemoteException {
		// read all pallets from the database
		Collection<PalletDTO> allPallets = palletDao.readAll();

		// create output collection
		int size = allPallets.size();
		ArrayList<IPallet> partList = new ArrayList<>(size);

		for (PalletDTO pallet : allPallets) {
			// cache, if pallet is not already cached
			if (!palletCache.containsKey(pallet.getId()))
				palletCache.put(pallet.getId(), new RemotePallet(pallet));

			partList.add(palletCache.get(pallet.getId()));
		}

		return partList;
	}

	@Override
	public List<IPart> dismantleCar(ICar car) throws RemoteException {
		// create output collection
		int size = car.getParts().size();
		ArrayList<IPart> carParts = new ArrayList<>(size);
		
		// register all parts & add them to output collection
		for (IPart part : car.getParts()) {
			IPart remotePart = registerPart(car.getChassisNumber(),
					part.getName(), part.getWeightKg());
			carParts.add(remotePart);
		}

		return carParts;
	}
	
	@Override
	public int removeFromPallet(IPart part) throws RemoteException {
		// get all available pallets
		getAllPallets();
		
		// find the pallet that contains the part
		for (Map.Entry<Integer, IPallet> entry : palletCache.entrySet()) {
			IPallet pallet = entry.getValue();
			
			if (pallet != null && pallet.containsPart(part)) {
				// remove part from pallet
				pallet.removePart(part);
				
				// update database
				palletDao.update(new PalletDTO(pallet));
				palletDao.removePartAssociations(pallet.getId(), new PartDTO(part));
				
				// update cache
				palletCache.put(pallet.getId(), pallet);
				partCache.remove(part.getId(), part);
				
				// return pallet id
				return pallet.getId();
			}
		}
		
		return -1;
	}

	/**
	 * Adds a part to a pallet. If there is no existing pallet that fits the part,
	 * a new pallet is created and registered for the part
	 * 
	 *  @param part
	 *  		the part
	 *  @return true, if the part is successfully added to a pallet. Otherwise, the part is 
	 *  			too heavy (above 1000 kg) for the standard pallets
	 * @throws RemoteException
	 **/
	private boolean addToPallet(IPart part) throws RemoteException {
		// part is too heavy for standard pallets
		if (isOverweight(part)) return false;
		
		// attempt to add to existing pallet
		for (Map.Entry<Integer, IPallet> entry : palletCache.entrySet()) {
			IPallet pallet = entry.getValue();
			
			if (pallet != null && pallet.fits(part)) {
				// add part to pallet
				pallet.addPart(part);
				
				// update database
				palletDao.update(new PalletDTO(pallet));
		
				// update cache
				palletCache.put(pallet.getId(), pallet);
				
				return true;
			}
		}
	
		// if there is no existing pallet that fits, create a new one
		ArrayList<IPart> palletParts = new ArrayList<>();
		palletParts.add(part);
		registerPallet(part.getType(), palletParts);
		
		return true;
	}
	
	/**
	 * Verifies that the part passed as a parameter fits the weight limits of the standards pallets
	 * 
	 * @param part 
	 * 			the part
	 * @return true, if the part fits the weight standards. Otherwise, false 
	 * @throws RemoteException
	 **/
	private boolean isOverweight(IPart part) throws RemoteException {
		boolean isOverweight = Double.compare(part.getWeightKg(),
				RemotePallet.MAX_PALLET_WEIGHT_CAPACITY) >= 0;
		return isOverweight;
	}

}
