package dto.pallet;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

import dto.part.PartDTO;
import model.pallet.IPallet;
import model.part.IPart;

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

	public PalletDTO(IPallet pallet) throws RemoteException {
		this(pallet.getId(), pallet.getPalletType(), toDTOParts(pallet.getParts()));
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

	@Override
	public String toString() {
		return "PalletDTO [id=" + id + ", palletType=" + palletType + ", parts=" + parts + "]";
	}

}
