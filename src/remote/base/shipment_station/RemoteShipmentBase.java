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
	public synchronized IShipment registerShipment(int id, List<PartDTO> parts, String receiverFirstName, String receiverLastName) throws RemoteException {
		// create database entry
		ShipmentDTO shipmentDto = shipmentDao.create(receiverFirstName, receiverLastName, parts);

		// cache and return
		shipmentCache.put(id, new RemoteShipment(shipmentDto));
		
		return shipmentCache.get(id);
	}

	@Override
	public IShipment getShipment(int id) throws RemoteException {
		// check if car is cached
		if (!shipmentCache.containsKey(id)) {

			// read car from the database
			ShipmentDTO shipmentDto = shipmentDao.read(id);

			// cache car
			shipmentCache.put(id, new RemoteShipment(shipmentDto));
		}

		return shipmentCache.get(id);
	}

	@Override
	public List<IShipment> getAllShipments() throws RemoteException {
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
