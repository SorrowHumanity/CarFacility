package server.main;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.LinkedList;
import java.util.List;

import dto.part.PartDTO;
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
import remote.model.pallet.IPallet;
import remote.model.part.IPart;

public final class ServerMain {

	private ServerMain() {}

	public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
		bindRemoteComponents();
		IRegistrationBase regBase = RegistrationBaseLocator.lookupBase();
		IDismantleBase disBase = DismantleBaseLocator.lookupBase();
		IShipmentBase shipBase = RemoteShipmentBaseLocator.lookupBase();
		
//		IPallet pallet = disBase.getPallet(11);
//		IPallet pallet1 = disBase.getPallet(9);
//		List<IPart> parts = pallet.getParts();
//		IPart p = parts.get(0);
//		IPart p1 = parts.get(1);
//		LinkedList<PartDTO> list = new LinkedList<>();
//		list.add(new PartDTO(p));
//		list.add(new PartDTO(p1));
//		list.add(new PartDTO(pallet1.getParts().get(0)));
//		shipBase.registerShipment(list, "Vlado", "Kuzov"); 
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
