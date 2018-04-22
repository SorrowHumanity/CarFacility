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
import remote.dao.shipment.IShipmentDAO;
import remote.model.shipment.IShipment;
import remote.model.shipment.RemoteShipment;

public class RemoteShipmentBase extends UnicastRemoteObject implements IShipmentBase {
	
	private static final long serialVersionUID = 1L;

	private Map<Integer, IShipment> shipmentCache = new HashMap<>();
	private IShipmentDAO shipmentDao;

	public RemoteShipmentBase(IShipmentDAO shipmentDao) throws RemoteException {
		this.shipmentDao = shipmentDao;
	}

	@Override
	public synchronized IShipment registerShipment(List<PartDTO> parts, String receiverFirstName, String receiverLastName) throws RemoteException {
		// create database entry
		ShipmentDTO shipmentDto = shipmentDao.create(receiverFirstName, receiverLastName, parts);

		// cache and return
		shipmentCache.put(shipmentDto.getId(), new RemoteShipment(shipmentDto));
		
		return shipmentCache.get(shipmentDto.getId());
	}

	@Override
	public synchronized IShipment getShipment(int shipmentId) throws RemoteException {
		// check if car is cached
		if (!shipmentCache.containsKey(shipmentId)) {

			// read car from the database
			ShipmentDTO shipmentDto = shipmentDao.read(shipmentId);

			// cache car
			shipmentCache.put(shipmentId, new RemoteShipment(shipmentDto));
		}

		return shipmentCache.get(shipmentId);
	}

	@Override
	public synchronized List<IShipment> getAllShipments() throws RemoteException {
		// read all cars from the database
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
}
