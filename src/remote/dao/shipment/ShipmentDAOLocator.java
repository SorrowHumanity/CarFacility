package remote.dao.shipment;

import java.rmi.Naming;
import java.rmi.RemoteException;

import remote.dao.part.PartDAOLocator;

public final class ShipmentDAOLocator {

	public static final String SHIPMENT_DAO_ID = "RemoteShipmentDAO";

	private ShipmentDAOLocator() {}

	public static IShipmentDAO lookupDAO() throws RemoteException {
		try {
			return (IShipmentDAO) Naming.lookup(SHIPMENT_DAO_ID);
		} catch (Exception e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

	public static void bindDAO() throws RemoteException {
		try {
			Naming.rebind(SHIPMENT_DAO_ID, new RemoteShipmentDAOServer(PartDAOLocator.lookupDAO()));
		} catch (Exception e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

}
