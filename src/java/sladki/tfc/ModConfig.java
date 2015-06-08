package sladki.tfc;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ModConfig {
	
	public static boolean isDebugging;
	public static float coolantConsumptionMultiplier;
	public static int cellarTemperature;
	public static int iceHouseTemperature;

	public static void loadConfig(FMLPreInitializationEvent event) {
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		
		config.load();
		
		isDebugging = config.get(Configuration.CATEGORY_GENERAL, "Debug", false).getBoolean(false);
		cellarTemperature = config.get(Configuration.CATEGORY_GENERAL, "TemperatureCellar", 5).getInt(5);
		iceHouseTemperature = config.get(Configuration.CATEGORY_GENERAL, "TemperatureIceHouse", 1).getInt(1);
		
		Property coolantConsumptionMultiplierProperty = config.get(Configuration.CATEGORY_GENERAL, "CoolantConsumptionMultiplier", 100);
		coolantConsumptionMultiplierProperty.comment = "The multiplier 100 is 1.0, 123 is 1.23";
		coolantConsumptionMultiplier = (float) (0.01 * coolantConsumptionMultiplierProperty.getInt());
		
		config.save();
	}
	
}