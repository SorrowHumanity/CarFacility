package remote.base.dismantle_station;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import remote.dao.pallet.PalletDAOLocator;
import remote.dao.part.PartDAOLocator;

public final class DismantleBaseLocator {

	public static final String DISMANTLE_BASE_ID = "RemoteDismantleBase";

	private DismantleBaseLocator() {}

	public static IDismantleBase lookupBase() throws MalformedURLException, RemoteException, NotBoundException {
		return (IDismantleBase) Naming.lookup(DISMANTLE_BASE_ID);
	}

	public static void bindBase(String id) throws RemoteException, MalformedURLException, NotBoundException {
		Naming.rebind(id, new RemoteDismantleBase(PartDAOLocator.lookupDAO(), PalletDAOLocator.lookupDAO()));
	}

}
