package remote.base.registration_station;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import remote.dao.car.CarDAOLocator;

public class RegistrationBaseLocator {

	public static final String REGISTRATION_BASE_ID = "RemoteRegistrationBase";

	private RegistrationBaseLocator() {}

	public static void bindBase(String id) throws RemoteException, MalformedURLException, NotBoundException {
		Naming.rebind(id, new RemoteRegistrationBase(CarDAOLocator.lookupDAO()));
	}

	public static IRegistrationBase lookupBase(String id) throws MalformedURLException, RemoteException, NotBoundException {
		return (IRegistrationBase) Naming.lookup(id);
	}

}
