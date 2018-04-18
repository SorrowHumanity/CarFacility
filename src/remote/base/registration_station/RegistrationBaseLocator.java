package remote.base.registration_station;

import java.rmi.Naming;
import java.rmi.RemoteException;

import remote.dao.car.CarDAOLocator;

public final class RegistrationBaseLocator {

	public static final String REGISTRATION_BASE_ID = "RemoteRegistrationBase";

	private RegistrationBaseLocator() {}

	public static void bindBase(String id) throws RemoteException {
		try {
			Naming.rebind(id, new RemoteRegistrationBase(CarDAOLocator.lookupDAO(CarDAOLocator.CAR_DAO_ID)));
		} catch (Exception e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

	public static IRegistrationBase lookupBase(String id) throws RemoteException {
		try {
			return (IRegistrationBase) Naming.lookup(id);
		} catch (Exception e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

}
