package service.shipment_station;

import java.rmi.RemoteException;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;
import dto.part.PartDTO;
import dto.shipment.ShipmentDTO;
import remote.base.shipping.IShipmentBase;
import remote.base.shipping.RemoteShipmentBaseLocator;
import remote.model.part.IPart;
import remote.model.shipment.IShipment;
import util.CollectionUtils;

@WebService
public class ShipmentStationService implements IShipmentStationService {

	private IShipmentBase shipmentBase;

	public ShipmentStationService() throws RemoteException {
		try {
			shipmentBase = RemoteShipmentBaseLocator.lookupBase();
		} catch (Exception e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

	@WebMethod
	@Override
	public ShipmentDTO registerShipment(PartDTO[] parts, String receiverFirstName, String receiverLastName)
			throws RemoteException {
		List<IPart> remoteParts = CollectionUtils.toRemotePartsList(parts);
		IShipment shipment = shipmentBase.registerShipment(remoteParts, receiverFirstName, receiverLastName);
		ShipmentDTO shipmentDto = new ShipmentDTO(shipment);
		return shipmentDto;
	}

	@WebMethod
	@Override
	public ShipmentDTO getShipment(int shipmentId) throws RemoteException {
		IShipment shipment = shipmentBase.getShipment(shipmentId);
		ShipmentDTO shipmentDto = new ShipmentDTO(shipment);
		return shipmentDto;
	}

	@WebMethod
	@Override
	public ShipmentDTO[] getAllShipments() throws RemoteException {
		ShipmentDTO[] allShipments = CollectionUtils.toShipmentDTOArray(shipmentBase.getAllShipments());
		return allShipments;
	}

}
