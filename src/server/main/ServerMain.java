package server.main;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import dto.car.CarDTO;
import dto.part.PartDTO;
import remote.base.dismantle_station.DismantleBaseLocator;
import remote.base.dismantle_station.IDismantleBase;
import remote.base.dismantle_station.RemoteDismantleBase;
import remote.base.registration_station.RegistrationBaseLocator;
import remote.dao.car.CarDAOLocator;
import remote.dao.pallet.IPalletDAO;
import remote.dao.pallet.PalletDAOLocator;
import remote.dao.pallet.PalletEntityConstants;
import remote.dao.part.IPartDAO;
import remote.dao.part.PartDAOLocator;
import remote.model.car.ICar;
import remote.model.car.RemoteCar;
import remote.model.pallet.IPallet;
import remote.model.pallet.RemotePallet;
import remote.model.part.IPart;
import remote.model.part.RemotePart;
import util.Utils;

public final class ServerMain {

	private ServerMain() {}

	public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
		bindRemoteComponents();

		LinkedList<PartDTO> parts = new LinkedList<>();
		parts.add(new PartDTO("newChassisNumber2", "Window", 4.5));
		ICar car = RegistrationBaseLocator.lookupBase(RegistrationBaseLocator.REGISTRATION_BASE_ID).registerCar(
				"newChassisNumber2", "Subaru BRZ", parts);
		
		
		DismantleBaseLocator.lookupBase(DismantleBaseLocator.DISMANTLE_BASE_ID).dismantleCar(car);

	}

	private static void bindRemoteComponents() throws RemoteException, MalformedURLException, NotBoundException {
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
