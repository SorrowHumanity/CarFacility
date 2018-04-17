package remote.main;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import dto.car.CarDTO;
import dto.part.PartDTO;
import remote.base.dismantle_station.RemoteDismantleBaseLocator;
import remote.dao.car.ICarDAO;
import remote.dao.car.RemoteCarDAOManager;
import remote.dao.pallet.RemotePalletDAOManager;
import remote.dao.part.RemotePartDAOManager;

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
		RemoteCarDAOManager.bindDAO(RemoteCarDAOManager.CAR_DAO);
		RemotePartDAOManager.bindDAO(RemotePartDAOManager.PART_DAO);
		RemotePalletDAOManager.bindDAO(RemotePalletDAOManager.PALLET_DAO);
		
		// bind Base objects
		RemoteDismantleBaseLocator.bindBase(RemotePartDAOManager.lookupDAO(),
				RemotePalletDAOManager.lookupDAO());

	}

}
