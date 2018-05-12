package remote.base.shipping;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import remote.model.part.*;
import dto.part.PartDTO;
import dto.shipment.ShipmentDTO;
import remote.base.dismantle.IDismantleBase;
import remote.dao.shipment.IShipmentDAO;
import remote.model.shipment.IShipment;
import remote.model.shipment.RemoteShipment;
import util.CollectionUtils;

public class RemoteShipmentBase extends UnicastRemoteObject implements IShipmentBase {

	private static final long serialVersionUID = 1L;

	private Map<Integer, IShipment> shipmentCache = new HashMap<>();
	private IShipmentDAO shipmentDao;
	private IDismantleBase dismantleBase;

	public RemoteShipmentBase(IShipmentDAO shipmentDao, IDismantleBase dismantleBase) throws RemoteException {
		this.shipmentDao = shipmentDao;
		this.dismantleBase = dismantleBase;
	}

	@Override
	public synchronized IShipment registerShipment(List<IPart> parts, String receiverFirstName, String receiverLastName)
			throws RemoteException {
		// get map of the pallet ids of the pallets from which the parts come
		// key(part id), value(pallet id)
		Map<Integer, Integer> palletIds = takeParts(parts);

		// create database entry
		List<PartDTO> partDtos = CollectionUtils.toDTOList(parts);
		ShipmentDTO shipmentDto = shipmentDao.create(receiverFirstName, receiverLastName, partDtos, palletIds);

		// cache and return
		shipmentCache.put(shipmentDto.getId(), new RemoteShipment(shipmentDto));

		return shipmentCache.get(shipmentDto.getId());
	}

	@Override
	public synchronized IShipment getShipment(int shipmentId) throws RemoteException {
		// cache if shipment not already cached
		if (!shipmentCache.containsKey(shipmentId)) {
			// read shipment from the database
			ShipmentDTO shipmentDto = shipmentDao.read(shipmentId);

			// avoid caching null values
			if (shipmentDto == null) 
				throw new IllegalArgumentException("Shipment with id " + shipmentId + " does not exist!");
			
			shipmentCache.put(shipmentId, new RemoteShipment(shipmentDto));
		}

		return shipmentCache.get(shipmentId);
	}

	@Override
	public synchronized List<IShipment> getAllShipments() throws RemoteException {
		// read all shipment from the database
		Collection<ShipmentDTO> allShipments = shipmentDao.readAll();

		// cache if not already cached
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
		HashMap<Integer, Integer> idMap = new HashMap<>(size);

		for (IPart part : parts) {
			// remove part from it's pallet
			int palletId = dismantleBase.removeFromPallet(part);

			// save the id of the pallet the part comes from
			idMap.put(part.getId(), palletId);
		}
		return idMap;
	}
}
