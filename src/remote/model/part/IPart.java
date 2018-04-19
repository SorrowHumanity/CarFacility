package remote.model.part;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IPart extends Remote {

	/**
	 * @return the id value
	 * @throws RemoteException
	 **/
	int getId() throws RemoteException;

	/**
	 * @return the car chassis number value
	 * @throws RemoteException
	 **/
	String getCarChassisNumber() throws RemoteException;

	/**
	 * @return the name value
	 * @throws RemoteException
	 **/
	String getName() throws RemoteException;

	/**
	 * @return the weight value in kilograms
	 * @throws RemoteException
	 **/
	double getWeightKg() throws RemoteException;

	/**
	 * @return the type value
	 * @throws RemoteException
	 **/
	String getType() throws RemoteException;

}
