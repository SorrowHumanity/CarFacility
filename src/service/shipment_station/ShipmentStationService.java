package service.shipment_station;

import java.rmi.RemoteException;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import dto.part.PartDTO;
import dto.shipment.ShipmentDTO;
import remote.base.shipment_station.IShipmentBase;
import remote.base.shipment_station.ShipmentBaseLocator;
import util.CollectionUtils;

@WebService
public class ShipmentStationService implements IShipmentStationService {
	private IShipmentBase shipmentBase;

	public ShipmentStationService() throws RemoteException {
		try {
			shipmentBase = ShipmentBaseLocator.lookupBase();
		} catch (Exception e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

	@WebMethod
	@Override
	public ShipmentDTO registerShipment(List<PartDTO> parts, String receiverFirstName, String receiverLastName)
			throws RemoteException {
		return new ShipmentDTO(shipmentBase.registerShipment(parts, receiverFirstName, receiverLastName));
	}

	@WebMethod
	@Override
	public ShipmentDTO getShipment(int id) throws RemoteException {
		return new ShipmentDTO(shipmentBase.getShipment(id));
	}

	@WebMethod
	@Override
	public ShipmentDTO[] getAllShipments() throws RemoteException {
		ShipmentDTO[] allShipments = CollectionUtils.toShipmentDTOArray(shipmentBase.getAllShipments());
		return allShipments;
	}

}
