package de.gruschtelapps.fh_maa_refuelpair.utils.constants;

/**
 * Create by Eric Werner
 * Edit by Alea Sauer: Values added and adjusted
 */
public interface ConstUrl {
    // Tankerkönig
    String URL_TANKERKOENIG = "https://creativecommons.tankerkoenig.de";
    String URL_TANKERKOENIG_ACTION_RADIUS_SEARCH = "/json/list.php";
    String URL_TANKERKOENIG_ACTION_DETAIL_SEARCH = "/json/detail.php";
    String URL_TANKERKOENIG_SEARCH_RADIUS = "?lat=%1$f&lng=%2$f&rad=%3$f&sort=%4$s&type=%5$s&apikey=%6$s";
    String URL_TANKERKOENIG_SEARCH_PRICE = "?lat=%1$f&lng=%2$f&rad=%3$f&sort=%4$s&type=%5$s&apikey=%6$s";
}
