package remote.dao.pallet;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RemotePalletDAOManager {

	public static final String PALLET_DAO = "RemotePalletDAO";

	private RemotePalletDAOManager() {}

	public static IPalletDAO lookupDAO() throws MalformedURLException, RemoteException, NotBoundException {
		return (IPalletDAO) Naming.lookup(PALLET_DAO);
	}

	public static void bindDAO(String name) throws RemoteException, MalformedURLException {
		Naming.rebind(PALLET_DAO, new RemotePalletDAOServer());
	}

}
