package remote.model.pallet;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.List;

import dto.pallet.PalletDTO;
import remote.base.dismantle_station.DismantleBaseLocator;
import remote.base.dismantle_station.IDismantleBase;
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

	public RemotePallet(PalletDTO palletDTO) throws RemoteException {
		this(palletDTO.getId(), palletDTO.getPalletType(), Utils.toRemotePartsList(Arrays.asList(palletDTO.getParts())),
				palletDTO.getWeightKg());
	}

	@Override
	public boolean addPart(IPart part) throws RemoteException {
		weightKg = Double.sum(weightKg, part.getWeightKg());
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
	public double getWeightKg() throws RemoteException {
		return weightKg;
	}

	@Override
	public String toString() {
		return "RemotePallet [id=" + id + ", palletType=" + palletType + ", parts=" + parts + "]";
	}

	@Override
	public boolean palletFits(IPart part) throws RemoteException {
		System.out.println("PaletTypeEquals: " + partTypeEquals(part));
		System.out.println("Enough volume: " + hasEnoughVolume(part));
		return partTypeEquals(part) && hasEnoughVolume(part);
	}

	private boolean partTypeEquals(IPart part) throws RemoteException {
		System.out.println(part.getType() + " ?= " + getPalletType());
		return part.getName().endsWith(getPalletType());
	}

	private boolean hasEnoughVolume(IPart part) throws RemoteException {
		System.out.println("Part weight: " + part.getWeightKg());
		System.out.println("Palelt weight: " + getWeightKg());
		double combinedWeight = Double.sum(part.getWeightKg(), getWeightKg());
		System.out.println("Combined weight: " + combinedWeight);
		return Double.compare(combinedWeight, PalletDTO.MAX_PALLET_WEIGHT_KG) <= 0;
	}

	public static void main(String[] args) throws RemoteException {
		IPallet p = DismantleBaseLocator.lookupBase(DismantleBaseLocator.DISMANTLE_BASE_ID).getPallet(2);
		System.out.println(p.getPalletType());
		IDismantleBase disBase = DismantleBaseLocator.lookupBase(DismantleBaseLocator.DISMANTLE_BASE_ID);
		List<IPart> list = disBase.getAllParts();
		if (p.palletFits(list.get(5))) {
			System.out.println(list.get(5).getWeightKg());
			boolean r = p.addPart(list.get(5));
			System.out.println("Yes: " + p.getWeightKg());
		} else {
			System.out.println("NO");
		}
	}

}
