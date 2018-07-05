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
import remote.dao.part.IPartDAO;
import util.CollectionUtils;

public class RemoteCarDAOServer extends UnicastRemoteObject implements ICarDAO {

	private static final long serialVersionUID = 1L;

	private IPartDAO partDao;
	private DatabaseHelper<CarDTO> carsEntity;

	public RemoteCarDAOServer(IPartDAO partDao) throws RemoteException {
		this.carsEntity = new DatabaseHelper<>(DatabaseHelper.CAR_FACILITY_DB_URL, DatabaseHelper.POSTGRES_USERNAME,
				DatabaseHelper.POSTGRES_PASSWORD);
		this.partDao = partDao;
	}

	@Override
	public CarDTO create(String chassisNumber, String model, List<PartDTO> parts) throws RemoteException {
		// weight all parts
		double weightKg = parts.stream().mapToDouble(PartDTO::getWeightKg).sum();

		// update database
		carsEntity.executeUpdate("INSERT INTO car_facility_schema.cars (chassis_number, model, weight_kg) VALUES (?, ?, ?);",
				chassisNumber, model, weightKg);

		PartDTO[] partDtos = CollectionUtils.toPartDTOArray(parts);

		return new CarDTO(chassisNumber, model, partDtos);
	}

	@Override
	public CarDTO read(String chassisNumber) throws RemoteException {
		return carsEntity.mapSingle((rs) -> createCar(rs),
				"SELECT * FROM car_facility_schema.cars WHERE chassis_number = ?;", chassisNumber);
	}

	@Override
	public Collection<CarDTO> readAll() throws RemoteException {
		return carsEntity.map((rs) -> createCar(rs), "SELECT * FROM car_facility_schema.cars;");
	}

	@Override
	public boolean update(CarDTO updatedCarDto) throws RemoteException {
		// update database
		int rowsAffected = carsEntity.executeUpdate(
				"UPDATE car_facility_schema.cars SET model = ?, weight_kg = ? WHERE chassis_number = ?;",
				updatedCarDto.getModel(), updatedCarDto.getWeightKg(), updatedCarDto.getChassisNumber());

		return rowsAffected != 0;
	}

	@Override
	public boolean delete(CarDTO carDto) throws RemoteException {
		// update database
		int rowsAffected = carsEntity.executeUpdate("DELETE FROM car_facility_schema.cars WHERE chassis_number = ?;",
				carDto.getChassisNumber());

		return rowsAffected != 0;
	}

	/**
	 * Creates a car object from a database result set
	 * 
	 * @param rs
	 *            the result set
	 * @return a car data transfer object
	 * @throws SQLException
	 **/
	private CarDTO createCar(ResultSet rs) {
		try {
			String chassisNumber = rs.getString(CarEntityConstants.CHASSIS_NUMBER_COLUMN);
			String model = rs.getString(CarEntityConstants.MODEL_COLUMN);

			// read the car's parts
			Collection<PartDTO> parts = partDao.readCarParts(chassisNumber);

			// convert them to an array
			PartDTO[] partDtos = CollectionUtils.toPartDTOArray(parts);

			return new CarDTO(chassisNumber, model, partDtos);
		} catch (RemoteException | SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
