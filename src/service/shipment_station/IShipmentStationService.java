package service.shipment_station;

import java.rmi.RemoteException;
import dto.part.PartDTO;
import dto.shipment.ShipmentDTO;

/**
 * Javadoc is the same as the one for IShipmentBase, with the exception that
 * IShipmentStationService uses DTO objects
 **/
public interface IShipmentStationService {

	ShipmentDTO registerShipment(PartDTO[] parts, String receiverFirstName, String receiverLastName)
			throws RemoteException;

	ShipmentDTO getShipment(int shipementId) throws RemoteException;

	ShipmentDTO[] getAllShipments() throws RemoteException;

}
