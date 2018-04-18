package remote.dao.pallet;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import dto.pallet.PalletDTO;
import dto.part.PartDTO;
import persistence.DatabaseHelper;
import remote.base.dismantle_station.DismantleBaseLocator;
import remote.model.part.IPart;
import util.Utils;

public class RemotePalletDAOServer extends UnicastRemoteObject implements IPalletDAO {

	private static final long serialVersionUID = 1L;

	private DatabaseHelper<PalletDTO> palletDb;

	public RemotePalletDAOServer() throws RemoteException {
		palletDb = new DatabaseHelper<>(
				DatabaseHelper.CAR_FACILITY_DB_URL,
				DatabaseHelper.POSTGRES_USERNAME,
				DatabaseHelper.POSTGRES_PASSWORD);
	}

	@Override
	public PalletDTO create(String palletType, PartDTO[] palletParts) throws RemoteException {
		double weight = Utils.weightParts(Arrays.asList(palletParts));
		// create pallet
		int id = palletDb.executeUpdateReturningId(
				"INSERT INTO car_facility_schema.pallets (pallet_type, total_weight_kg) VALUES (?, ?) "
				+ "RETURNING id;", palletType, weight);

		// create associations between the pallet and all the belonging parts
		associateParts(id, palletParts);

		return new PalletDTO(id, palletType, palletParts, weight);
	}

	@Override
	public PalletDTO read(int palletId) throws RemoteException {
		return palletDb.mapSingle((rs) -> {
			try {
				return createPallet(rs);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}, "SELECT * FROM car_facility_schema.pallets WHERE pallets.id = ?;", palletId);
	}

	@Override
	public Collection<PalletDTO> readAll() throws RemoteException {
		return palletDb.map((rs) -> {
			try {
				return createPallet(rs);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}, "SELECT * FROM car_facility_schema.pallets;");
	}

	@Override
	public boolean update(PalletDTO palletDto) throws RemoteException {
		// update database
		int rowsAffected = palletDb.executeUpdate(
				"UPDATE car_facility_schema.pallets SET pallet_type = ?, total_weight_kg = ? WHERE id = ?;",
				palletDto.getPalletType(), palletDto.getWeightKg(), palletDto.getId());

		// update the contents of the pallet
		associateParts(palletDto.getId(), palletDto.getParts());

		return rowsAffected != 0;
	}

	@Override
	public boolean delete(PalletDTO palletDto) throws RemoteException {
		// remove all part associations to the pallet
		palletDb.executeUpdate("DELETE FROM car_facility_schema.contains WHERE pallet_id = ?;", palletDto.getId());

		// remove pallet
		int rowsAffected = palletDb.executeUpdate("DELETE FROM car_facility_schema.pallets WHERE id = ?;",
				palletDto.getId());

		return rowsAffected != 0;
	}

	private void associateParts(int palletId, PartDTO[] allParts) throws RemoteException {
		for (PartDTO part : allParts) {
			palletDb.executeUpdate("INSERT INTO car_facility_schema.contains"
					+ " (part_id, pallet_id) SELECT ?, ? WHERE NOT EXISTS(SELECT *"
					+ " FROM car_facility_schema.contains WHERE contains.part_id = "
					+ "? AND contains.pallet_id = ?);",
					part.getId(), palletId, part.getId(), palletId);
		}
	}

	private PalletDTO createPallet(ResultSet rs) throws SQLException, RemoteException, MalformedURLException {
		int palletId = rs.getInt("id");
		String palletType = rs.getString("pallet_type");
		double weightKg = rs.getDouble("total_weight_kg");
		
		// DismantleBase is required to get all parts for each pallet
		List<IPart> parts = null;
		try {
			
			parts = DismantleBaseLocator.lookupBase(DismantleBaseLocator.DISMANTLE_BASE_ID).getParts(palletId);
			PalletDTO palletDto = new PalletDTO(palletId, palletType, Utils.toDTOArray(parts), weightKg);
			
			return palletDto;
		} catch (Exception e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

}
