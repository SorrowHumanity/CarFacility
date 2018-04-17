package remote.main;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import remote.base.dismantle_station.DismantleBaseLocator;
import remote.base.registration_station.RegistrationBaseLocator;
import remote.dao.car.CarDAOLocator;
import remote.dao.pallet.PalletDAOLocator;
import remote.dao.part.PartDAOLocator;

public class ServerMain {

	private ServerMain() {}
	
	public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
		startCarFacilitySystem();
	} 

	private static void startCarFacilitySystem() throws RemoteException,
											MalformedURLException, NotBoundException {
		// export registry
		LocateRegistry.createRegistry(1099);

		// bind DAO objects
		CarDAOLocator.bindDAO(CarDAOLocator.CAR_DAO_ID);
		PartDAOLocator.bindDAO(PartDAOLocator.PART_DAO_ID);
		PalletDAOLocator.bindDAO(PalletDAOLocator.PALLET_DAO_ID);
		
		// bind Base objects
		RegistrationBaseLocator.bindBase(RegistrationBaseLocator.REGISTRATION_BASE_ID);
		DismantleBaseLocator.bindBase(DismantleBaseLocator.DISMANTLE_BASE_ID);

	}

}
