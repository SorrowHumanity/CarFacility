package remote.base.registration_station;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import dto.part.PartDTO;
import remote.model.car.ICar;

public interface IRegistrationBase extends Remote {

	/**
	 * Registers a new car in the system
	 * 
	 * @param chassisNumber
	 *            the chassis number of the car
	 * @param model
	 *            the model of the car
	 * @param parts
	 *            the parts of the car
	 * @return the newly registered car
	 * @throws RemoteException
	 **/
	ICar registerCar(String chassisNumber, String model, List<PartDTO> parts) throws RemoteException;

	/**
	 * Retrieves a car with the chassis number passed as a parameter
	 * 
	 * @param chassisNumber
	 *            the chassis number of the car
	 * @return the car
	 * @throws RemoteException
	 **/
	ICar getCar(String chassisNumber) throws RemoteException;

	/**
	 * Retrieves all cars registered in the system
	 * 
	 * @return a list of cars
	 * @throws RemoteException
	 **/
	List<ICar> getAllCars() throws RemoteException;

}
