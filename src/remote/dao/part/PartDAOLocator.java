package remote.dao.part;

import java.rmi.Naming;
import java.rmi.RemoteException;

public final class PartDAOLocator {

	public static final String PART_DAO_ID = "RemotePartDAO";

	private PartDAOLocator() {}

	public static IPartDAO lookupDAO(String id) throws RemoteException {
		try {
			return (IPartDAO) Naming.lookup(id);
		} catch (Exception e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

	public static void bindDAO(String id) throws RemoteException {
		try {
			Naming.rebind(id, new RemotePartDAOServer());
		} catch (Exception e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

}
