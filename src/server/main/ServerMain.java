package server.main;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import dto.part.PartDTO;
import remote.base.dismantle.DismantleBaseLocator;
import remote.base.dismantle.IDismantleBase;
import remote.base.registration.IRegistrationBase;
import remote.base.registration.RegistrationBaseLocator;
import remote.base.shipping.IShipmentBase;
import remote.base.shipping.RemoteShipmentBaseLocator;
import remote.dao.car.RemoteCarDAOLocator;
import remote.dao.pallet.RemotePalletDAOLocator;
import remote.dao.part.RemotePartDAOLocator;
import remote.dao.shipment.RemoteShipmentDAOLocator;
import remote.model.car.ICar;
import remote.model.car.RemoteCar;
import remote.model.part.IPart;
import remote.model.part.RemotePart;

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
		RegistrationBaseLocator.bindBase();
		DismantleBaseLocator.bindBase();
		RemoteShipmentBaseLocator.bindBase();
	}

}
