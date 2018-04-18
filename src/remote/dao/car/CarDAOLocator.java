package remote.dao.car;

import java.rmi.Naming;
import java.rmi.RemoteException;

public final class CarDAOLocator {

	public static final String CAR_DAO_ID = "RemoteCarDAO";

	private CarDAOLocator() {}

	public static ICarDAO lookupDAO() throws RemoteException {
		try {
			return (ICarDAO) Naming.lookup(CAR_DAO_ID);
		} catch (Exception e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

	public static void bindDAO(String id) throws RemoteException {
		try {
			Naming.rebind(CAR_DAO_ID, new RemoteCarDAOServer());
		} catch (Exception e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

}
