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
import util.Utils;

public class RemoteRegistrationBase extends UnicastRemoteObject implements IRegistrationBase {

	private static final long serialVersionUID = 1L;

	private Map<String, ICar> carCache = new HashMap<>();

	private ICarDAO carDAO;

	public RemoteRegistrationBase(ICarDAO carDAO) throws RemoteException {
		this.carDAO = carDAO;
	}

	@Override
	public CarDTO registerCar(String chassisNumber, String model, List<PartDTO> parts) throws RemoteException {
		// create database entry
		CarDTO carDTO = carDAO.create(chassisNumber, model, parts);

		// cache
		carCache.put(chassisNumber, new RemoteCar(carDTO));
		
		return carDTO;
	}

	@Override
	public CarDTO getCar(String chassisNumber) throws RemoteException {
		// check if car is cached
		if (!carCache.containsKey(chassisNumber)) {

			// read car from the database
			CarDTO carDTO = carDAO.read(chassisNumber);

			// cache car
			carCache.put(chassisNumber, new RemoteCar(carDTO));
		}

		return new CarDTO(carCache.get(chassisNumber));
	}

	@Override
	public CarDTO[] getAllCars() throws RemoteException {
		// read all cars from the database
		Collection<CarDTO> allCars = carDAO.readAll();

		// create output collection
		LinkedList<CarDTO> carList = new LinkedList<>();

		// cache if not already cached
		for (CarDTO car : allCars) {
			if (!carCache.containsKey(car.getChassisNumber()))
				carCache.put(car.getChassisNumber(), new RemoteCar(car));

			// add to output collection
			carList.add(car);
		}

		return Utils.toArray(carList);
	}

}
