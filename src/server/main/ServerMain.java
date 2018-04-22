package server.main;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Arrays;
import java.util.List;

import remote.base.dismantle_station.DismantleBaseLocator;
import remote.base.dismantle_station.IDismantleBase;
import remote.base.registration_station.RegistrationBaseLocator;
import remote.base.shipment_station.IShipmentBase;
import remote.base.shipment_station.ShipmentBaseLocator;
import remote.dao.car.CarDAOLocator;
import remote.dao.pallet.PalletDAOLocator;
import remote.dao.part.PartDAOLocator;
import remote.dao.shipment.ShipmentDAOLocator;
import remote.model.part.IPart;
import util.CollectionUtils;

public final class ServerMain {

	private ServerMain() {}

	public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
		bindRemoteComponents();
		IDismantleBase disBase = DismantleBaseLocator.lookupBase();
		IShipmentBase shipBase = ShipmentBaseLocator.lookupBase();
		List<IPart> parts = disBase.getAllParts();
		shipBase.registerShipment(Arrays.asList(CollectionUtils.toDTOArray(parts)), "Anatoli", "Dvoyachki");
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
