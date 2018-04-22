package remote.dao.shipment;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import dto.part.PartDTO;
import dto.shipment.ShipmentDTO;
import persistence.DatabaseHelper;
import remote.dao.part.IPartDAO;
import util.CollectionUtils;

public class RemoteShipmentDAOServer extends UnicastRemoteObject implements IShipmentDAO {

	private static final long serialVersionUID = 1L;

	private IPartDAO partDao;
	private DatabaseHelper<ShipmentDTO> shipmentDb;

	public RemoteShipmentDAOServer(IPartDAO partDao) throws RemoteException {
		shipmentDb = new DatabaseHelper<>(
				DatabaseHelper.CAR_FACILITY_DB_URL,
				DatabaseHelper.POSTGRES_USERNAME,
				DatabaseHelper.POSTGRES_PASSWORD);
		this.partDao = partDao;
	}

	@Override
	public ShipmentDTO create(String receiverFirstName, String receiverLastName, List<PartDTO> parts)
			throws RemoteException {
		int id = shipmentDb.executeUpdateReturningId(
				"INSERT INTO car_facility_schema.shipments (receiver_first_name, receiver_last_name)"
						+ " VALUES (?, ?) RETURNING id;",
				receiverFirstName, receiverLastName);

		PartDTO[] partsArray = CollectionUtils.toPartDTOArray(parts);

		associateParts(id, partsArray);

		return new ShipmentDTO(id, partsArray, receiverFirstName, receiverLastName);
	}

	@Override
	public ShipmentDTO read(int shipmentId) throws RemoteException {
		return shipmentDb.mapSingle((rs) -> {
			try {
				return createShipment(rs);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}, "SELECT * FROM car_facility_schema.shipments WHERE shipments.id = ?;", shipmentId);
	}

	@Override
	public Collection<ShipmentDTO> readAll() throws RemoteException {
		return shipmentDb.map((rs) -> {
			try {
				return createShipment(rs);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}, "SELECT * FROM car_facility_schema.shipments;");
	}

	@Override
	public boolean update(ShipmentDTO shipmentDto) throws RemoteException {
		// update database
		int rowsAffected = shipmentDb.executeUpdate(
				"UPDATE car_facility_schema.shipments SET receiver_first_name = ?, receiver_last_name = ? WHERE id = ?;",
				shipmentDto.getReceiverFirstName(), shipmentDto.getReceiverLastName(), shipmentDto.getId());

		// update the contents of the shipment
		associateParts(shipmentDto.getId(), shipmentDto.getParts());

		return rowsAffected != 0;
	}

	@Override
	public boolean delete(ShipmentDTO shipmentDto) throws RemoteException {
		// remove all part associations to the shipment
		shipmentDb.executeUpdate("DELETE FROM car_facility_schema.requests WHERE shipments_id = ?;", shipmentDto.getId());

		// remove shipment
		int rowsAffected = shipmentDb.executeUpdate("DELETE FROM car_facility_schema.shipments WHERE id = ?;",
				shipmentDto.getId());

		return rowsAffected != 0;
	}

	private void associateParts(int shipmentId, PartDTO[] allParts) throws RemoteException {
		for (PartDTO part : allParts) {
			shipmentDb.executeUpdate("INSERT INTO car_facility_schema.requests"
					+ " (part_id, shipment_id) SELECT ?, ? WHERE NOT EXISTS(SELECT *"
					+ " FROM car_facility_schema.requests WHERE requests.part_id = "
					+ "? AND requests.shipment_id = ?);", part.getId(), shipmentId, part.getId(), shipmentId);
		}
	}

	private ShipmentDTO createShipment(ResultSet rs) throws SQLException, RemoteException, MalformedURLException {
		int shipmentId = rs.getInt(ShipmentEntityConstants.ID_COLUMN);
		String receiverFirstName = rs.getString(ShipmentEntityConstants.FIRST_NAME_COLUMN);
		String receiverLastName = rs.getString(ShipmentEntityConstants.LAST_NAME_COLUMN);

		try {

			Collection<PartDTO> parts = partDao.read(shipmentId);
			ShipmentDTO shipmentDto = new ShipmentDTO(shipmentId, CollectionUtils.toPartDTOArray(parts), receiverFirstName,
					receiverLastName);

			return shipmentDto;
		} catch (Exception e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

}
