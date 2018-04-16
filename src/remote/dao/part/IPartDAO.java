package remote.dao.part;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;

import dto.part.PartDTO;

public interface IPartDAO extends Remote {

	PartDTO create(String name, double weight) throws RemoteException;

	Collection<PartDTO> readAll(String chassisNumber) throws RemoteException;

	void update(PartDTO partDTO) throws RemoteException;

	void delete(PartDTO partDTO) throws RemoteException;

}
