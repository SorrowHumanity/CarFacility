package remote.model.pallet;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.List;

import dto.pallet.PalletDTO;
import remote.model.part.IPart;
import util.Utils;

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
				Utils.toRemotePartsList(Arrays.asList(palletDTO.getParts())));
	}

	@Override
	public boolean addPart(IPart part) throws RemoteException {
		return parts.add(part);
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
	public double getTotalWeightKg() throws RemoteException {
		double totalWeight = 0;

		for (IPart part : parts)
			totalWeight += part.getWeightKg();

		return totalWeight;
	}

	@Override
	public String toString() {
		return "RemotePallet [id=" + id + ", palletType=" + palletType + ", parts=" + parts + "]";
	}

	@Override
	public boolean palletFits(IPart part) throws RemoteException {
		return partTypeEquals(part) && hasEnoughVolume(part);
	}

	private boolean partTypeEquals(IPart part) throws RemoteException {
		return part.getName().endsWith(getPalletType());
	}

	private boolean hasEnoughVolume(IPart part) throws RemoteException {
		double combinedWeight = Double.sum(part.getWeightKg(), getTotalWeightKg());
		return Double.compare(combinedWeight, PalletDTO.MAX_PALLET_WEIGHT_KG) <= 0;
	}

}
