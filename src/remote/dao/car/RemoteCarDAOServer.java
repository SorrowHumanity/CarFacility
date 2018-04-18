package remote.dao.car;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import dto.car.CarDTO;
import dto.part.PartDTO;
import persistence.DatabaseHelper;
import remote.base.dismantle_station.DismantleBaseLocator;
import util.Utils;

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
	public CarDTO create(String chassisNumber, String model, List<PartDTO> parts) throws RemoteException {
		// weight parts
		double weight = Utils.weightParts(parts);
		
		// update database
		carDB.executeUpdate(
				"INSERT INTO car_facility_schema.cars (chassis_number, model, weight_kg) VALUES (?, ?, ?);",
				chassisNumber, model, weight);

		return new CarDTO(chassisNumber, model, Utils.toArray(parts));
	}

	@Override
	public CarDTO read(String chassisNumber) throws RemoteException {
		return carDB.mapSingle((rs) -> createCar(rs),
				"SELECT * FROM car_facility_schema.cars" + " WHERE chassis_number = ?;", chassisNumber);
	}

	@Override
	public Collection<CarDTO> readAll() throws RemoteException {
		return carDB.map((rs) -> createCar(rs), "SELECT * FROM car_facility_schema.cars;");
	}

	@Override
	public boolean update(CarDTO carDTO) throws RemoteException {
		int rowsAffected = carDB.executeUpdate(
				"UPDATE car_facility_schema.cars" + " SET model = ?, weight_kg = ?" + " WHERE chassis_number = ?;",
				carDTO.getModel(), carDTO.getWeight(), carDTO.getChassisNumber());

		return rowsAffected != 0;
	}

	@Override
	public boolean delete(CarDTO carDTO) throws RemoteException {
		int rowsAffected = carDB.executeUpdate("DELETE FROM car_facility_schema.cars" + " WHERE chassis_number = ?;",
				carDTO.getChassisNumber());

		return rowsAffected != 0;
	}

	private CarDTO createCar(ResultSet rs) throws SQLException {
		String chassisNumber = rs.getString("chassis_number");
		String model = rs.getString("model");
		try {
			PartDTO[] parts = DismantleBaseLocator.lookupBase(DismantleBaseLocator.DISMANTLE_BASE_ID)
					.getParts(chassisNumber);
			return new CarDTO(chassisNumber, model, parts);
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	}

}
