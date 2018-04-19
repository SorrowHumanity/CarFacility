package remote.dao.car;

import java.rmi.Naming;
import java.rmi.RemoteException;

import remote.dao.part.PartDAOLocator;

public final class CarDAOLocator {

	public static final String CAR_DAO_ID = "RemoteCarDAO";

	private CarDAOLocator() {}

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
			return (ICarDAO) Naming.lookup(CAR_DAO_ID);
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
			Naming.rebind(CAR_DAO_ID, new RemoteCarDAOServer(PartDAOLocator.lookupDAO()));
		} catch (Exception e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

}
