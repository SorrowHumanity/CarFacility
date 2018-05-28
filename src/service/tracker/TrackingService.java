package service.tracker;

import java.rmi.RemoteException;

import javax.jws.WebMethod;
import javax.jws.WebService;

import tracker.dao.ITrackerDAO;
import tracker.dao.TrackerDAO;

@WebService
public class TrackingService implements ITrackingService {

	private ITrackerDAO trackerDao;

	public TrackingService() throws RemoteException {
		trackerDao = TrackerDAO.getInstance();
	}

	@WebMethod
	@Override
	public String trackCar(String carChassisNumber) throws RemoteException {
		String records = trackerDao.readRecords(carChassisNumber); 
		return records;
	}

	@WebMethod
	@Override
	public String trackShipment(int shipmentId) throws RemoteException {
		String records = trackerDao.readRecords(shipmentId); 
		return records;
	}

}
