package service.tracker;

import java.rmi.RemoteException;

public interface ITrackingService {

	String trackCar(String carChassisNumber) throws RemoteException;

	String trackPart(int partId) throws RemoteException;
	
	String trackShipment(Integer shipmentId) throws RemoteException;
	
}
