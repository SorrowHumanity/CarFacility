package remote.dao.car;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import dto.car.CarDTO;
import dto.part.PartDTO;
import persistence.DatabaseHelper;
import remote.dao.part.RemotePartDAOManager;

public class RemoteCarDAOServer extends UnicastRemoteObject implements ICarDAO {

	private static final long serialVersionUID = 1L;

	private DatabaseHelper<CarDTO> carDB;

	public RemoteCarDAOServer() throws RemoteException {
		carDB = new DatabaseHelper<>(
				DatabaseHelper.CAR_FACILITY_DB_URL,
				DatabaseHelper.POSTGRES_USERNAME,
				DatabaseHelper.POSTGRES_PASSWORD);
	}

	@Override
	public CarDTO create(CarDTO carDTO) throws RemoteException {
		carDB.executeUpdate(
				"INSERT INTO car_facility_schema.cars"
				+ " (chassis_number, model, weight_kg) VALUES (?, ?, ?);",
				carDTO.getChassisNumber(), carDTO.getModel(), carDTO.getWeight());

		return carDTO;
	}

	@Override
	public CarDTO read(String chassisNumber) throws RemoteException {
		return carDB.mapSingle((rs) -> createCar(rs),
				"SELECT * FROM car_facility_schema.cars"
				+ " WHERE chassis_number = ?;", chassisNumber);
	}

	@Override
	public Collection<CarDTO> readAll() throws RemoteException {
		return carDB.map((rs) -> createCar(rs), "SELECT * FROM car_facility_schema.cars");
	}

	@Override
	public boolean update(CarDTO carDTO) throws RemoteException {
		int rowsAffected = carDB.executeUpdate("UPDATE car_facility_schema.cars"
				+ " SET model = ?, weight = ?"
				+ " WHERE chassis_number = ?;", 
				carDTO.getModel(), carDTO.getWeight(), carDTO.getChassisNumber());
		
		return rowsAffected != 0;
	}

	@Override
	public boolean delete(CarDTO carDTO) throws RemoteException {
		int rowsAffected = carDB.executeUpdate("DELETE FROM car_facility_schema.cars"
				+ " WHERE chassis_number = ?;", carDTO.getChassisNumber());
		
		return rowsAffected != 0;
	}

	private CarDTO createCar(ResultSet rs) throws SQLException {
		String chassisNumber = rs.getString("chassis_number");
		String model = rs.getString("model");
		Collection<PartDTO> parts = null;
		try {
			parts = RemotePartDAOManager.lookupDAO().read(chassisNumber); // get the parts of the car
		} catch (RemoteException | MalformedURLException | NotBoundException e) {
		}

		return new CarDTO(chassisNumber, model, new LinkedList<>(parts));
	}

}
