package com.ecomap.ukraine.map;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.model.Problem;
import com.ecomap.ukraine.model.ProblemType;
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
     * Icons for each bucket.
     */
    private SparseArray<BitmapDescriptor> icons = new SparseArray<>();
    private Activity activity;

    /**
     * If cluster size is less than this size, display individual markers.
     */
    private static final int MIN_CLUSTER_SIZE = 2;

    /**
     * Constructor
     *
     * @param activity       which callback manager
     * @param map            google map
     * @param clusterManager managed clusters
     */
    public IconRenderer(final Activity activity, final GoogleMap map,
                        ClusterManager<Problem> clusterManager) {
        super(activity.getApplicationContext(), map, clusterManager);
        this.activity = activity;
    }

    /**
     * Create Bitmap from view
     *
     * @param view view, which we want turn
     * @return generated bitmap
     */
    public static Bitmap createDrawableFromView(final View view) {
        view.setDrawingCacheEnabled(true);
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        return bitmap;
    }

    /**
     * Called before the marker for a ClusterItem is added to the map.
     */
    @Override
    protected void onBeforeClusterItemRendered(Problem problem, MarkerOptions markerOptions) {
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(IconRenderer.getResourceIdForMarker(
                problem.getProblemType()));
        markerOptions.icon(icon)
                .anchor(0.5f, 1f);
        super.onBeforeClusterItemRendered(problem, markerOptions);
    }

    /**
     * Get cluster text
     * @param bucket number of markers in cluster
     */
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

    /**
     * Determine whether the cluster should be rendered as individual markers or a cluster.
     */
    @Override
    protected boolean shouldRenderAsCluster(Cluster<Problem> cluster) {
        return cluster.getSize() > MIN_CLUSTER_SIZE;
    }

    /**
     * Called before the marker for a Cluster is added to the map.
     * The default implementation draws a circle with a rough count of the number of items.
     */
    protected void onBeforeClusterRendered(final Cluster<Problem> cluster,
                                           final MarkerOptions markerOptions) {
        int bucket = getBucket(cluster);
        BitmapDescriptor descriptor = icons.get(bucket);
        if (descriptor == null) {
            View clusterView = activity.getLayoutInflater().inflate(
                    R.layout.cluster_marker_view, null);
            ((TextView) clusterView.findViewById(R.id.marker_count))
                    .setText(getClusterText(bucket));
            clusterView.setBackgroundResource(getResourceIdForCluster(bucket));
            descriptor = BitmapDescriptorFactory.fromBitmap(createDrawableFromView(clusterView));
            icons.put(bucket, descriptor);
        }

        markerOptions.icon(descriptor);

    }

    /**
     * Gets markers resourses
     *
     * @param problemType id of type
     * @return icon resourse
     */
    public static int getResourceIdForMarker(final ProblemType problemType) {
        int resId = 0;
        switch (problemType) {
            case FOREST_DESTRUCTION:
                resId = R.drawable.type1;
                break;
            case RUBBISH_DUMP:
                resId = R.drawable.type2;
                break;
            case ILLEGAL_BUILDING:
                resId = R.drawable.type3;
                break;
            case WATER_POLLUTION:
                resId = R.drawable.type4;
                break;
            case THREAD_TO_BIODIVERSITY:
                resId = R.drawable.type5;
                break;
            case POACHING:
                resId = R.drawable.type6;
                break;
            case OTHER:
                resId = R.drawable.type7;
                break;
        }
        return resId;
    }

    /**
     * Gets markers resources
     *
     * @param size Numbers of markers in cluster
     * @return icon resource
     */
    private int getResourceIdForCluster(final int size) {
        int resId = 0;
        if (size < 10) {
            resId = R.drawable.m1;
        } else if (size < 50) {
            resId = R.drawable.m2;
        } else if (size < 100) {
            resId = R.drawable.m3;
        } else if (size < 500) {
            resId = R.drawable.m4;
        } else if (size < Integer.MAX_VALUE) {
            resId = R.drawable.m5;
        }
        return resId;
    }

}
