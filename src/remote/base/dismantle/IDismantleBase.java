package remote.base.dismantle;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import remote.model.car.ICar;
import remote.model.pallet.IPallet;
import remote.model.part.IPart;

public interface IDismantleBase extends Remote {

	/**
	 * Dismantles a car and registers each separate part in the system
	 * 
	 * @param car
	 *            the car to be dismantled
	 * @return a list of parts
	 * @throws RemoteException
	 **/
	List<IPart> dismantleCar(ICar car) throws RemoteException;

	/**
	 * Registers a part in the system
	 * 
	 * @param carChassisNumber
	 *            the chassis number of the part
	 * @param name
	 *            the name of the part
	 * @param weightKg
	 *            the weight of the part in kilograms
	 * @return the newly registered part
	 * @throws RemoteException
	 **/
	IPart registerPart(String carChassisNumber, String name, double weightKg) throws RemoteException;

	/**
	 * Retrieves a list of part that belong to the car with the chassis number
	 * passed as a parameter
	 * 
	 * @param carChassisNumber
	 *            the chassis number of the car
	 * @return a lit of the parts
	 * @throws RemoteException
	 **/
	List<IPart> getParts(String carChassisNumber) throws RemoteException;

	/**
	 * Retrieves a list of parts that belong to the pallet with the pallet id passed
	 * as a parameter
	 * 
	 * @param palletId
	 *            the id of the pallet
	 * @return a list of parts
	 * @throws RemoteException
	 **/
	List<IPart> getParts(int palletId) throws RemoteException;

	/**
	 * Retrieves a list of all registered parts in the system
	 * 
	 * @return a list of parts
	 * @throws RemoteException
	 **/
	List<IPart> getAllParts() throws RemoteException;

	/**
	 * Registers a new pallet in the system
	 * 
	 * @param palletType
	 *            the types of parts to be stored in the pallet
	 * @param parts
	 *            a list of parts to be stored in the pallet
	 * @return the newly registered pallet
	 * @throws RemoteException
	 **/
	IPallet registerPallet(String palletType, List<IPart> parts) throws RemoteException;

	/**
	 * Retrieves a pallet with the pallet id passed as a parameter
	 * 
	 * @param the
	 *            id of the pallet
	 * @return the pallet
	 * @throws RemoteException
	 **/
	IPallet getPallet(int palletId) throws RemoteException;

	/**
	 * Retrieves a list of all pallets registered in the system
	 * 
	 * @return a list of pallets
	 * @throws RemoteException
	 **/
	List<IPallet> getAllPallets() throws RemoteException;

	/**
	 * Removes a part from a pallet and returns the id of the pallet from which the
	 * part was removed
	 * 
	 * @param part
	 *            the part
	 * @return the pallet id from which the part was removed
	 * @throws RemoteException
	 **/
	int removeFromPallet(IPart part) throws RemoteException;

}
