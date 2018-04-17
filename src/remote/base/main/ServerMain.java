package remote.base.main;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import remote.base.dismantle.IDismantleBase;
import remote.base.dismantle.RemoteDismantleBase;
import remote.dao.pallet.RemotePalletDAOManager;
import remote.dao.part.RemotePartDAOManager;

public class ServerMain {

	public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
		LocateRegistry.createRegistry(1099);
		
		RemotePartDAOManager.bindDAO(RemotePartDAOManager.PART_DAO_NAME);
		RemotePalletDAOManager.bindDAO(RemotePalletDAOManager.PALLET_DAO_NAME);
		
		IDismantleBase dismantleBase =
				new RemoteDismantleBase(RemotePartDAOManager.lookupDAO(), RemotePalletDAOManager.lookupDAO());
		
	
		dismantleBase.registerPart(null, "Gas engine", 24.6);
		
	}

}
