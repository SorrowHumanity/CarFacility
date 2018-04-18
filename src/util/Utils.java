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

public class Utils {

	private Utils() {}

	public static List<IPart> toRemotePartsList(List<PartDTO> allDTOparts) throws RemoteException {
		LinkedList<IPart> allRemoteParts = new LinkedList<>();

		for (PartDTO part : allDTOparts)
			allRemoteParts.add(new RemotePart(part));

		return allRemoteParts;
	}

	public static IPart[] toRemotePartsArray(List<PartDTO> partDTOs) throws RemoteException {
		int size = partDTOs.size();
		IPart[] remoteParts = new IPart[size];
		
		for (int i = 0; i < size; i++)
			remoteParts[i] = new RemotePart(partDTOs.get(i));

		return remoteParts;
	}
	
	public static List<PartDTO> toDTOPartsList(List<IPart> allRemoteParts) throws RemoteException {
		LinkedList<PartDTO> allDTOParts = new LinkedList<>();
	
		for (IPart part : allRemoteParts)
			allDTOParts.add(new PartDTO(part));
	
		return allDTOParts;
	}

	public static PartDTO[] toDTOPartsArray(List<IPart> remoteParts) throws RemoteException {
		int size = remoteParts.size();
		PartDTO[] partDTOs = new PartDTO[size];
		
		for (int i = 0; i < size; i++) 
			partDTOs[i] = new PartDTO(remoteParts.get(i));
		
		return partDTOs;
	}
	
	public static CarDTO[] toCarDTOArray(List<ICar> remoteCars) throws RemoteException {
		int size = remoteCars.size();
		CarDTO[] carDTOs = new CarDTO[size];
		
		for (int i = 0; i < size; i++)
			carDTOs[i] = new CarDTO(remoteCars.get(i));
		
		return carDTOs;
	}
	
	public static PalletDTO[] toPalletDTOArray(List<IPallet> remotePallets) throws RemoteException {
		int size = remotePallets.size();
		PalletDTO[] palletDTOs = new PalletDTO[size];
		
		for (int i = 0; i < size; i++)
			palletDTOs[i] = new PalletDTO(remotePallets.get(i));
		
		return palletDTOs;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T[] toArray(Collection<T> list) {
		T[] array = (T[]) new Object[list.size()];
		list.toArray(array);
		return array;
	}
	
	public static double weightParts(Collection<PartDTO> allParts) {
		double totalWeight = 0;

		for (PartDTO part : allParts)
			totalWeight += part.getWeightKg();

		return totalWeight;
	}

}
