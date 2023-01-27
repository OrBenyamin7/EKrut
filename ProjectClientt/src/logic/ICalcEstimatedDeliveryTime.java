package logic;

/**
 * This interface defines methods for calculating the estimated delivery time for an order.
 */
public interface ICalcEstimatedDeliveryTime {
	 /**
     * Calculates the distance from an operational center in hours.
     * @param Area the area of the order
     * @return the distance from an operational center in hours
     */
	public double distanceFromOperationalCenterHours(String Area); //Calculates the distance from an operational center in hours.
	/**
     * Calculates the availability of a drone in hours.
     * @return the availability of a drone in hours
     */
	public double droneAvaliabilityHours(); //Calculates the availability of a drone in hours.
	/**
     * Calculates the time it takes to load a shipment in hours.
     * @return the time it takes to load a shipment in hours
     */
	public double loadingShippmentTimeHours(); //Calculates the time it takes to load a shipment in hours.
}
