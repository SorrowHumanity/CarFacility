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
		return trackerDao.readRecords(carChassisNumber);
	}

	@WebMethod
	@Override
	public String trackPart(int partId) throws RemoteException {
		return trackerDao.readRecords(partId);
	}

	@WebMethod
	@Override
	public String trackShipment(Integer shipmentId) throws RemoteException {
		return trackerDao.readRecords(shipmentId);
	}

}
