package remote.base.shipping;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import dto.part.PartDTO;
import dto.shipment.ShipmentDTO;
import remote.base.dismantle.IDismantleBase;
import remote.dao.shipment.IShipmentDAO;
import remote.domain.part.IPart;
import remote.domain.shipment.IShipment;
import remote.domain.shipment.RemoteShipment;
import util.CollectionUtils;

public class RemoteShipmentBase extends UnicastRemoteObject implements IShipmentBase {

	private static final long serialVersionUID = 1L;

	private Map<Integer, IShipment> shipmentCache;
	
	private IShipmentDAO shipmentDao;
	private IDismantleBase dismantleBase;

	public RemoteShipmentBase(IShipmentDAO shipmentDao, IDismantleBase dismantleBase) throws RemoteException {
		this.shipmentDao = shipmentDao;
		this.dismantleBase = dismantleBase;
		this.shipmentCache = new HashMap<>();
	}

	@Override
	public synchronized IShipment registerShipment(List<IPart> parts, String receiverFirstName, String receiverLastName)
			throws RemoteException {
		// get map of the pallet ids of the pallets from which the parts come
		// key(part id), value(pallet id)
		Map<Integer, Integer> palletIds = takeParts(parts);

		List<PartDTO> partDtos = CollectionUtils.toPartDTOList(parts);
		
		ShipmentDTO shipmentDto = shipmentDao.create(receiverFirstName, receiverLastName, partDtos, palletIds);

		IShipment newShipment = new RemoteShipment(shipmentDto);

		shipmentCache.put(shipmentDto.getId(), newShipment);

		return newShipment;
	}

	@Override
	public synchronized IShipment getShipment(int shipmentId) throws RemoteException {
		if (!shipmentCache.containsKey(shipmentId)) {
			ShipmentDTO shipmentDto = shipmentDao.read(shipmentId);

			if (shipmentDto == null) return null;
				
			IShipment remoteShipment = new RemoteShipment(shipmentDto);

			shipmentCache.put(shipmentId, remoteShipment);

			return remoteShipment;
		}

		return shipmentCache.get(shipmentId);
	}

	@Override
	public synchronized List<IShipment> getAllShipments() throws RemoteException {
		Collection<ShipmentDTO> allShipments = shipmentDao.readAll();

		for (ShipmentDTO shipment : allShipments)
			if (!shipmentCache.containsKey(shipment.getId()))
				shipmentCache.put(shipment.getId(), new RemoteShipment(shipment));

		return new ArrayList<>(shipmentCache.values());
	}

	/**
	 * Removes the parts that are going to be shipped from the pallets they belong
	 * to
	 * 
	 * @param parts
	 *            the parts to be removed from the pallets
	 * @return a map of all part ids and the pallet which they come from
	 * @throws RemoteException
	 **/
	private Map<Integer, Integer> takeParts(List<IPart> parts) throws RemoteException {
		int size = parts.size();

		// key(part id), value(pallet id)
		Map<Integer, Integer> idMap = new HashMap<>(size);

		for (IPart part : parts) {
			// remove part from it's pallet
			int palletId = dismantleBase.removeFromPallet(part);

			// save the id of the pallet the part comes from
			idMap.put(part.getId(), palletId);
		}
		return idMap;
	}
}
