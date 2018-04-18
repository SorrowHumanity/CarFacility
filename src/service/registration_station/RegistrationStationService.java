package service.registration_station;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import dto.car.CarDTO;
import dto.part.PartDTO;
import remote.base.registration_station.IRegistrationBase;
import remote.base.registration_station.RegistrationBaseLocator;
import util.Utils;

@WebService
public class RegistrationStationService {

	private IRegistrationBase registrationBase;

	public RegistrationStationService() throws MalformedURLException, RemoteException, NotBoundException {
		registrationBase = RegistrationBaseLocator.lookupBase(RegistrationBaseLocator.REGISTRATION_BASE_ID);
	}

	@WebMethod
	public CarDTO registerCar(String chassisNumber, String model, List<PartDTO> parts) throws RemoteException {
		return new CarDTO(registrationBase.registerCar(chassisNumber, model, parts));
	}

	@WebMethod
	public CarDTO getCar(String chassisNumber) throws RemoteException {
		return new CarDTO(registrationBase.getCar(chassisNumber));
	}

	@WebMethod
	public CarDTO[] getAllCars() throws RemoteException {
		CarDTO[] allcars = Utils.toCarDTOArray(registrationBase.getAllCars());
		return allcars;
	}

}
