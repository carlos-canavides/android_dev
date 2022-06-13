package com.example.androidapp.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.androidapp.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class HomeFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener {

    private lateinit var map: GoogleMap
    private var currentLocation: Location? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private lateinit var tvLatitude: TextView
    private lateinit var tvLongitude: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(com.example.androidapp.R.layout.fragment_home, container, false)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        tvLatitude = view.findViewById(R.id.tv_latitude)
        tvLongitude = view.findViewById(R.id.tv_longitude)
        fetchLocation()
        return view
    }

    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(requireActivity(),Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_LOCATION)
            return
        }
        val task = fusedLocationProviderClient!!.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null){
                currentLocation = location
                val supportMapFragment = (childFragmentManager.findFragmentById(R.id.fragment_map) as SupportMapFragment?)
                supportMapFragment!!.getMapAsync(this)
            }
        }
    }

    private fun isPermissionsGranted() = ContextCompat.checkSelfPermission(
        requireActivity(),
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        if(currentLocation!=null) {
            val coordenadas: LatLng = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
            val markerOptions = MarkerOptions().position(coordenadas).title("You are here!")
            map.addMarker(markerOptions)
            map.animateCamera(CameraUpdateFactory.newLatLng(coordenadas))
            map.animateCamera(
                CameraUpdateFactory.newLatLngZoom(coordenadas, 17f),
                3000,
                null
            )
            tvLatitude.setText("Latitude: "+currentLocation!!.latitude.toString())
            tvLongitude.setText("Longitude: "+currentLocation!!.longitude.toString())
        }
        map.setOnMyLocationButtonClickListener(this)
    }

    companion object {
        const val REQUEST_CODE_LOCATION = 0
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            REQUEST_CODE_LOCATION -> if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                map.isMyLocationEnabled = true
                fetchLocation()
            }else{
                Toast.makeText(requireContext(), "Para activar la localización ve a ajustes y acepta los permisos", Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume()
        if (!::map.isInitialized) return
        if(!isPermissionsGranted()){
            map.isMyLocationEnabled = false
            Toast.makeText(requireContext(), "Para activar la localización ve a ajustes y acepta los permisos", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        return false
    }
}