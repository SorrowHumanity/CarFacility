package remote.dao.part;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;

import dto.part.PartDTO;

public interface IPartDAO extends Remote {

	PartDTO create(String chassisNumber, String name, double weightKg) throws RemoteException;

	Collection<PartDTO> read(String chassisNumber) throws RemoteException;

	Collection<PartDTO> read(int palletId) throws RemoteException;

	Collection<PartDTO> readAll() throws RemoteException;

	boolean update(PartDTO partDto) throws RemoteException;

	boolean delete(PartDTO partDto) throws RemoteException;

}
