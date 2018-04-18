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
	private double weightKg;

	public RemotePallet(int id, String palletType, List<IPart> parts, double weightKg) throws RemoteException {
		this.id = id;
		this.palletType = palletType;
		this.parts = parts;
		this.weightKg = weightKg;
	}

	public RemotePallet(PalletDTO palletDto) throws RemoteException {
		this(palletDto.getId(), palletDto.getPalletType(), Utils.toRemotePartsList(Arrays.asList(palletDto.getParts())),
				palletDto.getWeightKg());
	}

	@Override
	public boolean addPart(IPart part) throws RemoteException {
		weightKg += part.getWeightKg();
		return parts.add(part);
	}

	@Override
	public boolean fits(IPart part) throws RemoteException {
		return typeEquals(part) && hasEnoughVolume(part);
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
	public double getWeightKg() throws RemoteException {
		return weightKg;
	}

	@Override
	public String toString() {
		return "RemotePallet [id=" + id + ", palletType=" + palletType + ", parts=" + parts + "]";
	}

	private boolean typeEquals(IPart part) throws RemoteException {
		boolean typeEquals = part.getName().endsWith(getPalletType()); 
		return typeEquals;
	}

	private boolean hasEnoughVolume(IPart part) throws RemoteException {
		double combinedWeight = Double.sum(part.getWeightKg(), getWeightKg());
		boolean hasEnoughVolume = Double.compare(combinedWeight, PalletDTO.MAX_PALLET_WEIGHT_KG) <= 0; 
		return hasEnoughVolume;
	}

}
