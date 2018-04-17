package remote.dao.part;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;

import dto.part.PartDTO;

public interface IPartDAO extends Remote {

	PartDTO create(String chassisNumber, String name, double weight) throws RemoteException;

	Collection<PartDTO> read(String chassisNumber) throws RemoteException;
	
	Collection<PartDTO> read(int palletId) throws RemoteException;

	Collection<PartDTO> readAll() throws RemoteException;
	
	boolean update(PartDTO partDTO) throws RemoteException;

	boolean delete(PartDTO partDTO) throws RemoteException;

}
