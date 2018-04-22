package remote.base.shipment_station;

import java.rmi.Naming;
import java.rmi.RemoteException;

import remote.dao.shipment.ShipmentDAOLocator;

public class ShipmentBaseLocator {

	public static final String SHIPMENT_BASE_ID = "RemoteShipmentBase";

	private ShipmentBaseLocator() {}

	public static void bindBase() throws RemoteException {
		try {
			Naming.rebind(SHIPMENT_BASE_ID, new RemoteShipmentBase(ShipmentDAOLocator.lookupDAO()));
		} catch (Exception e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

	public static IShipmentBase lookupBase() throws RemoteException {
		try {
			return (IShipmentBase) Naming.lookup(SHIPMENT_BASE_ID);
		} catch (Exception e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}
}
