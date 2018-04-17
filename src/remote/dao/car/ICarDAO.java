package remote.dao.car;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

import dto.car.CarDTO;
import dto.part.PartDTO;

public interface ICarDAO extends Remote {

	CarDTO create(String chassisNumber, String model, List<PartDTO> parts) throws RemoteException;

	CarDTO read(String chassisNumber) throws RemoteException;

	Collection<CarDTO> readAll() throws RemoteException;

	boolean update(CarDTO carDTO) throws RemoteException;

	boolean delete(CarDTO carDTO) throws RemoteException;

}
