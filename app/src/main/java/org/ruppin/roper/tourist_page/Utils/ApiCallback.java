package org.ruppin.roper.tourist_page.Utils;



import org.ruppin.roper.tourist_page.Models.Business;

import java.util.List;

public interface ApiCallback{

    void onGetItem(List<Business> businessList);

    void onError(Throwable t);
}
