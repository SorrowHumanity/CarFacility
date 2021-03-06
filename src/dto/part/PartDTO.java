package dto.part;

import java.io.Serializable;
import java.rmi.RemoteException;

import remote.domain.part.IPart;

public class PartDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private String name;
	private double weightKg;

	// no-arg constructor for deserialization (XML to Object)
	public PartDTO() {}

	// used when reading objects from the database
	public PartDTO(int id, String name, double weightKg) {
		setId(id);
		setName(name);
		setWeightKg(weightKg);
	}
	
	public PartDTO(IPart remotePart) throws RemoteException {
		this(remotePart.getId(),remotePart.getName(), remotePart.getWeightKg());
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public double getWeightKg() {
		return weightKg;
	}
	
	public String getType() {
		// The type of the part is always the last word of the name of each part.
		// Part names follow the following convention: {Model} + " " + {Part type}
		// Example: Subaru Engine
		return name.substring(name.lastIndexOf(" ") + 1); 	
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		assert name != null;
		this.name = name;
	}

	public void setWeightKg(double weightKg) {
		this.weightKg = weightKg;
	}

	@Override
	public String toString() {
		return "PartDTO [id=" + id + ", name=" + name + ", weightKg=" + weightKg + "]";
	}

}
