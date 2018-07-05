package service.registration_station;

import java.rmi.RemoteException;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;
import dto.car.CarDTO;
import dto.part.PartDTO;
import remote.base.registration.IRegistrationBase;
import remote.base.registration.RemoteRegistrationBaseLocator;
import remote.domain.car.ICar;
import remote.domain.part.IPart;
import util.CollectionUtils;

@WebService
public class RegistrationStationService implements IRegistrationStationService {

	private IRegistrationBase registrationBase;

	public RegistrationStationService() throws RemoteException {
		try {
			registrationBase = RemoteRegistrationBaseLocator.lookupBase();
		} catch (Exception e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

	@WebMethod
	@Override
	public CarDTO registerCar(String chassisNumber, String model, PartDTO[] parts) throws RemoteException {
		List<IPart> remoteParts = CollectionUtils.toRemotePartList(parts);
		ICar car = registrationBase.registerCar(chassisNumber, model, remoteParts);
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
		List<ICar> remoteCars = registrationBase.getAllCars();
		CarDTO[] allcars = CollectionUtils.toCarDTOArray(remoteCars);
		return allcars;
	}

}
