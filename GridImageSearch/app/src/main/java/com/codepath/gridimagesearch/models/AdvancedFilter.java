package com.codepath.gridimagesearch.models;

import java.io.Serializable;

/**
 * Created by qingdi on 9/19/14.
 */
public class AdvancedFilter implements Serializable {

    private static final long serialVersionUID = -277051178062400190L;

    public String   imageSize;
    public String   colorFilter;
    public String   imageType;
    public String   siteFilter;

    public String getFilterString() {
        StringBuilder strBlder = new StringBuilder();
        if (imageSize != null && !imageSize.isEmpty() && !"any".equals(imageSize)) {
            strBlder.append("&imgsz=").append(imageSize);
        }
        if (colorFilter != null && !colorFilter.isEmpty() && !"any".equals(colorFilter)) {
            strBlder.append("&imgcolor=").append(colorFilter);
        }
        if (imageType != null && !imageType.isEmpty() && !"any".equals(imageType)) {
            strBlder.append("&imgtype=").append(imageType);
        }
        if (siteFilter != null && !siteFilter.isEmpty() && !"any".equals(siteFilter)) {
            strBlder.append("&as_sitesearch=").append(siteFilter);
        }

        return strBlder.toString();
    }
}
