package com.ecomap.ukraine.activities;

import android.content.Context;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.models.Problem;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

/**
 * The default view for a ClusterManager. Markers are animated in and out of clusters.
 */
public class IconRenderer extends DefaultClusterRenderer<Problem> {


    /**
     * Constructor
     * @param context Appliccation context
     * @param map google map
     * @param clusterManager cluster manager
     */
    public IconRenderer(Context context, GoogleMap map,
                           ClusterManager<Problem> clusterManager) {
        super(context, map, clusterManager);
    }

    /**
     * Called before the marker for a ClusterItem is added to the map.
     */
    @Override
    protected void onBeforeClusterItemRendered(Problem problem, MarkerOptions markerOptions) {
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(this.getResourceIdForMarker(
                problem.getProblemTypesId()));
        markerOptions.icon(icon)
                .title(problem.getTitle())
                .anchor(0.5f,1f);
        super.onBeforeClusterItemRendered(problem, markerOptions);
    }

    /**
     * Gets markers resourses
     * @param typeId id of type
     * @return icon resourse
     */
    private int getResourceIdForMarker (int typeId) {
        int resId = 0;
        switch (typeId) {
            case 1:
                resId = R.drawable.type1;
                break;
            case 2:
                resId = R.drawable.type2;
                break;
            case 3:
                resId = R.drawable.type3;
                break;
            case 4:
                resId = R.drawable.type4;
                break;
            case 5:
                resId = R.drawable.type5;
                break;
            case 6:
                resId = R.drawable.type6;
                break;
            case 7:
                resId = R.drawable.type7;
                break;
        }
        return resId;
    }
    @Override
    protected String getClusterText(int bucket) {
            return String.valueOf(bucket);
    }

    /**
     * Gets the "bucket" for a particular cluster. By default, uses the number of points within the
     * cluster, bucketed to some set points.
     */
    @Override
    protected int getBucket(Cluster<Problem> cluster) {
        int size = cluster.getSize();
            return size;

    }

}
