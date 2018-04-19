package server.main;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import remote.base.dismantle_station.DismantleBaseLocator;
import remote.base.registration_station.RegistrationBaseLocator;
import remote.dao.car.CarDAOLocator;
import remote.dao.pallet.PalletDAOLocator;
import remote.dao.part.PartDAOLocator;

public final class ServerMain {

	private ServerMain() {}

	public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
		bindRemoteComponents();
	}

	private static void bindRemoteComponents() throws RemoteException, MalformedURLException, NotBoundException {
		// export registry
		LocateRegistry.createRegistry(1099);

		// bind DAO objects
		PartDAOLocator.bindDAO();
		CarDAOLocator.bindDAO();
		PalletDAOLocator.bindDAO();

		// bind Base objects
		RegistrationBaseLocator.bindBase();
		DismantleBaseLocator.bindBase();
	}

}
