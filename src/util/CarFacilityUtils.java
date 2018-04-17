package util;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

import dto.part.PartDTO;
import remote.model.part.IPart;
import remote.model.part.RemotePart;

public class CarFacilityUtils {

	private CarFacilityUtils() {}
	
	public static List<PartDTO> toDTOParts(List<IPart> allRemoteParts) throws RemoteException {
		LinkedList<PartDTO> allDTOParts = new LinkedList<>();

		for (IPart part : allRemoteParts)
			allDTOParts.add(new PartDTO(part));

		return allDTOParts;
	}
	
	public static List<IPart> toRemoteParts(List<PartDTO> allDTOparts) throws RemoteException {
		LinkedList<IPart> allRemoteParts = new LinkedList<>();
		
		for (PartDTO part : allDTOparts)
			allRemoteParts.add(new RemotePart(part));
		
		return allRemoteParts;
	}
	
}
