package dto.pallet;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;

import dto.part.PartDTO;
import remote.model.pallet.IPallet;
import util.Utils;

public class PalletDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final double MAX_PALLET_WEIGHT_KG = 250;

	private int id;
	private String palletType;
	private PartDTO[] parts;

	public PalletDTO() {}

	public PalletDTO(int id, String palletType, PartDTO[] parts) {
		setId(id);
		setPalletType(palletType);
		setParts(parts);
	}

	public PalletDTO(IPallet remotePallet) throws RemoteException {
		this(remotePallet.getId(), remotePallet.getPalletType(), Utils.toDTOPartsArray(remotePallet.getParts()));
	}

	public int getId() {
		return id;
	}

	public String getPalletType() {
		return palletType;
	}

	public PartDTO[] getParts() {
		return parts;
	}

	public double getTotalWeightKg() {
		double totalWeight = 0;

		for (PartDTO part : parts)
			totalWeight += part.getWeightKg();

		return totalWeight;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setPalletType(String palletType) {
		this.palletType = palletType;
	}

	public void setParts(PartDTO[] parts) {
		this.parts = parts;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(
				String.format("PalletDTO [id: %d, palletType: %s, \nParts:\n", id, palletType));

		for (PartDTO partDTO : parts)
			sb.append(partDTO + "\n");

		sb.append(String.format("totalWeight: %d]", getTotalWeightKg()));
		return sb.toString();
	}

}
