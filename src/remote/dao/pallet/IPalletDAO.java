package remote.dao.pallet;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import dto.pallet.PalletDTO;
import dto.part.PartDTO;

public interface IPalletDAO extends Remote {

	/**
	 * Creates a pallet entry in the pallets entity
	 * 
	 * @param palletType
	 *            the type of parts that the pallet stores
	 * @param parts
	 *            the parts to be stored in the pallet
	 * @return a pallet data transfer object
	 * @throws RemoteException
	 **/
	PalletDTO create(String palletType, PartDTO[] parts) throws RemoteException;

	/**
	 * Reads a pallet entry from the pallets entity with the specified pallet id,
	 * and all belonging parts. and creates a pallet data transfer object
	 * 
	 * @param palletId
	 *            the id of the pallet
	 * @return a pallet data transfer object
	 * @throws RemoteException
	 **/
	PalletDTO read(int palletId) throws RemoteException;

	/**
	 * Reads all pallet entries from the pallets entity, and all belonging parts
	 * 
	 * @return a collection of pallet data transfer objects
	 * @throws RemoteException
	 **/
	Collection<PalletDTO> readAll() throws RemoteException;

	/**
	 * Updates a pallet entry in the pallets entity, corresponding to the pallet id
	 * of the pallet parameter. Also updates the contents of the pallet (contains
	 * entity)
	 * 
	 * @param updatedPalletDto
	 *            the updated pallet
	 * @return true, if a pallet is updated. Otherwise false
	 * @throws RemoteException
	 **/
	boolean update(PalletDTO updatedPalletDto) throws RemoteException;

	/**
	 * Deletes a pallet entry from the pallets entity with the pallet id of the
	 * pallet parameter and all associations to parts in the contains entity
	 * 
	 * @param palletDto
	 *            the pallet
	 * @return true, if the pallet entry has been deleted. Otherwise, false
	 * @throws RemoteException
	 **/
	boolean delete(PalletDTO palletDto) throws RemoteException;

}
