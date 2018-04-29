package remote.base.registration;

import java.rmi.Naming;
import java.rmi.RemoteException;

import remote.dao.car.RemoteCarDAOLocator;

public final class RemoteRegistrationBaseLocator {

	public static final String REGISTRATION_BASE_ID = "RemoteRegistrationBase";

	private RemoteRegistrationBaseLocator() {}

	/**
	 * Binds a remote registration base
	 * 
	 * @throws RemoteException
	 **/
	public static void bindBase() throws RemoteException {
		try {
			Naming.rebind(REGISTRATION_BASE_ID, new RemoteRegistrationBase(RemoteCarDAOLocator.lookupDAO()));
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
