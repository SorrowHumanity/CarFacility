package remote.dao.car;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public final class CarDAOLocator {

	public static final String CAR_DAO_ID = "RemoteCarDAO";

	private CarDAOLocator() {}

	public static ICarDAO lookupDAO() throws MalformedURLException, RemoteException, NotBoundException {
		return (ICarDAO) Naming.lookup(CAR_DAO_ID);
	}

	public static void bindDAO(String id) throws RemoteException, MalformedURLException {
		Naming.rebind(CAR_DAO_ID, new RemoteCarDAOServer());
	}

}
