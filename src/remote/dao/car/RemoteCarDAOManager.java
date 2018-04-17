package remote.dao.car;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RemoteCarDAOManager {
	
	public static final String CAR_DAO = "RemoteCarDAO";
	
	private RemoteCarDAOManager() {}
	
	public static ICarDAO lookupDAO() throws MalformedURLException,
											RemoteException, NotBoundException {
		
		return (ICarDAO) Naming.lookup(CAR_DAO);
	}
	
	public static void bindDAO(String name) throws RemoteException,
											MalformedURLException {
		
		Naming.rebind(CAR_DAO, new RemoteCarDAOServer());
	} 

}
