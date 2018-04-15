package dao.part;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;

import dto.part.PartDTO;

public class PartDAOServer extends UnicastRemoteObject implements IPartDAO {
	
	// This is the beginning AND the end mortals, none may enter the master's sanctum! ;) Muhahahahahaha

	public PartDAOServer() throws RemoteException {
	}

	@Override
	public PartDTO create(String name, double weight) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<PartDTO> readAll(String chassisNumber) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(PartDTO partDTO) throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(PartDTO partDTO) throws RemoteException {
		// TODO Auto-generated method stub

	}

}
