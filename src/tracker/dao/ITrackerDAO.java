package tracker.dao;

import java.rmi.RemoteException;

public interface ITrackerDAO {

	/**
	 * Reads all database records of the parts, shipments and pallets related to the
	 * car with the chassis number passed as a parameter
	 * 
	 * @param carChassisNumber
	 *            the chassis number
	 * @return a string of records
	 * @throws RemoteException
	 **/
	String readRecords(String carChassisNumber) throws RemoteException;

	/**
	 * Reads all database records of the parts, shipments and pallets related to the
	 * shipment with the shipment id passed as a parameter
	 * 
	 * @param shipmentId
	 *            the id of the shipment
	 * @return a string of records
	 * @throws RemoteException
	 **/
	String readRecords(int shipmentId) throws RemoteException;

}
