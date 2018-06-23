package remote.base.registration;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dto.car.CarDTO;
import dto.part.PartDTO;
import remote.model.part.IPart;
import remote.dao.car.ICarDAO;
import remote.model.car.ICar;
import remote.model.car.RemoteCar;
import util.CollectionUtils;

public class RemoteRegistrationBase extends UnicastRemoteObject implements IRegistrationBase {

	private static final long serialVersionUID = 1L;

	private Map<String, ICar> carCache = new HashMap<>();
	private ICarDAO carDao;

	public RemoteRegistrationBase(ICarDAO carDao) throws RemoteException {
		this.carDao = carDao;
	}

	@Override
	public synchronized ICar registerCar(String chassisNumber, String model, List<IPart> parts) throws RemoteException {
		List<PartDTO> partDtos = CollectionUtils.toPartDTOList(parts);
		
		// create database entry
		CarDTO carDto = carDao.create(chassisNumber, model, partDtos);

		ICar newCar = new RemoteCar(carDto);
		
		// cache and return
		carCache.put(chassisNumber, newCar);
		
		return newCar;
	}

	@Override
	public synchronized ICar getCar(String chassisNumber) throws RemoteException {
		// cache if car is not already cached
		if (!carCache.containsKey(chassisNumber)) {
			// read car from the database
			CarDTO carDto = carDao.read(chassisNumber);

			// avoid caching null values
			if (carDto == null) 
				throw new IllegalArgumentException
								("Car with chassis number " + chassisNumber + " does not exist!");
			
			// cache car
			carCache.put(chassisNumber, new RemoteCar(carDto));
		}

		return carCache.get(chassisNumber);
	}

	@Override
	public synchronized List<ICar> getAllCars() throws RemoteException {
		// read all cars from the database
		Collection<CarDTO> allCars = carDao.readAll();

		// cache all cars that have not been cached already
		for (CarDTO car : allCars) 
			if (!carCache.containsKey(car.getChassisNumber()))
				carCache.put(car.getChassisNumber(), new RemoteCar(car));

		return new ArrayList<>(carCache.values());
	}

}
