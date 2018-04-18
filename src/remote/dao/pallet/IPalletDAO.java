package remote.dao.pallet;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import dto.pallet.PalletDTO;
import dto.part.PartDTO;

public interface IPalletDAO extends Remote {

	PalletDTO create(String palletType, PartDTO[] parts) throws RemoteException;

	PalletDTO read(int id) throws RemoteException;

	Collection<PalletDTO> readAll() throws RemoteException;

	boolean update(PalletDTO palletDTO) throws RemoteException;

	boolean delete(PalletDTO palletDTO) throws RemoteException;

}
