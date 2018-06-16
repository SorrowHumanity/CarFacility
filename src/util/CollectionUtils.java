package util;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import dto.car.CarDTO;
import dto.pallet.PalletDTO;
import dto.part.PartDTO;
import dto.shipment.ShipmentDTO;
import remote.model.car.ICar;
import remote.model.pallet.IPallet;
import remote.model.part.IPart;
import remote.model.part.RemotePart;
import remote.model.shipment.IShipment;

public final class CollectionUtils {

	private CollectionUtils() {}

	/**
	 * Converts an array of DTO parts to a list of remote parts
	 **/
	public synchronized static List<IPart> toRemotePartList(PartDTO[] allParts) throws RemoteException {
		int size = allParts.length;
		ArrayList<IPart> remoteParts = new ArrayList<>(size);

		for (PartDTO part : allParts)
			remoteParts.add(new RemotePart(part));

		return remoteParts;
	}

	/**
	 * Converts a list of remote cars to an array of DTO cars
	 **/
	public synchronized static CarDTO[] toCarDTOArray(List<ICar> allCars) throws RemoteException {
		int size = allCars.size();
		CarDTO[] carDtos = new CarDTO[size];

		for (int i = 0; i < size; ++i)
			carDtos[i] = new CarDTO(allCars.get(i));

		return carDtos;
	}

	/**
	 * Converts a list of remote pallets to an array of DTO pallets
	 **/
	public synchronized static PalletDTO[] toPalletDTOArray(List<IPallet> allPallets) throws RemoteException {
		int size = allPallets.size();
		PalletDTO[] palletDtos = new PalletDTO[size];

		for (int i = 0; i < size; ++i)
			palletDtos[i] = new PalletDTO(allPallets.get(i));

		return palletDtos;
	}

	/**
	 * Converts a collection of DTO parts to an array of DTO parts
	 **/
	public synchronized static PartDTO[] toPartDTOArray(Collection<PartDTO> allParts) {
		PartDTO[] array = allParts.toArray(new PartDTO[0]);
		return array;
	}

	/**
	 * Converts a list of remote parts to an array of DTO parts
	 **/
	public synchronized static PartDTO[] toPartDTOArray(List<IPart> allParts) throws RemoteException {
		int size = allParts.size();
		PartDTO[] partDtos = new PartDTO[size];

		for (int i = 0; i < size; ++i)
			partDtos[i] = new PartDTO(allParts.get(i));

		return partDtos;
	}

	/**
	 * Converts a list of remote shipments to an an array of DTO shipments
	 **/
	public synchronized static ShipmentDTO[] toShipmentDTOArray(List<IShipment> allShipments) throws RemoteException {
		int size = allShipments.size();
		ShipmentDTO[] shipmentDtos = new ShipmentDTO[size];

		for (int i = 0; i < size; ++i)
			shipmentDtos[i] = new ShipmentDTO(allShipments.get(i));

		return shipmentDtos;
	}
	
	/**
	 * Converts a list of remote parts to a list of DTO parts 
	 **/
	public synchronized static List<PartDTO> toPartDTOList(List<IPart> parts) throws RemoteException {
		int size = parts.size();
		ArrayList<PartDTO> partDtos = new ArrayList<>(size);
		
		for (IPart part : parts) 
			partDtos.add(new PartDTO(part));
		
		return partDtos;
	}

}
