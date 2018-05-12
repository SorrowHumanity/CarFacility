package server.main;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import remote.base.dismantle.RemoteDismantleBaseLocator;
import remote.base.registration.RemoteRegistrationBaseLocator;
import remote.base.shipping.RemoteShipmentBaseLocator;
import remote.dao.car.RemoteCarDAOLocator;
import remote.dao.pallet.RemotePalletDAOLocator;
import remote.dao.part.RemotePartDAOLocator;
import remote.dao.shipment.RemoteShipmentDAOLocator;

public final class ServerMain {

	private ServerMain() {}

	public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
		bindRemoteComponents();
	}

	private static void bindRemoteComponents() throws RemoteException, MalformedURLException, NotBoundException {
		// export registry
		LocateRegistry.createRegistry(1099);

		// bind DAO objects
		RemotePartDAOLocator.bindDAO();
		RemoteCarDAOLocator.bindDAO();
		RemotePalletDAOLocator.bindDAO();
		RemoteShipmentDAOLocator.bindDAO();

		// bind Base objects
		RemoteRegistrationBaseLocator.bindBase();
		RemoteDismantleBaseLocator.bindBase();
		RemoteShipmentBaseLocator.bindBase();
		
		System.out.println("Remote components bound...");
	}

}
