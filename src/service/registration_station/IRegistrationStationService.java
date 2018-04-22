package service.registration_station;

import java.rmi.RemoteException;
import java.util.List;

import dto.car.CarDTO;
import dto.part.PartDTO;

public interface IRegistrationStationService {

	CarDTO registerCar(String chassisNumber, String model, List<PartDTO> parts) throws RemoteException;

	CarDTO getCar(String chassisNumber) throws RemoteException;

	CarDTO[] getAllCars() throws RemoteException;

}
