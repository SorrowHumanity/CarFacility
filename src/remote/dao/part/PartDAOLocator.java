package remote.dao.part;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class PartDAOLocator {

	public static final String PART_DAO_ID = "RemotePartDAO";
	
	private PartDAOLocator() {}
	
	public static IPartDAO lookupDAO() throws MalformedURLException, RemoteException, NotBoundException {
		return (IPartDAO) Naming.lookup(PART_DAO_ID);
	}
	
	public static void bindDAO(String id) throws RemoteException, MalformedURLException {
		Naming.rebind(PART_DAO_ID, new RemotePartDAOServer());
	}
	
}
