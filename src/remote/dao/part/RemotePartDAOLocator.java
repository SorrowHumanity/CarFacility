package remote.dao.part;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RemotePartDAOLocator {

	public static final String PART_DAO_NAME = "PartDAOServer";
	
	private RemotePartDAOLocator() {}
	
	public static IPartDAOServer lookupDAO() throws MalformedURLException, RemoteException, NotBoundException {
		return (IPartDAOServer) Naming.lookup(PART_DAO_NAME);
	}
	
	public static void bindDAO(String name) throws RemoteException, MalformedURLException {
		Naming.rebind(PART_DAO_NAME, new RemotePartDAOServer());
	}
	
}
