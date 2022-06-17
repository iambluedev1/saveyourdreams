package fr.saveyourdreams.app.ui.main;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import fr.saveyourdreams.app.R;
import fr.saveyourdreams.app.databinding.FragmentMainBinding;
import fr.saveyourdreams.app.repositories.FakeDb;

public class MapFragment extends Fragment {

    private FragmentMainBinding binding;

    private GoogleMap mMap;
    private View mCustomMarkerView;
    private ImageView mMarkerImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentMainBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mCustomMarkerView = ((LayoutInflater) inflater).inflate(R.layout.view_custom_marker, null);
        mMarkerImageView = (ImageView) mCustomMarkerView.findViewById(R.id.profile_image);

        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        SupportMapFragment mapFragment = new SupportMapFragment();
        fragmentTransaction.replace(root.getId(), mapFragment);
        fragmentTransaction.commit();

        mapFragment.getMapAsync(googleMap -> {
            mMap = googleMap;
            mMap.getUiSettings().setZoomControlsEnabled(true);
            if (ActivityCompat.checkSelfPermission(inflater.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(inflater.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d("SAVE_YOUR_DREAMS", "pas la perms");
            } else {
                mMap.setMyLocationEnabled(true);
            }
            mMap.getUiSettings().setMapToolbarEnabled(true);
            addCustomMarker(getContext());
            mMap.setOnMarkerClickListener(marker -> {
                Log.d("SAVE_YOUR_DREAMS", marker.getSnippet());
                return false;
            });
        });

        return root;
    }

    private void addCustomMarker(Context context) {
        Log.d("SAVE_YOUR_DREAMS", "addCustomMarker()");
        if (mMap == null) {
            return;
        }

        Log.d("SAVE_YOUR_DREAMS", FakeDb.markers.size() + "");

        FakeDb.markers.forEach((marker) -> {
            LatLng latLng = new LatLng(marker.getLat(), marker.getLng());
            Log.d("SAVE_YOUR_DREAMS", "Loading marker" + marker.toString());
            Glide
                    .with(context)
                    .asBitmap()
                    .fitCenter()
                    .load(marker.getImages().iterator().next())
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            mMap.addMarker(new MarkerOptions()
                                    .snippet(marker.getId().toString())
                                    .position(latLng)
                                    .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(mCustomMarkerView, resource))));
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });
        });
    }

    private Bitmap getMarkerBitmapFromView(View view, Bitmap bitmap) {
        mMarkerImageView.setImageBitmap(bitmap);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = view.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        view.draw(canvas);
        return returnedBitmap;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        mMap = null;
    }
}
