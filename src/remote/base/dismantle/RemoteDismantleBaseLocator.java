package remote.base.dismantle;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import remote.dao.pallet.IPalletDAOServer;
import remote.dao.part.IPartDAOServer;

public class RemoteDismantleBaseLocator {

	private static final String DISMANTLE_BASE_ID = "RemoteDismantleBase";

	private RemoteDismantleBaseLocator() {}

	public static IDismantleBase lookupBase() throws MalformedURLException, RemoteException, NotBoundException {
		return (IDismantleBase) Naming.lookup(DISMANTLE_BASE_ID);
	}

	public static void bindBase(IPartDAOServer partDAO, IPalletDAOServer palletDAO)
			throws RemoteException, MalformedURLException {
		Naming.rebind(DISMANTLE_BASE_ID, new RemoteDismantleBase(partDAO, palletDAO));
	}

}
