package remote.dao.pallet;

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
import remote.dao.part.IPartDAO;
import util.CollectionUtils;

public class RemotePalletDAOServer extends UnicastRemoteObject implements IPalletDAO {

	private static final long serialVersionUID = 1L;

	private IPartDAO partDao;
	private DatabaseHelper<PalletDTO> palletDb;

	public RemotePalletDAOServer(IPartDAO partDao) throws RemoteException {
		palletDb = new DatabaseHelper<>(
				DatabaseHelper.CAR_FACILITY_DB_URL, 
				DatabaseHelper.POSTGRES_USERNAME,
				DatabaseHelper.POSTGRES_PASSWORD);
		this.partDao = partDao;
	}

	@Override
	public PalletDTO create(String palletType, List<PartDTO> parts) throws RemoteException {
		// weight the parts
		double weightKg = parts.stream().mapToDouble(PartDTO::getWeightKg).sum();
		
		// create pallet
		int id = palletDb.executeUpdateReturningId(
				"INSERT INTO car_facility_schema.pallets (pallet_type, total_weight_kg) VALUES (?, ?) "
						+ "RETURNING id;",
				palletType, weightKg);

		// create associations between the pallet and all the belonging parts
		associateParts(id, parts);
		
		// convert to array
		PartDTO[] partDtos = CollectionUtils.toPartDTOArray(parts);
		
		return new PalletDTO(id, palletType, partDtos, weightKg);
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
		associateParts(palletDto.getId(), Arrays.asList(palletDto.getParts()));

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

	@Override
	public void removePartAssociations(int palletId, PartDTO... parts) throws RemoteException {
		for (PartDTO part : parts) 
			palletDb.executeUpdate("DELETE FROM car_facility_schema.contains WHERE pallet_id = ? AND "
					+ "part_id = ?;", palletId, part.getId());
	}

	/**
	 * Associate only the parts that have not been associated already with the pallet id passed as a parameter
	 * 
	 *  @param palletId
	 *  		the id of the pallet
	 *  @param parts 
	 *  		the parts
	 *  @throws RemoteException
	 **/
	private void associateParts(int palletId, List<PartDTO> parts) throws RemoteException {
		for (PartDTO part : parts) 
			palletDb.executeUpdate("INSERT INTO car_facility_schema.contains"
					+ " (part_id, pallet_id) SELECT ?, ? WHERE NOT EXISTS(SELECT *"
					+ " FROM car_facility_schema.contains WHERE contains.part_id = " 
					+ "? AND contains.pallet_id = ?);",
					part.getId(), palletId, part.getId(), palletId);
	}

	/**
	 * Creates a pallet data transfer object from a database result set
	 * 
	 * @param rs
	 *            the result set
	 * @return a pallet data transfer object
	 * @throws SQLException
	 * @throws RemoteException
	 **/
	private PalletDTO createPallet(ResultSet rs) throws SQLException, RemoteException {
		int palletId = rs.getInt(PalletEntityConstants.ID_COLUMN);
		String palletType = rs.getString(PalletEntityConstants.PALLET_TYPE_COLUMN);
		double weightKg = rs.getDouble(PalletEntityConstants.TOTAL_WEIGHT_KG_COLUMN);

		try {

			// read all the parts of this pallet
			Collection<PartDTO> parts = partDao.readPalletParts(palletId);
			
			// convert them to an array
			PartDTO[] partDtos = CollectionUtils.toPartDTOArray(parts);

			return new PalletDTO(palletId, palletType, partDtos, weightKg);
		} catch (Exception e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

}
