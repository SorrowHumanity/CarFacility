package remote.base.dismantle_station;

import java.rmi.Naming;
import java.rmi.RemoteException;

import remote.dao.pallet.PalletDAOLocator;
import remote.dao.part.PartDAOLocator;

public final class DismantleBaseLocator {

	public static final String DISMANTLE_BASE_ID = "RemoteDismantleBase";

	private DismantleBaseLocator() {}

	public static IDismantleBase lookupBase(String id) throws RemoteException {
		try {
			return (IDismantleBase) Naming.lookup(id);
		} catch (Exception e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

	public static void bindBase(String id) throws RemoteException {
		try {
			Naming.rebind(id, new RemoteDismantleBase(PartDAOLocator.lookupDAO(PartDAOLocator.PART_DAO_ID),
					PalletDAOLocator.lookupDAO(PalletDAOLocator.PALLET_DAO_ID)));
		} catch (Exception e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

}
