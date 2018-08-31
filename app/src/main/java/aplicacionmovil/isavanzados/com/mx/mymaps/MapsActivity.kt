package aplicacionmovil.isavanzados.com.mx.mymaps

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setMinZoomPreference(15F)
        mMap.setMaxZoomPreference(20F)

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney").draggable(true))

        //Max zoom 21
        //Max Bearing rotacion de flor de viente 360°
        //Max Tilt efecto 3d 90
        var camara: CameraPosition = CameraPosition.builder().target(sydney).zoom(18F).bearing(90F).tilt(45F).build()

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camara))

        mMap.setOnMapClickListener(object : GoogleMap.OnMapClickListener{
            override fun onMapClick(p0: LatLng?) {
                Toast.makeText(this@MapsActivity, "Click on: ${p0!!.latitude} ${p0!!.longitude}", Toast.LENGTH_LONG).show()
            }
        })

        mMap.setOnMapLongClickListener(object : GoogleMap.OnMapLongClickListener{
            override fun onMapLongClick(p0: LatLng?) {
                Toast.makeText(this@MapsActivity, "Click on: ${p0!!.latitude} ${p0!!.longitude}", Toast.LENGTH_LONG).show()
            }
        })

        mMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener{
            override fun onMarkerDragEnd(p0: Marker?) {
                Toast.makeText(this@MapsActivity, "${p0!!.position.longitude} ${p0!!.position.latitude}", Toast.LENGTH_LONG).show()
            }

            override fun onMarkerDragStart(p0: Marker?) {
            }

            override fun onMarkerDrag(p0: Marker?) {
            }
        })

//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}
