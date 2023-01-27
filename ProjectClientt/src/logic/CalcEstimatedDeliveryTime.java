package logic;
import java.time.LocalDateTime;
/**
 * This class calculates the estimated delivery time based on the distance from operational center, drone availability, and loading/shipping time.
 * @author Your Name
 */
public class CalcEstimatedDeliveryTime implements ICalcEstimatedDeliveryTime{
    /**
     * This method calculates the distance from the operational center in hours.
     * @param Area The area of the delivery location
     * @return The distance from the operational center in hours as a double
     */
	@Override
	public double distanceFromOperationalCenterHours(String Area) {
		return 3.5;
	}
    /**
     * This method calculates the drone availability in hours.
     * @return The drone availability in hours as a double
     */
	@Override
	public double droneAvaliabilityHours() {
		return 20;
	}
    /**
     * This method calculates the loading and shipping time in hours.
     * @return The loading and shipping time in hours as a double
     */
	@Override
	public double loadingShippmentTimeHours() {
		return 3;
	}

    /**
     * This method calculates the estimated delivery time based on the distance from operational center, drone availability, and loading/shipping time.
     * @param Area The area of the delivery location
     * @return The estimated delivery time as a LocalDateTime object
     */
	public LocalDateTime calcEstimatedDeliveryTime(String Area) {
		LocalDateTime EstimatedDeliveryDate = LocalDateTime.now(); 
		double totalHoursForShipppemet = distanceFromOperationalCenterHours(Area) + droneAvaliabilityHours() + loadingShippmentTimeHours() ;
		EstimatedDeliveryDate = EstimatedDeliveryDate.plusHours((long) totalHoursForShipppemet);
		return EstimatedDeliveryDate;
	}
	
}
