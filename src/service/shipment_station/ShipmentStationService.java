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
public class ShipmentStationService {
	private IShipmentBase shipmentBase;
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public ShipmentStationService() throws RemoteException {
		try {
			shipmentBase = ShipmentBaseLocator.lookupBase();
		} catch (Exception e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////

	@WebMethod
	public ShipmentDTO registerShipment(int id, List<PartDTO> parts, String receiverFirstName, String receiverLastName) throws RemoteException {
		return new ShipmentDTO(shipmentBase.registerShipment(id, parts, receiverFirstName, receiverLastName));
	}

	@WebMethod
	public ShipmentDTO getShipment(int id) throws RemoteException {
		return new ShipmentDTO(shipmentBase.getShipment(id));
	}

	@WebMethod
	public ShipmentDTO[] getAllShipments() throws RemoteException {
		ShipmentDTO[] allShipments = CollectionUtils.toShipmentDTOArray(shipmentBase.getAllShipments());
		return allShipments;
	}

}
