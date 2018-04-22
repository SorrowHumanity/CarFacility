package remote.dao.part;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import dto.part.PartDTO;
import persistence.DatabaseHelper;

public class RemotePartDAOServer extends UnicastRemoteObject implements IPartDAO {

	private static final long serialVersionUID = 1L;

	private DatabaseHelper<PartDTO> partDb;

	public RemotePartDAOServer() throws RemoteException {
		partDb = new DatabaseHelper<>(
				DatabaseHelper.CAR_FACILITY_DB_URL,
				DatabaseHelper.POSTGRES_USERNAME,
				DatabaseHelper.POSTGRES_PASSWORD);
	}

	@Override
	public PartDTO create(String chassisNumber, String name, double weightKg) throws RemoteException {
		// create database entry
		int id = partDb.executeUpdateReturningId("INSERT INTO "
				+ "car_facility_schema.parts (car_chassis_number, name, weight_kg)" 
				+ " VALUES (?, ?, ?) RETURNING id;",
				chassisNumber, name, weightKg);

		return new PartDTO(id, chassisNumber, name, weightKg);
	}

	@Override
	public Collection<PartDTO> read(String chassisNumber) throws RemoteException {
		return partDb.map((rs) -> createPart(rs),
				"SELECT * FROM car_facility_schema.parts WHERE parts.car_chassis_number = ?;", chassisNumber);
	}

	@Override
	public Collection<PartDTO> read(int palletId) throws RemoteException {
		return partDb.map((rs) -> createPart(rs),
				"SELECT car_facility_schema.parts.id, car_facility_schema.parts.name,"
						+ " car_facility_schema.parts.car_chassis_number, car_facility_schema.parts.weight_kg "
						+ "FROM car_facility_schema.parts, car_facility_schema.contains, car_facility_schema.pallets "
						+ "WHERE contains.pallet_id = ? AND parts.id = contains.part_id GROUP BY parts.id;",
				palletId);
	}

	@Override
	public Collection<PartDTO> read(Integer shipmentId) throws RemoteException {
		return partDb.map((rs) -> createPart(rs),
				"SELECT car_facility_schema.parts.id, car_facility_schema.parts.name,"
						+ " car_facility_schema.parts.car_chassis_number, car_facility_schema.parts.weight_kg "
						+ "FROM car_facility_schema.parts, car_facility_schema.contains, car_facility_schema.pallets "
						+ "WHERE contains.pallet_id = ? AND parts.id = contains.part_id;",
				shipmentId);
	}

	@Override
	public Collection<PartDTO> readAll() throws RemoteException {
		return partDb.map((rs) -> createPart(rs), "SELECT * FROM car_facility_schema.parts;");
	}

	@Override
	public boolean update(PartDTO updatedPartDto) throws RemoteException {
		// update database
		int rowsAffected = partDb.executeUpdate(
				"UPDATE car_facility_schema.parts"
						+ " SET car_chassis_number = ?, name = ?, weight_kg = ? WHERE id = ?;",
				updatedPartDto.getCarChassisNumber(), updatedPartDto.getName(), updatedPartDto.getWeightKg(),
				updatedPartDto.getId());

		return rowsAffected != 0;
	}

	@Override
	public boolean delete(PartDTO partDto) throws RemoteException {
		// update database
		int rowsAffected = partDb.executeUpdate("DELETE FROM car_facility_schema.parts WHERE parts.id = ?;",
				partDto.getId());

		return rowsAffected != 0;
	}

	/**
	 * Creates a part data transfer object from a database result set
	 *
	 * @param rs
	 * 			the result set
	 * @return a part data transfer object
	 * @throws SQLException
	 **/
	private PartDTO createPart(ResultSet rs) throws SQLException {
		int id = rs.getInt(PartEntityConstants.ID_COLUMN);
		String carChassisNumber = rs.getString(PartEntityConstants.CAR_CHASSIS_NUMBER_COLUMN);
		String name = rs.getString(PartEntityConstants.NAME_COLUMN);
		double weightKg = rs.getDouble(PartEntityConstants.WEIGHT_KG_COLUMN);

		return new PartDTO(id, carChassisNumber, name, weightKg);
	}

}
