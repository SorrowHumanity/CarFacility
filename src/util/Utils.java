package util;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import dto.part.PartDTO;
import remote.model.part.IPart;
import remote.model.part.RemotePart;

public class Utils {

	private Utils() {}

	public static List<PartDTO> toDTOPartsList(List<IPart> allRemoteParts) throws RemoteException {
		LinkedList<PartDTO> allDTOParts = new LinkedList<>();

		for (IPart part : allRemoteParts)
			allDTOParts.add(new PartDTO(part));

		return allDTOParts;
	}

	public static List<IPart> toRemotePartsList(List<PartDTO> allDTOparts) throws RemoteException {
		LinkedList<IPart> allRemoteParts = new LinkedList<>();

		for (PartDTO part : allDTOparts)
			allRemoteParts.add(new RemotePart(part));

		return allRemoteParts;
	}

	public static IPart[] toRemotePartsArray(List<PartDTO> partDTOParts) throws RemoteException {
		IPart[] remoteParts = new IPart[partDTOParts.size()];
		
		for (int i = 0; i < remoteParts.length; i++)
			remoteParts[i] = new RemotePart(partDTOParts.get(i));

		return remoteParts;
	}
	
	public static PartDTO[] toDTOPartsArray(List<IPart> remoteParts) throws RemoteException {
		PartDTO[] partDTOs = new PartDTO[remoteParts.size()];
		
		for (int i = 0; i < partDTOs.length; i++) 
			partDTOs[i] = new PartDTO(remoteParts.get(i));
		
		return partDTOs;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T[] toArray(Collection<T> list) {
		T[] array = (T[]) new Object[list.size()];
		list.toArray(array);
		return array;
	}
	
	public static double weightParts(List<PartDTO> allParts) {
		double totalWeight = 0;

		for (PartDTO part : allParts)
			totalWeight += part.getWeightKg();

		return totalWeight;
	}

}
