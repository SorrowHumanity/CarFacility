package remote.dao.pallet;

public final class PalletEntityConstants {

	private PalletEntityConstants() {}

	public static final String ID_COLUMN = "id";
	public static final String PALLET_TYPE_COLUMN = "pallet_type";
	public static final String TOTAL_WEIGHT_KG_COLUMN = "total_weight_kg";
	
	public static final class PalletTypes {
		
		private PalletTypes() {}
		
		public static final String ENGINE_PART_TYPE = "Engine";
		public static final String WINDSHIELD_PART_TYPE = "Windshield";
		public static final String TIRES_PART_TYPE = "Tires";
		public static final String CAR_DOOR_PART_TYPE = "Car Door";
		public static final String CAR_HOOD_PART_TYPE = "Car Hood";
		public static final String BRAKE_SYSTEM_PART_TYPE = "Brake System";
	}
	
}
