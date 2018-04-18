package remote.dao.car;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import dto.car.CarDTO;
import dto.part.PartDTO;
import persistence.DatabaseHelper;
import remote.base.dismantle_station.DismantleBaseLocator;
import remote.model.part.IPart;
import util.Utils;

public class RemoteCarDAOServer extends UnicastRemoteObject implements ICarDAO {

	private static final long serialVersionUID = 1L;

	private DatabaseHelper<CarDTO> carDb;

	public RemoteCarDAOServer() throws RemoteException {
		carDb = new DatabaseHelper<>(
				DatabaseHelper.CAR_FACILITY_DB_URL,
				DatabaseHelper.POSTGRES_USERNAME,
				DatabaseHelper.POSTGRES_PASSWORD);
	}

	@Override
	public CarDTO create(String chassisNumber, String model, List<PartDTO> parts) throws RemoteException {
		// weight parts
		double weightKg = Utils.weightParts(parts);
		
		// update database
		carDb.executeUpdate(
				"INSERT INTO car_facility_schema.cars (chassis_number, model, weight_kg) VALUES (?, ?, ?);",
				chassisNumber, model, weightKg);

		return new CarDTO(chassisNumber, model, Utils.toPartDTOArray(parts));
	}

	@Override
	public CarDTO read(String chassisNumber) throws RemoteException {
		return carDb.mapSingle((rs) -> createCar(rs),
				"SELECT * FROM car_facility_schema.cars WHERE chassis_number = ?;", chassisNumber);
	}

	@Override
	public Collection<CarDTO> readAll() throws RemoteException {
		return carDb.map((rs) -> createCar(rs), "SELECT * FROM car_facility_schema.cars;");
	}

	@Override
	public boolean update(CarDTO carDto) throws RemoteException {
		// weight parts
		double weightKg = Utils.weightParts(Arrays.asList(carDto.getParts()));
		
		// update database
		int rowsAffected = carDb.executeUpdate(
				"UPDATE car_facility_schema.cars SET model = ?, weight_kg = ? WHERE chassis_number = ?;",
				carDto.getModel(), weightKg, carDto.getChassisNumber());

		return rowsAffected != 0;
	}

	@Override
	public boolean delete(CarDTO carDto) throws RemoteException {
		// update database
		int rowsAffected = carDb.executeUpdate("DELETE FROM car_facility_schema.cars WHERE chassis_number = ?;",
				carDto.getChassisNumber());

		return rowsAffected != 0;
	}

	private CarDTO createCar(ResultSet rs) throws SQLException {
		String chassisNumber = rs.getString("chassis_number");
		String model = rs.getString("model");
		List<IPart> parts = null;
		
		try {
			
			parts = DismantleBaseLocator.lookupBase(DismantleBaseLocator.DISMANTLE_BASE_ID)
					.getParts(chassisNumber);
		
			return new CarDTO(chassisNumber, model, Utils.toDTOArray(parts));
		
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	}

}
