package remote.base.shipment_station;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import dto.part.PartDTO;
import dto.shipment.ShipmentDTO;
import remote.base.dismantle_station.IDismantleBase;
import remote.dao.shipment.IShipmentDAO;
import remote.model.part.RemotePart;
import remote.model.shipment.IShipment;
import remote.model.shipment.RemoteShipment;

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
	public synchronized IShipment registerShipment(List<PartDTO> parts, String receiverFirstName, String receiverLastName) throws RemoteException {
		// get map of the pallet ids of the pallets from which the parts come
		Map<Integer, Integer> palletIds = getPalletIds(parts);
		
		// create database entry
		ShipmentDTO shipmentDto = shipmentDao.create(receiverFirstName, receiverLastName, parts, palletIds);
		
		// cache and return
		shipmentCache.put(shipmentDto.getId(), new RemoteShipment(shipmentDto));
		
		return shipmentCache.get(shipmentDto.getId());
	}

	@Override
	public synchronized IShipment getShipment(int shipmentId) throws RemoteException {
		// check if shipment is cached
		if (!shipmentCache.containsKey(shipmentId)) {

			// read shipment from the database
			ShipmentDTO shipmentDto = shipmentDao.read(shipmentId);

			// cache shipment
			shipmentCache.put(shipmentId, new RemoteShipment(shipmentDto));
		}

		return shipmentCache.get(shipmentId);
	}

	@Override
	public synchronized List<IShipment> getAllShipments() throws RemoteException {
		// read all shipment from the database
		Collection<ShipmentDTO> allShipments = shipmentDao.readAll();

		// create output collection
		LinkedList<IShipment> shipmentList = new LinkedList<>();

		// cache if not already cached
		for (ShipmentDTO shipment : allShipments) {
			if (!shipmentCache.containsKey(shipment.getId()))
				shipmentCache.put(shipment.getId(), new RemoteShipment(shipment));

			// add to output collection
			shipmentList.add(shipmentCache.get(shipment.getId()));
		}

		return shipmentList;
	}
	
	private Map<Integer, Integer> getPalletIds(List<PartDTO> parts) throws RemoteException {
		// key(part id), value(pallet id)
		HashMap<Integer, Integer> palletIdMap = new HashMap<>();
		for (PartDTO part : parts) {
			int palletId = dismantleBase.removeFromPallet(new RemotePart(part));
			palletIdMap.put(part.getId(), palletId);
		}
		return palletIdMap;
	}
}
