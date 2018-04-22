package service.shipment_station;

import java.rmi.RemoteException;
import java.util.List;

import dto.part.PartDTO;
import dto.shipment.ShipmentDTO;

public interface IShipmentStationService {

	ShipmentDTO registerShipment(List<PartDTO> parts, String receiverFirstName, String receiverLastName)
			throws RemoteException;

	ShipmentDTO getShipment(int id) throws RemoteException;

	ShipmentDTO[] getAllShipments() throws RemoteException;

}
