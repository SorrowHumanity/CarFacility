package remote.dao.pallet;

import java.rmi.Naming;
import java.rmi.RemoteException;

public final class PalletDAOLocator {

	public static final String PALLET_DAO_ID = "RemotePalletDAO";

	private PalletDAOLocator() {}

	public static IPalletDAO lookupDAO(String id) throws RemoteException {
		try {
			return (IPalletDAO) Naming.lookup(id);
		} catch (Exception e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

	public static void bindDAO(String id) throws RemoteException {
		try {
			Naming.rebind(id, new RemotePalletDAOServer());
		} catch (Exception e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

}
