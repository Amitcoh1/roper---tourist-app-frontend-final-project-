package org.ruppin.roper.tourist_page.Utils;




import org.ruppin.roper.tourist_page.Models.GooglePlace;

import java.util.List;

public interface GoogleCallback {

    void onGetPlaces(List<GooglePlace> placesList);

    void onError(Throwable t);
}
