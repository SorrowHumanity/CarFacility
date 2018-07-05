package remote.domain.pallet;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Objects;

import dto.pallet.PalletDTO;
import remote.domain.part.IPart;
import util.CollectionUtils;

public class RemotePallet extends UnicastRemoteObject implements IPallet {

	private static final long serialVersionUID = 1L;

	public static final double MAX_PALLET_WEIGHT_CAPACITY = 1000.0;
	
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
		this(palletDto.getId(), palletDto.getPalletType(), CollectionUtils.toRemotePartList(palletDto.getParts()),
				palletDto.getWeightKg());
	}

	@Override
	public boolean addPart(IPart part) throws RemoteException {
		if (part == null) return false;
		
		// update weight
		weightKg += part.getWeightKg();
		
		// add part
		return parts.add(part);
	}

	@Override
	public boolean removePart(IPart part) throws RemoteException {
		if (part == null) return false;
		
		// update weight
		weightKg -= part.getWeightKg();
		
		// find & remove part
		int size = parts.size();
		for (int i = 0; i < size; ++i)
			if (parts.get(i).getId() == part.getId())
				return parts.remove(i) != null;

		return false;
	}

	@Override
	public boolean fits(IPart part) throws RemoteException {
		return typeEquals(part) && hasEnoughVolume(part);
	}

	@Override
	public boolean containsPart(IPart part) throws RemoteException {
		for (IPart p : parts)
			if (p.getId() == part.getId())
				return true;

		return false;
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

	/**
	 * Checks if a part's type is the same as the type of the pallet
	 * 
	 * @param part
	 *            the part
	 * @return true, if the types match. Otherwise, false
	 * @throws RemoteException
	 **/
	private boolean typeEquals(IPart part) throws RemoteException {
		boolean typeEquals = Objects.equals(part.getType(), getPalletType());
		return typeEquals;
	}

	/**
	 * Checks if the pallet has enough remaining volume to fit a part
	 * 
	 * @param part
	 *            the part
	 * @return true, if the pallet has enough volume to fit the part. Otherwise,
	 *         false
	 * @throws RemoteException
	 **/
	private boolean hasEnoughVolume(IPart part) throws RemoteException {
		double combinedWeight = part.getWeightKg() + getWeightKg(); // combined weight = part weight + pallet weight
		boolean hasEnoughVolume = Double.compare(combinedWeight, MAX_PALLET_WEIGHT_CAPACITY) <= 0;
		return hasEnoughVolume;
	}

}
