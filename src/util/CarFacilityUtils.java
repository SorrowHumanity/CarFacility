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
		LinkedList<PartDTO> allDTOparts = new LinkedList<>();

		for (IPart part : allRemoteParts)
			allDTOparts.add(new PartDTO(part));

		return allDTOparts;
	}
	
	public static List<IPart> toRemoteParts(List<PartDTO> allDTOparts) throws RemoteException {
		List<IPart> allRemoteParts = new LinkedList<>();
		
		for (PartDTO part : allDTOparts)
			allRemoteParts.add(new RemotePart(part));
		
		return allRemoteParts;
	}
	
}
