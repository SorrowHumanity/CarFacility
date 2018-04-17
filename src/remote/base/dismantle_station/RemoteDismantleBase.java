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
import remote.dao.pallet.RemotePalletDAOServer;
import remote.dao.part.IPartDAO;
import remote.dao.part.RemotePartDAOServer;
import remote.model.pallet.IPallet;
import remote.model.pallet.RemotePallet;
import remote.model.part.IPart;
import remote.model.part.RemotePart;

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
		PalletDTO palletDTO = palletDAO.create(palletType, toPartDTOList(parts));
		
		// create remote palet and cache it
		return palletCache.put(palletDTO.getId(), new RemotePallet(palletDTO));
	}

	@Override
	public IPallet getPallet(int palletId) throws RemoteException {
		// cache, if it is not already cached
		if (!palletCache.containsKey(palletId)) {
			// read pallet from database
			PalletDTO palletDTO = palletDAO.read(palletId);

			palletCache.put(palletDTO.getId(), new RemotePallet(palletDTO));
		}

		return palletCache.get(palletId);
	}

	private List<PartDTO> toPartDTOList(List<IPart> allParts) throws RemoteException {
		LinkedList<PartDTO> allPartDTOs = new LinkedList<>();
		
		for (IPart part : allParts)
			allPartDTOs.add(new PartDTO(part));
		
		return allPartDTOs;
	}

	public static void main(String[] args) throws RemoteException {
		IDismantleBase disBase = new RemoteDismantleBase(new RemotePartDAOServer(), new RemotePalletDAOServer());
		List<IPart> parts = disBase.getAllParts();
		System.out.println(parts);
	}

}
