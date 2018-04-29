package tracker.dao;

import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import persistence.DatabaseHelper;
import remote.dao.car.CarEntityConstants;
import remote.dao.part.PartEntityConstants;
import remote.dao.shipment.ShipmentEntityConstants;

public class TrackerDAO implements ITrackerDAO {

	private static ITrackerDAO instance = null;

	private DatabaseHelper<String> carFacilityDb;

	private TrackerDAO() throws RemoteException {
		carFacilityDb = new DatabaseHelper<>(
				DatabaseHelper.CAR_FACILITY_DB_URL,
				DatabaseHelper.POSTGRES_USERNAME,
				DatabaseHelper.POSTGRES_PASSWORD);
	}

	/**
	 * Singleton implementation
	 * 
	 * @return an instance of a tracker data access object
	 * @throws RemoteException
	 **/
	public static ITrackerDAO getInstance() throws RemoteException {
		if (instance == null)
			instance = new TrackerDAO();
		return instance;
	}

	@Override
	public String readRecords(String carChassisNumber) throws RemoteException {
		List<String> recordList = carFacilityDb.map((rs) -> createRecord(rs), "SELECT"
				+ " car_facility_schema.requests.part_id, car_facility_schema.cars.model,"
				+ " car_facility_schema.requests.pallet_id, car_facility_schema.parts.name,"
				+ " car_facility_schema.shipments.id AS shipment_id, car_facility_schema.shipments.receiver_first_name,"
				+ " car_facility_schema.shipments.receiver_last_name FROM"
				+ " car_facility_schema.contains, car_facility_schema.parts, car_facility_schema.requests,"
				+ " car_facility_schema.shipments, car_facility_schema.cars"
				+ " WHERE requests.part_id = parts.id AND requests.shipment_id = shipments.id AND shipments.id = requests.shipment_id"
				+ " AND cars.chassis_number = parts.car_chassis_number AND cars.chassis_number = ?"
				+ " GROUP BY requests.shipment_id, requests.part_id, cars.model, requests.pallet_id,"
				+ " parts.name, shipments.id, shipments.receiver_first_name, shipments.receiver_last_name;",
				carChassisNumber);
		String allRecords = String.format("Tracking by chassis number %s:\n[%d parts found]\n%s", carChassisNumber,
				recordList.size(), String.join("\n", recordList));
		return allRecords;

	}

	@Override
	public String readRecords(Integer shipmentId) throws RemoteException {
		List<String> recordList = carFacilityDb.map((rs) -> createRecord(rs),
				"SELECT (SELECT car_facility_schema.cars.model AS model"
						+ " FROM car_facility_schema.cars, car_facility_schema.parts"
						+ " WHERE cars.chassis_number = parts.car_chassis_number LIMIT 1),"
						+ " car_facility_schema.requests.part_id, (SELECT car_facility_schema.parts.name"
						+ " FROM car_facility_schema.parts WHERE car_facility_schema.requests.part_id = parts.id),"
						+ " car_facility_schema.requests.pallet_id, car_facility_schema.requests.shipment_id,"
						+ " car_facility_schema.shipments.receiver_first_name,"
						+ " car_facility_schema.shipments.receiver_last_name"
						+ " FROM car_facility_schema.requests, car_facility_schema.shipments,"
						+ " car_facility_schema.parts WHERE shipment_id = ? AND shipment_id = shipments.id"
						+ " GROUP BY requests.part_id, requests.shipment_id,"
						+ " requests.pallet_id, shipments.receiver_first_name, shipments.receiver_last_name;",
				shipmentId);
		String allRecords = String.format("Tracking by shipment id %s:\n[%d parts found]\n%s", shipmentId,
				recordList.size(), String.join("\n", recordList));

		return allRecords;
	}

	/**
	 * Creates a record from a database result set
	 * 
	 * @param rs
	 *            the result set
	 * @return a record
	 * @throws SQLException
	 **/
	private String createRecord(ResultSet rs) throws SQLException {
		String carModel = rs.getString(CarEntityConstants.MODEL_COLUMN);
		String partId = rs.getString("part_id");
		String partName = rs.getString(PartEntityConstants.NAME_COLUMN);
		String palletId = rs.getString("pallet_id");
		String shipmentId = rs.getString("shipment_id");
		String recieverFirstName = rs.getString(ShipmentEntityConstants.FIRST_NAME_COLUMN);
		String recieverLastName = rs.getString(ShipmentEntityConstants.LAST_NAME_COLUMN);

		String record = String.format(
				"[From car with model: %s], [Part \"%s\" with id: %s], [Was stored in pallet with id: %s],"
						+ " [Shipped in shipment with id: %s], [Shipped to: %s %s]",
				carModel, partName, partId, palletId, shipmentId, recieverFirstName, recieverLastName);

		return record;
	}

}
