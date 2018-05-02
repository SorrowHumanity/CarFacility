package service.tracker;

import java.rmi.RemoteException;

public interface ITrackingService {

	String trackCar(String carChassisNumber) throws RemoteException;

	String trackShipment(int shipmentId) throws RemoteException;

}
