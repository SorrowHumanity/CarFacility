package remote.base.registration_station;

import java.rmi.Naming;
import java.rmi.RemoteException;

import remote.dao.car.CarDAOLocator;

public final class RegistrationBaseLocator {

	public static final String REGISTRATION_BASE_ID = "RemoteRegistrationBase";

	private RegistrationBaseLocator() {}

	/**
	 * Binds a remote registration base
	 * 
	 * @throws RemoteException
	 **/
	public static void bindBase() throws RemoteException {
		try {
			Naming.rebind(REGISTRATION_BASE_ID, new RemoteRegistrationBase(CarDAOLocator.lookupDAO()));
		} catch (Exception e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

	/**
	 * Returns a remote registration base
	 * 
	 * @return a reference to remote registration base
	 * @throws RemoteException
	 **/
	public static IRegistrationBase lookupBase() throws RemoteException {
		try {
			return (IRegistrationBase) Naming.lookup(REGISTRATION_BASE_ID);
		} catch (Exception e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

}
