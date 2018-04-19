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

public final class CollectionUtils {

	private CollectionUtils() {}

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
		CarDTO[] carDtos = new CarDTO[size];

		for (int i = 0; i < size; i++)
			carDtos[i] = new CarDTO(allCars.get(i));

		return carDtos;
	}

	public static PalletDTO[] toPalletDTOArray(List<IPallet> allPallets) throws RemoteException {
		int size = allPallets.size();
		PalletDTO[] palletDtos = new PalletDTO[size];

		for (int i = 0; i < size; i++)
			palletDtos[i] = new PalletDTO(allPallets.get(i));

		return palletDtos;
	}

	public static PartDTO[] toPartDTOArray(List<PartDTO> allParts) {
		int size = allParts.size();
		PartDTO[] array = new PartDTO[size];
		allParts.toArray(array);
		return array;
	}

	public static PartDTO[] toDTOArray(List<IPart> allParts) throws RemoteException {
		int size = allParts.size();
		PartDTO[] partDtos = new PartDTO[size];
	
		for (int i = 0; i < size; i++)
			partDtos[i] = new PartDTO(allParts.get(i));
	
		return partDtos;
	}

	public static double weightParts(Collection<PartDTO> allParts) {
		double totalWeight = 0;

		for (PartDTO part : allParts)
			totalWeight += part.getWeightKg();

		return totalWeight;
	}

}
