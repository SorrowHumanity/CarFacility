package service.dismantle_station;

import java.rmi.RemoteException;

import javax.jws.WebMethod;
import javax.jws.WebService;

import dto.part.PartDTO;
import remote.base.dismantle_station.DismantleBaseLocator;
import remote.base.dismantle_station.IDismantleBase;

@WebService
public class DismantleStationService {

	private IDismantleBase dismantleBase;
	
	public DismantleStationService() throws RemoteException {
		dismantleBase = DismantleBaseLocator.lookupBase(DismantleBaseLocator.DISMANTLE_BASE_ID);
	}
	
	@WebMethod
	public PartDTO[] getParts(String carChassisNumber) throws RemoteException {
		return dismantleBase.getParts(carChassisNumber);
	}
	
}
