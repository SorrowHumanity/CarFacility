package service.registration_station;

import java.rmi.RemoteException;
import dto.car.CarDTO;
import dto.part.PartDTO;

/**
 * Javadoc is the same as the one for IRegistrationBase, with the exception that
 * IRegistrationStationService uses DTO objects as parameters and return types
 **/
public interface IRegistrationStationService {

	CarDTO registerCar(String chassisNumber, String model, PartDTO[] parts) throws RemoteException;

	CarDTO getCar(String chassisNumber) throws RemoteException;

	CarDTO[] getAllCars() throws RemoteException;

}
