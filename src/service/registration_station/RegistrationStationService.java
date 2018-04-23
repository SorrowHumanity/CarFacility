package service.registration_station;

import java.rmi.RemoteException;
import java.util.Arrays;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import dto.car.CarDTO;
import dto.part.PartDTO;
import remote.base.registration_station.IRegistrationBase;
import remote.base.registration_station.RegistrationBaseLocator;
import remote.model.car.ICar;
import util.CollectionUtils;

@WebService
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
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
		ICar car = registrationBase.registerCar(chassisNumber, model, Arrays.asList(parts));
		return new CarDTO(car);
	}

	@WebMethod
	@Override
	public CarDTO getCar(String chassisNumber) throws RemoteException {
		ICar car = registrationBase.getCar(chassisNumber);
		return new CarDTO(car);
	}

	@WebMethod
	@Override
	public CarDTO[] getAllCars() throws RemoteException {
		CarDTO[] allcars = CollectionUtils.toCarDTOArray(registrationBase.getAllCars());
		return allcars;
	}

}
