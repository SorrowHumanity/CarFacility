package remote.dao.pallet;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public final class PalletDAOLocator {

	public static final String PALLET_DAO_ID = "RemotePalletDAO";

	private PalletDAOLocator() {}

	public static IPalletDAO lookupDAO(String id ) throws MalformedURLException, RemoteException, NotBoundException {
		return (IPalletDAO) Naming.lookup(id);
	}

	public static void bindDAO(String id) throws RemoteException, MalformedURLException {
		Naming.rebind(id, new RemotePalletDAOServer());
	}

}
