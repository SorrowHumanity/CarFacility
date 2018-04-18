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

	private IPartDAO partDAO;
	private IPalletDAO palletDAO;

	public RemoteDismantleBase(IPartDAO partDAO, IPalletDAO palletDAO) throws RemoteException {
		this.partDAO = partDAO;
		this.palletDAO = palletDAO;
	}

	@Override
	public PartDTO registerPart(String carChassisNumber, String name, double weight) throws RemoteException {
		// create part in the database
		PartDTO partDTO = partDAO.create(carChassisNumber, name, weight);

		// cache
		partCache.put(partDTO.getId(), new RemotePart(partDTO));
	
		return partDTO;
	}

	@Override
	public PartDTO[] getParts(String carChassisNumber) throws RemoteException {
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

		return Utils.toDTOPartsArray(matchingParts);
	}

	@Override
	public PartDTO[] getParts(int palletId) throws RemoteException {
		// read all parts from the database
		Collection<PartDTO> allParts = partDAO.read(palletId);

		// create output collection
		LinkedList<PartDTO> output = new LinkedList<>();

		// go through all parts
		for (PartDTO part : allParts) {

			// cache, if it is not already cached
			if (!partCache.containsKey(part.getId())) {
				partCache.put(part.getId(), new RemotePart(part));
			}

			// add to output collection
			output.add(part);
		}

		return Utils.toArray(output);
	}

	@Override
	public PartDTO[] getAllParts() throws RemoteException {
		// read all parts from the database
		Collection<PartDTO> allParts = partDAO.readAll();

		// create output collection
		LinkedList<PartDTO> output = new LinkedList<>();

		// go through all parts
		for (PartDTO part : allParts) {

			// cache, if it is not already cached
			if (!partCache.containsKey(part.getId())) {
				partCache.put(part.getId(), new RemotePart(part));
			}

			// add to output collection
			output.add(part);
		}

		return Utils.toArray(output);
	}

	@Override
	public PalletDTO registerPallet(String palletType, List<IPart> parts) throws RemoteException {
		// create pallet in the database
		PalletDTO palletDTO = palletDAO.create(palletType, Utils.toDTOPartsArray(parts));

		// create remote palet and cache it
		palletCache.put(palletDTO.getId(), new RemotePallet(palletDTO));
	
		return palletDTO;
	}

	@Override
	public boolean addToPallet(IPart part) throws RemoteException {
		// get all available pallets
		getAllPallets();

		// attempt to add to existing pallet
		for (Map.Entry<Integer, IPallet> entry : palletCache.entrySet()) {
			IPallet pallet = entry.getValue();
			System.out.println(pallet);
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
	public PalletDTO getPallet(int palletId) throws RemoteException {
		// cache, if it is not already cached
		if (!palletCache.containsKey(palletId)) {

			// read pallet from database
			PalletDTO palletDTO = palletDAO.read(palletId);

			// cache pallet
			palletCache.put(palletDTO.getId(), new RemotePallet(palletDTO));
		}

		return new PalletDTO(palletCache.get(palletId));
	}

	@Override
	public PalletDTO[] getAllPallets() throws RemoteException {
		// read all parts from the database
		Collection<PalletDTO> allPallets = palletDAO.readAll();

		// create output collection
		LinkedList<PalletDTO> output = new LinkedList<>();

		// go through all parts
		for (PalletDTO pallet : allPallets) {

			// cache, if it is not already cached
			if (!palletCache.containsKey(pallet.getId())) {
				palletCache.put(pallet.getId(), new RemotePallet(pallet));
			}

			// add to output collection
			output.add(pallet);
		}

		return Utils.toArray(output);
	}

	@Override
	public PartDTO[] dismantleCar(ICar car) throws RemoteException {
		// create output collection
		LinkedList<PartDTO> carParts = new LinkedList<>();

		// register all parts
		for (IPart part : car.getParts()) {
			// register part
			PartDTO remotePart = registerPart(part.getCarChassisNumber(), part.getName(), part.getWeightKg());
			
			// add part to output list
			carParts.add(remotePart);
			
			// add part to pallet
			addToPallet(new RemotePart(remotePart));
		}

		return Utils.toArray(carParts);
	}

}
