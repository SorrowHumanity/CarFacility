package dto.shipment;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Arrays;

import dto.part.PartDTO;
import remote.domain.shipment.IShipment;
import util.CollectionUtils;

public class ShipmentDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private PartDTO[] parts;
	private String receiverFirstName, receiverLastName;

	public ShipmentDTO() {}

	public ShipmentDTO(int id, PartDTO[] parts, String receiverFirstName, String receiverLastName) {
		setId(id);
		setParts(parts);
		setReceiverFirstName(receiverFirstName);
		setReceiverLastName(receiverLastName);
	}

	public ShipmentDTO(IShipment remoteShipment) throws RemoteException {
		this(remoteShipment.getId(), CollectionUtils.toPartDTOArray(remoteShipment.getShippedParts()),
				remoteShipment.getReceiverFirstName(), remoteShipment.getReceiverLastName());
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public PartDTO[] getParts() {
		return parts;
	}

	public void setParts(PartDTO[] parts) {
		assert parts != null;
		this.parts = parts;
	}

	public String getReceiverFirstName() {
		return receiverFirstName;
	}

	public void setReceiverFirstName(String receiverFirstName) {
		assert receiverFirstName != null;
		this.receiverFirstName = receiverFirstName;
	}

	public String getReceiverLastName() {
		return receiverLastName;
	}

	public void setReceiverLastName(String receiverLastName) {
		assert receiverLastName != null;
		this.receiverLastName = receiverLastName;
	}

	@Override
	public String toString() {
		return "ShipmentDTO [id=" + id + ", parts=" + Arrays.toString(parts) + ", receiverFirstName="
				+ receiverFirstName + ", receiverLastName=" + receiverLastName + "]";
	}

}
