package server.main;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import remote.base.dismantle.DismantleBaseLocator;
import remote.base.dismantle.IDismantleBase;
import remote.base.registration.IRegistrationBase;
import remote.base.registration.RegistrationBaseLocator;
import remote.base.shipping.IShipmentBase;
import remote.base.shipping.RemoteShipmentBaseLocator;
import remote.dao.car.CarDAOLocator;
import remote.dao.pallet.PalletDAOLocator;
import remote.dao.part.PartDAOLocator;
import remote.dao.shipment.RemoteShipmentDAOLocator;

public final class ServerMain {

	private ServerMain() {}

	public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
		bindRemoteComponents();
		IRegistrationBase regBase = RegistrationBaseLocator.lookupBase();
		IDismantleBase disBase = DismantleBaseLocator.lookupBase();
		IShipmentBase shipBase = RemoteShipmentBaseLocator.lookupBase();
		
	}

	private static void bindRemoteComponents() throws RemoteException, MalformedURLException, NotBoundException {
		// export registry
		LocateRegistry.createRegistry(1099);

		// bind DAO objects
		PartDAOLocator.bindDAO();
		CarDAOLocator.bindDAO();
		PalletDAOLocator.bindDAO();
		RemoteShipmentDAOLocator.bindDAO();

		// bind Base objects
		RegistrationBaseLocator.bindBase();
		DismantleBaseLocator.bindBase();
		RemoteShipmentBaseLocator.bindBase();
	}

}
