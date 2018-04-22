package remote.base.registration_station;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import dto.car.CarDTO;
import dto.part.PartDTO;
import remote.dao.car.ICarDAO;
import remote.model.car.ICar;
import remote.model.car.RemoteCar;

public class RemoteRegistrationBase extends UnicastRemoteObject implements IRegistrationBase {

	private static final long serialVersionUID = 1L;

	private Map<String, ICar> carCache = new HashMap<>();
	private ICarDAO carDao;

	public RemoteRegistrationBase(ICarDAO carDao) throws RemoteException {
		this.carDao = carDao;
	}

	@Override
	public synchronized ICar registerCar(String chassisNumber, String model, List<PartDTO> parts) throws RemoteException {
		// create database entry
		CarDTO carDto = carDao.create(chassisNumber, model, parts);

		// cache and return
		carCache.put(chassisNumber, new RemoteCar(carDto));
		
		return carCache.get(chassisNumber);
	}

	@Override
	public synchronized ICar getCar(String chassisNumber) throws RemoteException {
		// check if car is cached
		if (!carCache.containsKey(chassisNumber)) {

			// read car from the database
			CarDTO carDto = carDao.read(chassisNumber);

			// cache car
			carCache.put(chassisNumber, new RemoteCar(carDto));
		}

		return carCache.get(chassisNumber);
	}

	@Override
	public synchronized List<ICar> getAllCars() throws RemoteException {
		// read all cars from the database
		Collection<CarDTO> allCars = carDao.readAll();

		// create output collection
		LinkedList<ICar> carList = new LinkedList<>();

		// cache if not already cached
		for (CarDTO car : allCars) {
			if (!carCache.containsKey(car.getChassisNumber()))
				carCache.put(car.getChassisNumber(), new RemoteCar(car));

			// add to output collection
			carList.add(carCache.get(car.getChassisNumber()));
		}

		return carList;
	}

}
