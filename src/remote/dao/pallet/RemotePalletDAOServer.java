package remote.dao.pallet;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import dto.pallet.PalletDTO;
import dto.part.PartDTO;
import persistence.DatabaseHelper;
import remote.dao.part.RemotePartDAOServer;

public class RemotePalletDAOServer extends UnicastRemoteObject implements IPalletDAO {

	private static final long serialVersionUID = 1L;

	private DatabaseHelper<PalletDTO> palletDB;

	public RemotePalletDAOServer() throws RemoteException {
		palletDB = new DatabaseHelper<>(
				DatabaseHelper.CAR_FACILITY_DB_URL, 
				DatabaseHelper.POSTGRES_USERNAME,
				DatabaseHelper.POSTGRES_PASSWORD);
	}

	@Override
	public PalletDTO create(String palletType, List<PartDTO> palletParts) throws RemoteException {
		// create pallet
		int id = palletDB.executeUpdateReturningId(
				"INSERT INTO car_facility_schema.pallets (pallet_type,"
				+ " total_weight_kg) VALUES (?, ?);", palletType,
				getPalletWeight(palletParts));

		// create associations between the pallet and all the parts belonging to the
		// pallet
		associateParts(id, palletParts);

		return new PalletDTO(id, palletType, palletParts);
	}

	@Override
	public PalletDTO read(int id) throws RemoteException {
		return palletDB.mapSingle((rs) -> {
			try {
				return createPallet(rs);
			} catch (RemoteException e) {
				e.printStackTrace();
				return null;
			}
		}, "SELECT * FROM car_facility_schema.pallets WHERE pallets.id = ?;", id);
	}

	@Override
	public Collection<PalletDTO> readAll() throws RemoteException {
		return palletDB.map((rs) -> {
			try {
				return createPallet(rs);
			} catch (RemoteException e) {
				e.printStackTrace();
				return null;
			}
		}, "SELECT * FROM car_facility_schema.pallets;");
	}

	@Override
	public boolean update(PalletDTO palletDTO) throws RemoteException {
		// update entry in pallets entity
		int rowsAffected = palletDB.executeUpdate(
				"UPDATE car_facility_schema.pallets SET"
				+ " pallet_type = ?," + "total_weight_kg = ? WHERE id = ?;",
				palletDTO.getPalletType(), palletDTO.getTotalWeightKg(), palletDTO.getId());

		return rowsAffected != 0;
	}

	@Override
	public boolean delete(PalletDTO palletDTO) throws RemoteException {
		// remove all part associations
		palletDB.executeUpdate("DELETE FROM car_facility_schema.contains"
				+ " WHERE pallet_id = ?;", palletDTO.getId());

		// remove entry from pallets entity
		int rowsAffected = palletDB.executeUpdate("DELETE FROM"
				+ " car_facility_schema.pallets WHERE id = ?;",
				palletDTO.getId());

		return rowsAffected != 0;
	}

	private double getPalletWeight(List<PartDTO> allParts) {
		double totalWeight = 0;

		for (PartDTO part : allParts)
			totalWeight += part.getWeightKg();

		return totalWeight;
	}

	private void associateParts(int palletId, List<PartDTO> allParts) throws RemoteException {
		for (PartDTO part : allParts) {
			palletDB.executeUpdate("INSERT INTO car_facility_schema.contains"
					+ " (pallet_id, part_id) VALUES (?, ?);",
					palletId, part.getId());
		}
	}

	private PalletDTO createPallet(ResultSet rs) throws SQLException, RemoteException {
		int palletId = rs.getInt("id");
		String palletType = rs.getString("pallet_type");
		List<PartDTO> parts = getParts(palletId);
		
		return new PalletDTO(palletId, palletType, parts);
	}

	private List<PartDTO> getParts(int palletId) throws RemoteException {
		DatabaseHelper<PartDTO> partsDB = new DatabaseHelper<>(
				DatabaseHelper.CAR_FACILITY_DB_URL, 
				DatabaseHelper.POSTGRES_USERNAME,
				DatabaseHelper.POSTGRES_PASSWORD);
		
		return partsDB.map((rs) -> RemotePartDAOServer.createPart(rs),
				"SELECT car_facility_schema.parts.id, car_facility_schema.parts.name,"
						+ " car_facility_schema.parts.car_chassis_number, car_facility_schema.parts.weight_kg "
						+ "FROM car_facility_schema.parts, car_facility_schema.contains, car_facility_schema.pallets "
						+ "WHERE contains.pallet_id = ? AND parts.id = contains.part_id;",
				palletId);
	}

}
