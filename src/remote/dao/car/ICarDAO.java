package remote.dao.car;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

import dto.car.CarDTO;
import dto.part.PartDTO;

public interface ICarDAO extends Remote {

	/**
	 * Creates a car entry in the car entity and a car data transfer object
	 * 
	 * @param chassisNumber
	 *            the car chassis number
	 * @param model
	 *            the model of the car
	 * @param parts
	 *            the parts of the car
	 * @return a car data transfer object
	 * @throws RemoteException
	 **/
	CarDTO create(String chassisNumber, String model, List<PartDTO> parts) throws RemoteException;

	/**
	 * Reads a car entry from the cars entity with the chassis number passed as a
	 * parameter and creates a car data transfer object
	 * 
	 * @param chassisNumber
	 *            the chassis number of the car
	 * @return a car data transfer object
	 * @throws RemoteException
	 **/
	CarDTO read(String chassisNumber) throws RemoteException;

	/**
	 * Reads and returns all car entries from the cars entity
	 * 
	 * @return a collection of car data transfer objects
	 * @throws RemoteException
	 **/
	Collection<CarDTO> readAll() throws RemoteException;

	/**
	 * Updates the car entry in the cars entity with the chassis number of the car
	 * object passed as parameter
	 * 
	 * @param updatedCarDto
	 *            an updated car object
	 * @return true, if at least one car entry is updated. Otherwise, false
	 * @throws RemoteException
	 **/
	boolean update(CarDTO updatedCarDto) throws RemoteException;

	/**
	 * Remove the car entry in the cars entity with the chassis number of the car
	 * object passed as parameter
	 * 
	 * @param carDto
	 *            the car object to be removed
	 * @return true, if the car entry is removed from the cars entity. Otherwise,
	 *         false
	 * @throws RemoteException
	 **/
	boolean delete(CarDTO carDto) throws RemoteException;

}
