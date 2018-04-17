package remote.main;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.LinkedList;
import java.util.List;

import remote.base.dismantle_station.DismantleBaseLocator;
import remote.base.dismantle_station.RemoteDismantleBase;
import remote.base.registration_station.RegistrationBaseLocator;
import remote.dao.car.CarDAOLocator;
import remote.dao.pallet.PalletDAOLocator;
import remote.dao.pallet.PalletEntityConstants;
import remote.dao.part.PartDAOLocator;
import remote.model.car.ICar;
import remote.model.car.RemoteCar;
import remote.model.pallet.IPallet;
import remote.model.pallet.RemotePallet;
import remote.model.part.IPart;
import remote.model.part.RemotePart;
import util.CarFacilityUtils;

public class ServerMain {

	private ServerMain() {
	}

	public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
		startCarFacilitySystem();

		List<IPart> parts = new LinkedList<>();
		parts.add(new RemotePart(1, "987", "Mitsubishi Engine", 30.5));
		parts.add(new RemotePart(2, "987", "Some Engine", 10.5));
		ICar car = new RemoteCar("987", "Subaru Outback", parts);
		
		DismantleBaseLocator.lookupBase().dismantleCar(car);
		
		//IPallet pallet = new RemotePallet(1, PalletEntityConstants.ENGINE_PART_TYPE, parts);
		//System.out.println(pallet.getPalletType());
	}

	private static void startCarFacilitySystem() throws RemoteException, MalformedURLException, NotBoundException {
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
