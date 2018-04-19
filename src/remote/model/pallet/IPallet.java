package remote.model.pallet;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import remote.model.part.IPart;

public interface IPallet extends Remote {

	/**
	 * @return the id value
	 * @throws RemoteException
	 **/
	int getId() throws RemoteException;

	/**
	 * @return the pallet type value
	 * @throws RemoteException
	 **/
	String getPalletType() throws RemoteException;

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

	/**
	 * Adds a part to the pallet and updates the pallet weight value
	 * 
	 * @param part
	 *            the part to be added
	 * @return true, if the addition is successful. Otherwise, false
	 * @throws RemoteException
	 **/
	boolean addPart(IPart part) throws RemoteException;

	/**
	 * Checks if a part is can be added to the pallet according to two constraints:
	 * weight and part type
	 * 
	 * @param part
	 *            the part
	 * @return true, if the part fits the pallet. Otherwise, false
	 * @throws RemoteException
	 **/
	boolean fits(IPart part) throws RemoteException;

}
