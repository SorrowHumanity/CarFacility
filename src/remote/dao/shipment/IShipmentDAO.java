package remote.dao.shipment;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

import dto.part.PartDTO;
import dto.shipment.ShipmentDTO;

public interface IShipmentDAO extends Remote {

	ShipmentDTO create(String receiverFirstName, String receiverLastName, List<PartDTO> parts) throws RemoteException;

	ShipmentDTO read(int shipmentId) throws RemoteException;

	Collection<ShipmentDTO> readAll() throws RemoteException;

	boolean update(ShipmentDTO shipmentDto) throws RemoteException;

	boolean delete(ShipmentDTO shipmentDto) throws RemoteException;

}
