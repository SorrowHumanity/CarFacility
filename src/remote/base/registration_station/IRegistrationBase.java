package remote.base.registration_station;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import dto.part.PartDTO;
import remote.model.car.ICar;

public interface IRegistrationBase extends Remote {

	ICar registerCar(String chassisNumber, String model, List<PartDTO> parts) throws RemoteException;

	ICar getCar(String chassisNumber) throws RemoteException;

	List<ICar> getAllCars() throws RemoteException;

}
