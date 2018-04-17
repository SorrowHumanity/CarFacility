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
import remote.model.pallet.IPallet;
import remote.model.pallet.RemotePallet;
import remote.model.part.IPart;
import remote.model.part.RemotePart;
import util.CarFacilityUtils;

public class RemoteDismantleStationBase extends UnicastRemoteObject implements IDismantleStation {

	private static final long serialVersionUID = 1L;

	private Map<Integer, IPart> partCache = new HashMap<>();
	private Map<Integer, IPallet> palletCache = new HashMap<>();

	private IPartDAO partDAO;
	private IPalletDAO palletDAO;

	public RemoteDismantleStationBase(IPartDAO partDAO, IPalletDAO palletDAO) throws RemoteException {
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

}
