package com.example.androidapp.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.androidapp.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private var currentLocation: Location? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var locationRequest: com.google.android.gms.location.LocationRequest? = null
    private var locationCallback: LocationCallback? = null
    private var lastMarker: Marker? = null
    private lateinit var tvLatitude: TextView
    private lateinit var tvLongitude: TextView
    private lateinit var buttonGoogleMaps: Button
    private lateinit var buttonGetLocation: FloatingActionButton
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                if(it.key==Manifest.permission.ACCESS_FINE_LOCATION) {
                    if(it.value) {
                        getLastLocation()
                    }
                    else {
                        Toast.makeText(requireActivity(), "Ve a ajustes y acepta los permisos", Toast.LENGTH_SHORT)
                    }
                }
            }
        }
    companion object {
        val TAG: String = HomeFragment::class.java.simpleName
        var PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        createMapFragment()
        tvLatitude = view.findViewById(R.id.tv_latitude)
        tvLongitude = view.findViewById(R.id.tv_longitude)
        buttonGetLocation = view.findViewById(R.id.btn_get_location)
        buttonGetLocation.setOnClickListener {
            if (hasPermissions(context!!, PERMISSIONS)) {
                getLastLocation()
            } else {
                requestPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION));
            }
        }
        buttonGoogleMaps = view.findViewById(R.id.btn_google_maps)
        buttonGoogleMaps.setOnClickListener {
            if(currentLocation!=null) {
                val gmmIntentUri = Uri.parse("geo:"+currentLocation!!.latitude.toString()+","+currentLocation!!.longitude.toString()+"?z=18")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }
            else {
                Toast.makeText(requireContext(), "Debe activar la ubicación para obtener al menos una coordenada", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

    private fun getLastLocation() {
        locationRequest = com.google.android.gms.location.LocationRequest.create()
        locationRequest!!.setInterval(1000)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                currentLocation = p0.lastLocation
                setUpMap()
            }
        }
        fusedLocationProviderClient!!.requestLocationUpdates(locationRequest!!, locationCallback!!, Looper.getMainLooper())
    }

    private fun createMapFragment() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.fragment_map) as SupportMapFragment
        mapFragment.getMapAsync(this@HomeFragment)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
    }

    private fun setUpMap() {
        val coordenadas: LatLng = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
        val markerOptions = MarkerOptions().position(coordenadas).title("You are here!")
        lastMarker?.remove()
        lastMarker = map.addMarker(markerOptions)!!
        map.animateCamera(CameraUpdateFactory.newLatLng(coordenadas))
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(coordenadas, 17f),
            1000,
            null
        )
        tvLatitude.setText("Latitude: "+currentLocation!!.latitude.toString())
        tvLongitude.setText("Longitude: "+currentLocation!!.longitude.toString())
    }
}