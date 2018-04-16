package remote.base.dismantle;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import dto.part.PartDTO;
import remote.dao.part.IPartDAO;
import remote.dao.part.PartDAOServer;
import remote.model.pallet.IPallet;
import remote.model.part.IPart;
import remote.model.part.RemotePart;

public class RemoteDismantleBase extends UnicastRemoteObject implements IDismantleBase {

	private static final long serialVersionUID = 1L;

	private Map<Integer, IPart> partCache = new HashMap<>();
	private IPartDAO partDAO;

	public RemoteDismantleBase(IPartDAO partDAO) throws RemoteException {
		this.partDAO = partDAO;
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
		Collection<PartDTO> parts = partDAO.readAll(carChassisNumber);
		
		// create output collection
		LinkedList<IPart> matchingParts = new LinkedList<>();
		
		// go through all parts & cache the ones that are not already
		for (PartDTO dto : parts) {
			
			if (!partCache.containsKey(dto.getId())) {
				partCache.put(dto.getId(), new RemotePart(dto));
			}
			
			// add to output collection
			matchingParts.add(partCache.get(dto.getId()));
		}
		
		return matchingParts;
	}

	@Override
	public IPallet registerPallet(int id, String palletType, List<IPart> parts) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPallet getPallet(int id) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static void main(String[] args) throws RemoteException {
		IPartDAO dao = new PartDAOServer();
		IDismantleBase disBase = new RemoteDismantleBase(dao);
		List<IPart> parts = disBase.getParts("123456");
		System.out.println(parts);
	}

}
