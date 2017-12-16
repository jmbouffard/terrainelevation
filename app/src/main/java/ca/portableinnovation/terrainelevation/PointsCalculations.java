package ca.portableinnovation.terrainelevation;

public class PointsCalculations {

    /**
     * Function that calculates a destination point based on an origin, an azimuth and a distance
     *  The algorithm are taken from the BPR-3 section C.4
     * @param lat Latitude of the starting point
     * @param lon Longitude of the starting point
     * @param azimuth_deg Direction towards new point
     * @param dist_m Distance from new point
     * @return Latitude of new point
     */
    public static double getNewLat (double lat, double lon, double azimuth_deg, double dist_m) {
        double m_per_deg_lat = 1000.0 * (111.108 - (0.566 * Math.cos(2 * lat * Math.PI / 180)));
        double deviation_lat = Math.cos(azimuth_deg * Math.PI / 180) * dist_m / m_per_deg_lat;
        return lat + deviation_lat;
    }

    /**
     * Function that calculates a destination point based on an origin, an azimuth and a distance
     *  The algorithm are taken from the BPR-3 section C.4
     * @param lat Latitude of the starting point
     * @param lon Longitude of the starting point
     * @param azimuth_deg Direction towards new point
     * @param dist_m Distance from new point
     * @return Longitude of new point
     */
    public static double getNewLon (double lat, double lon, double azimuth_deg, double dist_m) {
        double m_per_deg_lon = 1000.0 * ((111.391 * Math.cos(lat * Math.PI / 180)) - (0.95 * Math.cos(3 * lat * Math.PI / 180)));
        double deviation_lon = Math.sin(azimuth_deg * Math.PI / 180) * dist_m / m_per_deg_lon;
        return lon - deviation_lon;
    }

    public static double GetDoubleFromDDMMSS(int deg, int min, int sec) {
        double value = deg + (min/60.0) + (sec/3600.0);
        return value;
    }

}
