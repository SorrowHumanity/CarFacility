package service.registration_station;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import javax.jws.WebMethod;
import javax.jws.WebService;

import dto.car.CarDTO;
import remote.base.registration_station.IRegistrationBase;
import remote.base.registration_station.RegistrationBaseLocator;

@WebService
public class RegistrationStationService {

	private IRegistrationBase registrationBase;

	public RegistrationStationService() throws MalformedURLException, RemoteException, NotBoundException {
		registrationBase = RegistrationBaseLocator.lookupBase(RegistrationBaseLocator.REGISTRATION_BASE_ID);
	}

	@WebMethod
	public CarDTO getCar(String chassisNumber) throws RemoteException {
		return registrationBase.getCar(chassisNumber);
	}

}
