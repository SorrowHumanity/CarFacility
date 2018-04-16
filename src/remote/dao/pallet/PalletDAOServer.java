package remote.dao.pallet;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import dto.pallet.PalletDTO;
import dto.part.PartDTO;
import persistence.DatabaseHelper;
import remote.dao.part.IPartDAO;
import remote.dao.part.PartDAOServer;

public class PalletDAOServer extends UnicastRemoteObject implements IPalletDAO {

	private static final long serialVersionUID = 1L;

	private DatabaseHelper<PalletDTO> palletDB;

	public PalletDAOServer() throws RemoteException {
		palletDB = new DatabaseHelper<>("jdbc:postgresql://localhost:5432" + "/car_facility_system", "postgres",
				"password");
	}

	@Override
	public PalletDTO create(String palletType, List<PartDTO> palletParts) throws RemoteException {
		// create pallet
		int id = palletDB.executeUpdateReturningId(
				"INSERT INTO car_facility_schema.pallets (pallet_type, total_weight)" + " VALUES (?, ?);", palletType,
				getTotalWeightKg(palletParts));
		
		// create associations between the pallet and all the parts belonging to the pallet
		associateParts(id, palletParts);
		
		// create & return PalletDTO
		return new PalletDTO(id, palletType, palletParts);
	}

	@Override
	public PalletDTO read(int id) throws RemoteException {
		return null;
	}

	@Override
	public Collection<PalletDTO> readAll() throws RemoteException {
		return null;
	}

	@Override
	public boolean update(PalletDTO palletDTO) throws RemoteException {
		// update entry in pallets entity
		int rowsAffected = palletDB.executeUpdate("UPDATE car_facility_schema.pallets SET pallet_type = ?,"
				+ "total_weight_kg = ? WHERE id = ?;", 
				palletDTO.getPalletType(), palletDTO.getTotalWeightKg(), palletDTO.getId());
		
		return rowsAffected != 0;
	}

	@Override
	public boolean delete(PalletDTO palletDTO) throws RemoteException {
		// remove all associated parts
		palletDB.executeUpdate("DELETE FROM car_facility_schema.contains"
				+ " WHERE pallet_id = ?;", palletDTO.getId());
		
		// remove entry from pallets entity
		int rowsAffected = palletDB.executeUpdate("DELETE FROM car_facility_schema.pallets WHERE id = ?;",
				palletDTO.getId());
		
		return rowsAffected != 0;
	}

	private double getTotalWeightKg(List<PartDTO> allParts) {
		double totalWeight = 0;

		for (PartDTO part : allParts)
			totalWeight += part.getWeightKg();

		return totalWeight;
	}

	private void associateParts(int palletId, List<PartDTO> allParts) throws RemoteException {
		for (PartDTO part : allParts) {
			palletDB.executeUpdate("INSERT INTO car_facility_schema.contains (pallet_id, part_id) VALUES (?, ?);",
					palletId, part.getId());
		}
	}
	
	public static void main(String[] args) throws RemoteException {
		IPartDAO partDAO = new PartDAOServer();
		IPalletDAO dao = new PalletDAOServer();
		Collection<PartDTO> allParts = partDAO.readAll("123456");
		PartDTO[] parts = new PartDTO[allParts.size()];
		allParts.toArray(parts);
		
		
		
	}

}
