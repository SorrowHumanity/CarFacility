package remote.base.dismantle;

import java.rmi.Naming;
import java.rmi.RemoteException;

import remote.dao.pallet.RemotePalletDAOLocator;
import remote.dao.part.RemotePartDAOLocator;

public final class RemoteDismantleBaseLocator {

	public static final String DISMANTLE_BASE_URL = "RemoteDismantleBase";

	private RemoteDismantleBaseLocator() {}

	/**
	 * Returns a reference to the remote dismantle base
	 * 
	 * @return a reference to remote dismantle base object
	 * @throws RemoteException
	 **/
	public static IDismantleBase lookupBase() throws RemoteException {
		try {
			return (IDismantleBase) Naming.lookup(DISMANTLE_BASE_URL);
		} catch (Exception e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

	/**
	 * Binds a remote dismantle base to the specified id
	 * @throws RemoteException
	 **/
	public static void bindBase() throws RemoteException {
		try {
			Naming.rebind(DISMANTLE_BASE_URL,
					new RemoteDismantleBase(RemotePartDAOLocator.lookupDAO(), RemotePalletDAOLocator.lookupDAO()));
		} catch (Exception e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

}
