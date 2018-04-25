package remote.dao.part;

import java.rmi.Naming;
import java.rmi.RemoteException;

public final class RemotePartDAOLocator {

	public static final String PART_DAO_ID = "RemotePartDAO";

	private RemotePartDAOLocator() {}

	/**
	 * Returns a part data access object
	 * 
	 * @return a remote part data access object
	 **/
	public static IPartDAO lookupDAO() throws RemoteException {
		try {
			return (IPartDAO) Naming.lookup(PART_DAO_ID);
		} catch (Exception e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

	/**
	 * Binds a remote part data access object
	 * 
	 * @throws RemoteException
	 **/
	public static void bindDAO() throws RemoteException {
		try {
			Naming.rebind(PART_DAO_ID, new RemotePartDAOServer());
		} catch (Exception e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

}
