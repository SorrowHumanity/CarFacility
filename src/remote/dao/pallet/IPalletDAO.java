package remote.dao.pallet;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

import dto.pallet.PalletDTO;
import dto.part.PartDTO;

public interface IPalletDAO extends Remote {

	PalletDTO create(String palletType, List<PartDTO> parts) throws RemoteException;

	PalletDTO read(int id) throws RemoteException;

	Collection<PalletDTO> readAll() throws RemoteException;

	boolean update(PalletDTO palletDTO) throws RemoteException;

	boolean delete(PalletDTO palletDTO) throws RemoteException;
	
	boolean addToPallet(PalletDTO palletDTO, List<PartDTO> partDTOs) throws RemoteException;

}
