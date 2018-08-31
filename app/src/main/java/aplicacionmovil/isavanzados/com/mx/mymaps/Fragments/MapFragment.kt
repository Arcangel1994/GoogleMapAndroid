package aplicacionmovil.isavanzados.com.mx.mymaps.Fragments


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context.LOCATION_SERVICE
import android.content.DialogInterface
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import aplicacionmovil.isavanzados.com.mx.mymaps.R
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import java.io.IOException
import java.util.*
                                                        //Marker
//class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerDragListener, View.OnClickListener
class MapFragment : Fragment(), OnMapReadyCallback, View.OnClickListener, LocationListener {

    var mapView: MapView? = null
    var gMap: GoogleMap? = null
    var rootView: View? = null

    var FABAddCity: FloatingActionButton? = null

    var geocoder: Geocoder? = null
    var addresses: List<Address>? = null

    var markerOptions: MarkerOptions? = null

    var locationManager: LocationManager? = null
    var currentLocation: Location? = null

    var marker: Marker? = null

    var zoom: CameraPosition? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_map, container, false)

        FABAddCity = rootView!!.findViewById<FloatingActionButton>(R.id.FABAddCity) as FloatingActionButton

        FABAddCity!!.setOnClickListener(this)

        return rootView
    }

    @SuppressLint("MissingPermission")
    override fun onClick(v: View?) {
//        checkIfGPSIsEnabled()

        if(!isGPSEnabled()){
            showInfAlert()
        }else{
           var location: Location = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)

            if(location == null){
                location = locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            }

            currentLocation = location

            if(currentLocation != null){
                createOrUpdateMarkerByLocation(location)
                zoomToLocation(location)
            }

        }

    }

    fun createOrUpdateMarkerByLocation(location: Location?){
        if(marker == null){
            marker = gMap!!.addMarker(MarkerOptions().position(LatLng(location!!.latitude, location!!.longitude )).draggable(true))
        }else{
            marker!!.position = LatLng(location!!.latitude, location!!.longitude)
        }
    }

    fun showInfAlert(){

        AlertDialog.Builder(context)
                .setTitle("GPS Signal")
                .setMessage("You don't have GPS signal enabled. Would you like to enable the GPS signal now?")
                .setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which ->
                    var intent: Intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)
                })
                .setNegativeButton("CANCEL", null)
                .show()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView = rootView!!.findViewById<MapView>(R.id.map) as MapView

        if(mapView != null){

            mapView!!.onCreate(null)
            mapView!!.onResume()
            mapView!!.getMapAsync(this)

        }

    }

//    fun checkIfGPSIsEnabled(){
//
//        try{
//
//            var gpsSignal: Int = Settings.Secure.getInt(activity!!.contentResolver, Settings.Secure.LOCATION_MODE)
//
//            if(gpsSignal == 0){
//                showInfAlert()
//            }
//
//        }catch (e: Settings.SettingNotFoundException){
//            Toast.makeText(rootView!!.context, e.message.toString(), Toast.LENGTH_LONG).show()
//        }
//
//    }


    fun isGPSEnabled() : Boolean{

        try{

            var gpsSignal: Int = Settings.Secure.getInt(activity!!.contentResolver, Settings.Secure.LOCATION_MODE)

            if(gpsSignal == 0){
//                showInfAlert()
                return false
            }else{
                return true
            }

        }catch (e: Settings.SettingNotFoundException){
            Toast.makeText(rootView!!.context, e.message.toString(), Toast.LENGTH_LONG).show()
            return false
        }

    }


    @SuppressLint("MissingPermission")
    override fun onMapReady(p0: GoogleMap?) {

        gMap = p0
        locationManager = context!!.getSystemService(LOCATION_SERVICE) as LocationManager

        gMap!!.isMyLocationEnabled = true

        //Ocultar un button top derecha
        gMap!!.uiSettings.isMyLocationButtonEnabled = true

        locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0F, this);
        locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0F, this);

//        var place : LatLng = LatLng(21.882252,-102.318094)
//        var placeMarker : LatLng = LatLng(21.881848,-102.318057)
//
//        var zoom: CameraUpdate? = CameraUpdateFactory.zoomTo(16F)
//
//        markerOptions = MarkerOptions()
//        markerOptions!!.position(placeMarker)
//        markerOptions!!.title("Mi marcador")
//        markerOptions!!.draggable(true)
//        markerOptions!!.snippet("Esto es una caja de texto donde modificar los datos")
//        markerOptions!!.icon(BitmapDescriptorFactory.fromResource(android.R.drawable.star_on))
//
//        gMap!!.addMarker(markerOptions)
//        gMap!!.addMarker(MarkerOptions().position(place).title("Working").draggable(true))
//        gMap!!.moveCamera(CameraUpdateFactory.newLatLng(place))
//        gMap!!.animateCamera(zoom)
//
//            //funciones de marker
////        gMap!!.setOnMarkerDragListener(this)
//
//        geocoder = Geocoder(context, Locale.getDefault())


    }

    fun zoomToLocation(location: Location?){
        zoom = CameraPosition.Builder().target(LatLng(location!!.latitude, location!!.longitude)).zoom(15F).bearing(0F).tilt(30F).build()
        gMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(zoom))
    }

     //Funciones de marker
//    override fun onMarkerDragEnd(p0: Marker?) {
//
//        var latitude: Double = p0!!.position.latitude
//        var longitude: Double = p0!!.position.longitude
//
//        //MaxResults 1 a 5
//        try{
//            addresses = geocoder!!.getFromLocation(latitude, longitude, 1)
//        }catch (e: IOException){
//            Toast.makeText(context, e.message.toString(), Toast.LENGTH_LONG).show()
//        }
//
//        var address: String? = addresses!!.get(0).getAddressLine(0)
//        var city: String? = addresses!!.get(0).locality
//        var state: String? = addresses!!.get(0).adminArea
//        var country: String? = addresses!!.get(0).countryName
//        var postalCode: String? = addresses!!.get(0).postalCode
//
//        p0.snippet = "Address: $address, City: $city, State: $state, Country: $country, PostalCode: $postalCode"
//
////        Toast.makeText(context, "Address: $address, City: $city, State: $state, Country: $country, PostalCode: $postalCode ", Toast.LENGTH_LONG).show()
//
//        p0.showInfoWindow()
//
//    }
//
//    override fun onMarkerDragStart(p0: Marker?) {
//        p0!!.hideInfoWindow()
//    }
//
//    override fun onMarkerDrag(p0: Marker?) {
//
//    }

     override fun onLocationChanged(location: Location?) {
         Toast.makeText(context, "Changed! ${location!!.provider} -> ${location!!.latitude} ${location!!.longitude} ", Toast.LENGTH_LONG).show()
//         gMap!!.addMarker(MarkerOptions().position(LatLng(location!!.latitude, location!!.longitude )).draggable(true));

         createOrUpdateMarkerByLocation(location)

     }

     override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
     }

     override fun onProviderEnabled(provider: String?) {
     }

     override fun onProviderDisabled(provider: String?) {
     }


}
