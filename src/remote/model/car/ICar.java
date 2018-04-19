package remote.model.car;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import remote.model.part.IPart;

public interface ICar extends Remote {

	/**
	 * @return the chassis number value
	 * @throws RemoteException
	 **/
	String getChassisNumber() throws RemoteException;

	/**
	 * @return the model value
	 * @throws RemoteException
	 **/
	String getModel() throws RemoteException;

	/**
	 * @return a list of all belonging parts
	 * @throws RemoteException
	 **/
	List<IPart> getParts() throws RemoteException;

	/**
	 * @return the weight value in kilograms
	 * @throws RemoteException
	 **/
	double getWeightKg() throws RemoteException;

}
