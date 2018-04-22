package server.main;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.LinkedList;
import java.util.List;

import dto.part.PartDTO;
import remote.base.dismantle_station.DismantleBaseLocator;
import remote.base.dismantle_station.IDismantleBase;
import remote.base.registration_station.IRegistrationBase;
import remote.base.registration_station.RegistrationBaseLocator;
import remote.base.shipment_station.IShipmentBase;
import remote.base.shipment_station.ShipmentBaseLocator;
import remote.dao.car.CarDAOLocator;
import remote.dao.pallet.PalletDAOLocator;
import remote.dao.part.PartDAOLocator;
import remote.dao.shipment.ShipmentDAOLocator;
import remote.model.pallet.IPallet;
import remote.model.part.IPart;

public final class ServerMain {

	private ServerMain() {}

	public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
		bindRemoteComponents();
		IRegistrationBase regBase = RegistrationBaseLocator.lookupBase();
		IDismantleBase disBase = DismantleBaseLocator.lookupBase();
		IShipmentBase shipBase = ShipmentBaseLocator.lookupBase();
		
		IPallet pallet = disBase.getPallet(10);
		List<IPart> parts = pallet.getParts();
		IPart p = parts.get(0);
		IPart p1 = parts.get(1);
		LinkedList<PartDTO> list = new LinkedList<>();
		list.add(new PartDTO(p));
		list.add(new PartDTO(p1));
		shipBase.registerShipment(list, "Misho", "Shamara"); // Big Sha motherfucker
	}

	private static void bindRemoteComponents() throws RemoteException, MalformedURLException, NotBoundException {
		// export registry
		LocateRegistry.createRegistry(1099);

		// bind DAO objects
		PartDAOLocator.bindDAO();
		CarDAOLocator.bindDAO();
		PalletDAOLocator.bindDAO();
		ShipmentDAOLocator.bindDAO();

		// bind Base objects
		RegistrationBaseLocator.bindBase();
		DismantleBaseLocator.bindBase();
		ShipmentBaseLocator.bindBase();
	}

}
