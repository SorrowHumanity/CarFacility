package service.tracker;

import java.rmi.RemoteException;

public interface ITrackingService {

	String trackCar(String carChassisNumber) throws RemoteException;

	String trackShipment(Integer shipmentId) throws RemoteException;

}
