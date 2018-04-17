package remote.base.main;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import remote.base.dismantle.IDismantleBase;
import remote.base.dismantle.RemoteDismantleBase;
import remote.dao.pallet.RemotePalletDAOLocator;
import remote.dao.part.RemotePartDAOLocator;

public class ServerMain {

	public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
		LocateRegistry.createRegistry(1099);
		
		RemotePartDAOLocator.bindDAO(RemotePartDAOLocator.PART_DAO_NAME);
		RemotePalletDAOLocator.bindDAO(RemotePalletDAOLocator.PALLET_DAO_NAME);
		
		IDismantleBase dismantleBase =
				new RemoteDismantleBase(RemotePartDAOLocator.lookupDAO(), RemotePalletDAOLocator.lookupDAO());
		
	}

}
