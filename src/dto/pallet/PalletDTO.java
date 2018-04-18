package dto.pallet;

import java.io.Serializable;
import java.rmi.RemoteException;
import dto.part.PartDTO;
import remote.model.pallet.IPallet;
import util.Utils;

public class PalletDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final double MAX_PALLET_WEIGHT_KG = 250.0;

	private int id;
	private String palletType;
	private PartDTO[] parts;
	private double weightKg;

	public PalletDTO() {}

	public PalletDTO(int id, String palletType, PartDTO[] parts, double weightKg) {
		setId(id);
		setPalletType(palletType);
		setParts(parts);
		setWeightKg(weightKg);
	}

	public PalletDTO(IPallet remotePallet) throws RemoteException {
		this(remotePallet.getId(), remotePallet.getPalletType(), Utils.toDTOArray(remotePallet.getParts()),
				remotePallet.getWeightKg());
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

	public double getWeightKg() {
		return weightKg;
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
	
	public void setWeightKg(double weightKg) {
		this.weightKg = weightKg;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(
				String.format("PalletDTO [id: %d, palletType: %s, \nParts:\n", id, palletType));

		for (PartDTO part : parts)
			sb.append(part + "\n");

		sb.append(String.format("totalWeight: %d]", getWeightKg()));
		return sb.toString();
	}

}
