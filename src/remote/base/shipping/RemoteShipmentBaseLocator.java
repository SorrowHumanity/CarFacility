package remote.base.shipping;

import java.rmi.Naming;
import java.rmi.RemoteException;

import remote.base.dismantle.DismantleBaseLocator;
import remote.dao.shipment.RemoteShipmentDAOLocator;

public class RemoteShipmentBaseLocator {

	public static final String SHIPMENT_BASE_ID = "RemoteShipmentBase";

	private RemoteShipmentBaseLocator() {}

	/**
	 * Creates and binds a remote shipment base to the registry
	 * 
	 * @throws RemoteException
	 **/
	public static void bindBase() throws RemoteException {
		try {
			Naming.rebind(SHIPMENT_BASE_ID,
					new RemoteShipmentBase(RemoteShipmentDAOLocator.lookupDAO(), DismantleBaseLocator.lookupBase()));
		} catch (Exception e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

	/**
	 * Returns a reference to a remote shipment base
	 * 
	 * @return a reference to a remote shipment base
	 * @throws RemoteException
	 **/
	public static IShipmentBase lookupBase() throws RemoteException {
		try {
			return (IShipmentBase) Naming.lookup(SHIPMENT_BASE_ID);
		} catch (Exception e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}
}
