package util;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import dto.car.CarDTO;
import dto.pallet.PalletDTO;
import dto.part.PartDTO;
import remote.model.car.ICar;
import remote.model.pallet.IPallet;
import remote.model.part.IPart;
import remote.model.part.RemotePart;

public final class Utils {

	private Utils() {}

	public static List<IPart> toRemotePartsList(List<PartDTO> allParts) throws RemoteException {
		LinkedList<IPart> allRemoteParts = new LinkedList<>();

		for (PartDTO part : allParts)
			allRemoteParts.add(new RemotePart(part));

		return allRemoteParts;
	}

	public static List<IPart> toRemotePartsList(PartDTO[] allParts) throws RemoteException {
		LinkedList<IPart> remoteParts = new LinkedList<>();
	
		for (PartDTO part : allParts)
			remoteParts.add(new RemotePart(part));
	
		return remoteParts;
	
	}

	public static CarDTO[] toCarDTOArray(List<ICar> allCars) throws RemoteException {
		int size = allCars.size();
		CarDTO[] carDTOs = new CarDTO[size];

		for (int i = 0; i < size; i++)
			carDTOs[i] = new CarDTO(allCars.get(i));

		return carDTOs;
	}

	public static PalletDTO[] toPalletDTOArray(List<IPallet> allPallets) throws RemoteException {
		int size = allPallets.size();
		PalletDTO[] palletDTOs = new PalletDTO[size];

		for (int i = 0; i < size; i++)
			palletDTOs[i] = new PalletDTO(allPallets.get(i));

		return palletDTOs;
	}

	public static PartDTO[] toPartDTOArray(List<PartDTO> allParts) {
		PartDTO[] array = new PartDTO[allParts.size()];
		allParts.toArray(array);
		return array;
	}

	public static PartDTO[] toDTOArray(List<IPart> allParts) throws RemoteException {
		int size = allParts.size();
		PartDTO[] partDTOs = new PartDTO[size];
	
		for (int i = 0; i < size; i++)
			partDTOs[i] = new PartDTO(allParts.get(i));
	
		return partDTOs;
	}

	public static double weightParts(Collection<PartDTO> allParts) {
		double totalWeight = 0;

		for (PartDTO part : allParts)
			totalWeight += part.getWeightKg();

		return totalWeight;
	}

}
