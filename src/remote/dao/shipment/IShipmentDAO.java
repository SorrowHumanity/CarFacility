package remote.dao.shipment;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import dto.part.PartDTO;
import dto.shipment.ShipmentDTO;

public interface IShipmentDAO extends Remote {

	/**
	 * Creates a shipment entry in the shipments entity
	 * 
	 * @param receiverFirstName
	 *            the first name of the reciever
	 * @param receiverLastName
	 *            the last name of the receiver
	 * @param parts
	 *            the parts to be stored in the shipment
	 * @return a shipment data transfer object
	 * @throws RemoteException
	 **/
	ShipmentDTO create(String receiverFirstName, String receiverLastName, List<PartDTO> parts,
			Map<Integer, Integer> palletAssociations) throws RemoteException;

	/**
	 * Reads the shipment entry from the shipment entity and its belonging parts
	 * that matches the shipment id passed as a parameter
	 * 
	 * @param shipmentId
	 *            the shioment id
	 * @return a shipment data transfer object
	 * @throws RemoteException
	 **/
	ShipmentDTO read(int shipmentId) throws RemoteException;

	/**
	 * Reads all shipment entries from the shipments entity and their belonging
	 * parts
	 * 
	 * @param shipmentId
	 *            the shioment id
	 * @return a shipment data transfer object
	 * @throws RemoteException
	 **/
	Collection<ShipmentDTO> readAll() throws RemoteException;

	/**
	 * Updates a shipment entry in the shipments entity, corresponding to the
	 * shipment id of the shipment parameter. Also updates the contents of the
	 * shipment (requests entity)
	 * 
	 * @param updatedShipmentDto
	 *            the updated shipment
	 * @return true, if a shipment is updated. Otherwise false
	 * @throws RemoteException
	 **/
	boolean update(ShipmentDTO updatedShipmentDto) throws RemoteException;

	/**
	 * Deletes a shipment entry from the shipments entity with the shipment id of
	 * the shipment parameter and all associations to parts in the requests entity
	 * 
	 * @param shipmentDto
	 *            the shipment
	 * @return true, if the shipment entry has been deleted. Otherwise, false
	 * @throws RemoteException
	 **/
	boolean delete(ShipmentDTO shipmentDto) throws RemoteException;

}
