package remote.pallet;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;

import dto.pallet.PalletDTO;
import dto.part.PartDTO;
import remote.part.IPart;
import remote.part.RemotePart;

public class RemotePallet extends UnicastRemoteObject implements IPallet {

	private static final long serialVersionUID = 1L;

	private int id;
	private String palletType;
	private List<IPart> parts;

	public RemotePallet(int id, String palletType, List<IPart> parts) throws RemoteException {
		this.id = id;
		this.palletType = palletType;
		this.parts = parts;
	}

	public RemotePallet(PalletDTO palletDTO) throws RemoteException {
		this(palletDTO.getId(), palletDTO.getPalletType(), 
				toRemoteParts(palletDTO.getParts()));
	}

	private static List<IPart> toRemoteParts(List<PartDTO> partDTOs) throws RemoteException {
		List<IPart> remoteParts = new LinkedList<>();
		for (PartDTO part : partDTOs)
			remoteParts.add(new RemotePart(part));
		return remoteParts;
	}

	@Override
	public int getId() throws RemoteException {
		return id;
	}

	@Override
	public String getPalletType() throws RemoteException {
		return palletType;
	}

	@Override
	public List<IPart> getParts() throws RemoteException {
		return parts;
	}

	@Override
	public String toString() {
		return "RemotePallet [id=" + id + ", palletType=" + palletType + ", parts=" + parts + "]";
	}

}
