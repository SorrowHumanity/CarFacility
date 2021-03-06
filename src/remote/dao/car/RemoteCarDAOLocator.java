package remote.dao.car;

import java.rmi.Naming;
import java.rmi.RemoteException;

import remote.dao.part.RemotePartDAOLocator;

public final class RemoteCarDAOLocator {

	public static final String CAR_DAO_URL = "RemoteCarDAO";

	private RemoteCarDAOLocator() {}

	/**
	 * Returns a reference, a stub, for the car data access object associated with
	 * the specified id
	 * 
	 * @param id
	 *            the id
	 * @return a reference to remote car data access object
	 * @throws RemoteException
	 **/
	public static ICarDAO lookupDAO() throws RemoteException {
		try {
			return (ICarDAO) Naming.lookup(CAR_DAO_URL);
		} catch (Exception e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

	/**
	 * Binds a remote car data access object to the specified id

	 * @throws RemoteException
	 **/
	public static void bindDAO() throws RemoteException {
		try {
			Naming.rebind(CAR_DAO_URL, new RemoteCarDAOServer(RemotePartDAOLocator.lookupDAO()));
		} catch (Exception e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

}
