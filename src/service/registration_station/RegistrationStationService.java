package service.registration_station;

import java.rmi.RemoteException;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import dto.car.CarDTO;
import dto.part.PartDTO;
import remote.base.registration_station.IRegistrationBase;
import remote.base.registration_station.RegistrationBaseLocator;
import util.CollectionUtils;

@WebService
public class RegistrationStationService implements IRegistrationStationService {

	private IRegistrationBase registrationBase;

	public RegistrationStationService() throws RemoteException {
		try {
			registrationBase = RegistrationBaseLocator.lookupBase();
		} catch (Exception e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

	@WebMethod
	@Override
	public CarDTO registerCar(String chassisNumber, String model, List<PartDTO> parts) throws RemoteException {
		return new CarDTO(registrationBase.registerCar(chassisNumber, model, parts));
	}

	@WebMethod
	@Override
	public CarDTO getCar(String chassisNumber) throws RemoteException {
		return new CarDTO(registrationBase.getCar(chassisNumber));
	}

	@WebMethod
	@Override
	public CarDTO[] getAllCars() throws RemoteException {
		CarDTO[] allcars = CollectionUtils.toCarDTOArray(registrationBase.getAllCars());
		return allcars;
	}

}
