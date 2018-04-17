package remote.dao.pallet;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RemotePalletDAOManager {

	public static final String PALLET_DAO_NAME = "PalletDAOServer";

	private RemotePalletDAOManager() {}

	public static IPalletDAOServer lookupDAO() throws MalformedURLException, RemoteException, NotBoundException {
		return (IPalletDAOServer) Naming.lookup(PALLET_DAO_NAME);
	}

	public static void bindDAO(String name) throws RemoteException, MalformedURLException {
		Naming.rebind(PALLET_DAO_NAME, new RemotePalletDAOServer());
	}

}
