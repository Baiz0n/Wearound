package com.nothing.timing.wearound.bloc.data.extras.results;

import com.nothing.timing.wearound.bloc.data.ClassData.StaticData;
import com.nothing.timing.wearound.tools.ApiHelper;
;

public class ResultChecker {

    public String getIsOpenResult(NearbyResult.ResultsBean.OpeningHoursBean openingHoursBean1,
                                  PlaceIdResult.ResultBean.OpeningHoursBean openingHoursBean2) {

        String result;
        boolean isOpen;

        if (openingHoursBean1 != null) {

            isOpen = openingHoursBean1.isOpen_now();

        } else if (openingHoursBean2 != null) {

            isOpen = openingHoursBean2.isOpen_now();

        } else {

            return "";
        }

        if (isOpen) {

            result = StaticData.OPEN;

        } else {

            result = StaticData.CLOSED;
        }

        return result;

    }

    public String getImgUrlResult(NearbyResult.ResultsBean result, String key) {

        String url;

        if ( result.getPhotos() != null ) {

            url = ApiHelper.getImageUrl(result.getPhotos().get(0).getPhoto_reference(), key);

        } else {

            url = result.getIcon();
        }

        return url;
    }

}
