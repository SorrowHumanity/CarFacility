package remote.dao.part;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;

import dto.part.PartDTO;

public interface IPartDAO extends Remote {

	/**
	 * Creates a part entry in the part entity and creates a data transfer object
	 * 
	 * @param chassisNumber
	 *            the chassis number
	 * @param name
	 *            the name
	 * @param weightKg
	 *            the weight
	 * @return a part data transfer object
	 * @throws RemoteException
	 **/
	PartDTO create(String chassisNumber, String name, double weightKg) throws RemoteException;

	/**
	 * Reads all part entries with the chassis number specified as a paramter in the
	 * parts entity and creates a collection of part data transfer objects
	 * 
	 * @param carChassisNumber
	 *            the chassis number of the car that the part belonged to
	 * @return a collection of part data transfer objects
	 * @throws RemoteException
	 **/
	Collection<PartDTO> read(String carChassisNumber) throws RemoteException;

	/**
	 * Reads all part entries from the parts entity that belong to the pallet with
	 * the pallet id specified as a parameter and returns a collection of data
	 * transfer objects
	 * 
	 * @param palletId
	 *            the id of the pallet that the parts belong to
	 * @return a collection of part data trasnfer objects
	 * @throws RemoteException
	 **/
	Collection<PartDTO> read(int palletId) throws RemoteException;

	/**
	 * Reads all part entries from the parts entity and returns a collection of part
	 * data transfer objcets
	 * 
	 * @return a collection of part data transfer objects
	 * @throws RemoteException
	 **/
	Collection<PartDTO> readAll() throws RemoteException;

	/**
	 * Updates the entry in the part entity that matches the part id of the part
	 * data transfer object passed as a parameter
	 * 
	 * @param updatedPartDto
	 *            the updated part object
	 * @return true, if a part entry has been updated. Otherwise, false
	 * @throws RemoteException
	 **/
	boolean update(PartDTO updatedPartDto) throws RemoteException;

	/**
	 * Deletes the entry in the part entity that matches the part if od the part
	 * data transfer object passed as a paramter
	 * 
	 * @param partDto
	 *            the part
	 * @return true, if a part entry has been deleted. Otherwise, false
	 * @throws RemoteException
	 **/
	boolean delete(PartDTO partDto) throws RemoteException;

}
