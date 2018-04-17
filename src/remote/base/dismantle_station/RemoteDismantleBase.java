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
import util.CarFacilityUtils;

public class RemoteDismantleBase extends UnicastRemoteObject implements IDismantleBase {

	private static final long serialVersionUID = 1L;

	private Map<Integer, IPart> partCache = new HashMap<>();
	private Map<Integer, IPallet> palletCache = new HashMap<>();

	private IPartDAO partDAO;
	private IPalletDAO palletDAO;

	public RemoteDismantleBase(IPartDAO partDAO, IPalletDAO palletDAO) throws RemoteException {
		this.partDAO = partDAO;
		this.palletDAO = palletDAO;
	}

	@Override
	public IPart registerPart(String carChassisNumber, String name, double weight) throws RemoteException {
		// create part in the database
		PartDTO partDTO = partDAO.create(carChassisNumber, name, weight);

		// cache & return the remote part object;
		return partCache.put(partDTO.getId(), new RemotePart(partDTO));
	}

	@Override
	public List<IPart> getParts(String carChassisNumber) throws RemoteException {
		// read all parts from the database
		Collection<PartDTO> parts = partDAO.read(carChassisNumber);

		// create output collection
		LinkedList<IPart> matchingParts = new LinkedList<>();

		// go through all parts
		for (PartDTO dto : parts) {

			// cache, if it is not already cached
			if (!partCache.containsKey(dto.getId())) {
				partCache.put(dto.getId(), new RemotePart(dto));
			}

			// add to output collection
			matchingParts.add(partCache.get(dto.getId()));
		}

		return matchingParts;
	}

	@Override
	public List<IPart> getParts(int palletId) throws RemoteException {
		// read all parts from the database
		Collection<PartDTO> parts = partDAO.read(palletId);

		// create output collection
		LinkedList<IPart> matchingParts = new LinkedList<>();

		// go through all parts
		for (PartDTO dto : parts) {

			// cache, if it is not already cached
			if (!partCache.containsKey(dto.getId())) {
				partCache.put(dto.getId(), new RemotePart(dto));
			}

			// add to output collection
			matchingParts.add(partCache.get(dto.getId()));
		}

		return matchingParts;
	}

	@Override
	public List<IPart> getAllParts() throws RemoteException {
		// read all parts from the database
		Collection<PartDTO> parts = partDAO.readAll();

		// create output collection
		LinkedList<IPart> matchingParts = new LinkedList<>();

		// go through all parts
		for (PartDTO dto : parts) {

			// cache, if it is not already cached
			if (!partCache.containsKey(dto.getId())) {
				partCache.put(dto.getId(), new RemotePart(dto));
			}

			// add to output collection
			matchingParts.add(partCache.get(dto.getId()));
		}

		return matchingParts;
	}

	@Override
	public IPallet registerPallet(String palletType, List<IPart> parts) throws RemoteException {
		// create pallet in the database
		PalletDTO palletDTO = palletDAO.create(palletType, CarFacilityUtils.toDTOParts(parts));

		// create remote palet and cache it
		return palletCache.put(palletDTO.getId(), new RemotePallet(palletDTO));
	}

	@Override
	public boolean addToPallet(IPart part) throws RemoteException {
		// get all available pallets
		getAllPallets();

		// attempt to add to existing pallet
		for (Map.Entry<Integer, IPallet> entry : palletCache.entrySet()) {
			IPallet pallet = entry.getValue();
			if (pallet.palletFits(part)) {
				pallet.addPart(part);
				palletDAO.update(new PalletDTO(pallet));
				return true;
			}
		}

		// if there is no extinsting pallet that fits, create a new one
		LinkedList<IPart> partDTOs = new LinkedList<>();
		partDTOs.add((part));
		registerPallet(part.getType(), partDTOs);
		
		return true;
	}

	@Override
	public IPallet getPallet(int palletId) throws RemoteException {
		// cache, if it is not already cached
		if (!palletCache.containsKey(palletId)) {

			// read pallet from database
			PalletDTO palletDTO = palletDAO.read(palletId);

			// cache pallet
			palletCache.put(palletDTO.getId(), new RemotePallet(palletDTO));
		}

		return palletCache.get(palletId);
	}

	@Override
	public List<IPallet> getAllPallets() throws RemoteException {
		// read all parts from the database
		Collection<PalletDTO> allPallets = palletDAO.readAll();

		// create output collection
		LinkedList<IPallet> palletList = new LinkedList<>();

		// go through all parts
		for (PalletDTO pallet : allPallets) {

			// cache, if it is not already cached
			if (!palletCache.containsKey(pallet.getId())) {
				palletCache.put(pallet.getId(), new RemotePallet(pallet));
			}

			// add to output collection
			palletList.add(palletCache.get(pallet.getId()));
		}

		return palletList;
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

}
