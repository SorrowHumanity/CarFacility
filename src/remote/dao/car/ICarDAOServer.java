package remote.dao.car;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import dto.car.CarDTO;

public interface ICarDAOServer extends Remote {

	CarDTO create(CarDTO carDTO) throws RemoteException;

	CarDTO read(String chassisNumber) throws RemoteException;

	Collection<CarDTO> readAll() throws RemoteException;

	boolean update(CarDTO carDTO) throws RemoteException;

	boolean delete(CarDTO carDTO) throws RemoteException;

}
