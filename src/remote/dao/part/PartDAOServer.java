package remote.dao.part;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;

import dto.part.PartDTO;
import persistence.DatabaseHelper;

public class PartDAOServer extends UnicastRemoteObject implements IPartDAO {

	private static final long serialVersionUID = 1L;

	private DatabaseHelper<PartDTO> helper;

	public PartDAOServer() throws RemoteException {
		helper = new DatabaseHelper<>("jdbc:postgresql://localhost:5432/bank_system", "postgres", "password");
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
