package remote.dao.car;

import java.rmi.Naming;
import java.rmi.RemoteException;

public final class CarDAOLocator {

	public static final String CAR_DAO_ID = "RemoteCarDAO";

	private CarDAOLocator() {}

	public static ICarDAO lookupDAO(String id) throws RemoteException {
		try {
			return (ICarDAO) Naming.lookup(id);
		} catch (Exception e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

	public static void bindDAO(String id) throws RemoteException {
		try {
			Naming.rebind(id, new RemoteCarDAOServer());
		} catch (Exception e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

}
