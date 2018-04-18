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

	private DatabaseHelper<PartDTO> partsDb;

	public RemotePartDAOServer() throws RemoteException {
		partsDb = new DatabaseHelper<>(
				DatabaseHelper.CAR_FACILITY_DB_URL, 
				DatabaseHelper.POSTGRES_USERNAME,
				DatabaseHelper.POSTGRES_PASSWORD);
	}

	@Override
	public PartDTO create(String chassisNumber, String name, double weightKg) throws RemoteException {
		// create database entry
		int id = partsDb
				.executeUpdateReturningId("INSERT INTO "
						+ "car_facility_schema.parts (car_chassis_number, name, weight_kg)"
						+ " VALUES (?, ?, ?) RETURNING id;", chassisNumber, name, weightKg);
		
		return new PartDTO(id, chassisNumber, name, weightKg);
	}

	@Override
	public Collection<PartDTO> read(String chassisNumber) throws RemoteException {
		return partsDb.map((rs) -> createPart(rs), 
		"SELECT * FROM car_facility_schema.parts WHERE parts.car_chassis_number = ?;", chassisNumber);
	}

	@Override
	public Collection<PartDTO> read(int palletId) throws RemoteException {
		return partsDb.map((rs) -> createPart(rs),
				"SELECT car_facility_schema.parts.id, car_facility_schema.parts.name,"
						+ " car_facility_schema.parts.car_chassis_number, car_facility_schema.parts.weight_kg "
						+ "FROM car_facility_schema.parts, car_facility_schema.contains, car_facility_schema.pallets "
						+ "WHERE contains.pallet_id = ? AND parts.id = contains.part_id;",
				palletId);
	}

	@Override
	public Collection<PartDTO> readAll() throws RemoteException {
		return partsDb.map((rs) -> createPart(rs), "SELECT * FROM car_facility_schema.parts;");
	}

	@Override
	public boolean update(PartDTO partDTO) throws RemoteException {
		// update database
		int rowsAffected = partsDb.executeUpdate("UPDATE car_facility_schema.parts" + 
				" SET car_chassis_number = ?, name = ?, weight_kg = ? WHERE id = ?;", 
				partDTO.getCarChassisNumber(), partDTO.getName(), partDTO.getWeightKg(), partDTO.getId());
		
		return rowsAffected != 0;
	}

	@Override
	public boolean delete(PartDTO partDTO) throws RemoteException {
		// update database
		int rowsAffected = partsDb.executeUpdate("DELETE FROM car_facility_schema.parts WHERE parts.id = ?;",
				partDTO.getId());
		
		return rowsAffected != 0;
	}
	
	private PartDTO createPart(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		String carChassisNumber = rs.getString("car_chassis_number");
		String name = rs.getString("name");
		double weightKg = rs.getDouble("weight_kg");
		
		return new PartDTO(id, carChassisNumber, name, weightKg);
	}

}
