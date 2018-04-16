package dto.pallet;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

import dto.part.PartDTO;
import remote.model.pallet.IPallet;
import remote.model.part.IPart;

public class PalletDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String palletType;
	private List<PartDTO> parts;

	public PalletDTO(int id, String palletType, List<PartDTO> parts) {
		this.id = id;
		this.palletType = palletType;
		this.parts = parts;
	}

	public PalletDTO(IPallet remotePallet) throws RemoteException {
		this(remotePallet.getId(), remotePallet.getPalletType(), toDTOParts(remotePallet.getParts()));
	}

	private static List<PartDTO> toDTOParts(List<IPart> remoteParts) throws RemoteException {
		List<PartDTO> dtoParts = new LinkedList<>();

		for (IPart part : remoteParts)
			dtoParts.add(new PartDTO(part));

		return dtoParts;
	}

	public int getId() {
		return id;
	}

	public String getPalletType() {
		return palletType;
	}

	public List<PartDTO> getParts() {
		return parts;
	}

	public double getTotalWeight() {
		double totalWeight = 0;

		for (PartDTO part : parts)
			totalWeight += part.getWeightKg();

		return totalWeight;
	}

	@Override
	public String toString() {
		return "PalletDTO [id=" + id + ", palletType=" + palletType + ", parts=" + parts + "]";
	}

}
