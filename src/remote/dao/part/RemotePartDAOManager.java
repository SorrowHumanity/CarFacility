package remote.dao.part;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RemotePartDAOManager {

	public static final String PART_DAO = "PartDAOServer";
	
	private RemotePartDAOManager() {}
	
	public static IPartDAO lookupDAO() throws MalformedURLException, RemoteException, NotBoundException {
		return (IPartDAO) Naming.lookup(PART_DAO);
	}
	
	public static void bindDAO(String name) throws RemoteException, MalformedURLException {
		Naming.rebind(PART_DAO, new RemotePartDAOServer());
	}
	
}
