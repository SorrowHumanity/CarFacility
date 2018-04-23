package service.shipment_station;

import java.rmi.RemoteException;
import java.util.Arrays;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import dto.part.PartDTO;
import dto.shipment.ShipmentDTO;
import remote.base.shipping.IShipmentBase;
import remote.base.shipping.RemoteShipmentBaseLocator;
import remote.model.shipment.IShipment;
import util.CollectionUtils;

@WebService
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
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
		IShipment shipment = shipmentBase.registerShipment(Arrays.asList(parts), receiverFirstName, receiverLastName);
		return new ShipmentDTO(shipment);
	}

	@WebMethod
	@Override
	public ShipmentDTO getShipment(int shipmentId) throws RemoteException {
		IShipment shipment = shipmentBase.getShipment(shipmentId);
		return new ShipmentDTO(shipment);
	}

	@WebMethod
	@Override
	public ShipmentDTO[] getAllShipments() throws RemoteException {
		ShipmentDTO[] allShipments = CollectionUtils.toShipmentDTOArray(shipmentBase.getAllShipments());
		return allShipments;
	}

}
