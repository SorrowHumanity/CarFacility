package service.registration_station;

import java.rmi.RemoteException;
import javax.jws.WebMethod;
import javax.jws.WebService;
import dto.car.CarDTO;
import dto.part.PartDTO;
import remote.base.registration.IRegistrationBase;
import remote.base.registration.RegistrationBaseLocator;
import remote.model.car.ICar;
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
	public CarDTO registerCar(String chassisNumber, String model, PartDTO[] parts) throws RemoteException {
		ICar car = registrationBase.registerCar(chassisNumber, model, CollectionUtils.toRemotePartsList(parts));
		CarDTO carDto = new CarDTO(car);
		return carDto;
	}

	@WebMethod
	@Override
	public CarDTO getCar(String chassisNumber) throws RemoteException {
		ICar car = registrationBase.getCar(chassisNumber);
		CarDTO carDto = new CarDTO(car);
		return carDto;
	}

	@WebMethod
	@Override
	public CarDTO[] getAllCars() throws RemoteException {
		CarDTO[] allcars = CollectionUtils.toCarDTOArray(registrationBase.getAllCars());
		return allcars;
	}

}
