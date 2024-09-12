package com.example.uberclone

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.bonuspack.location.GeocoderNominatim

class MapActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(this, applicationContext.getSharedPreferences("osmdroid", MODE_PRIVATE))
        setContentView(R.layout.activity_map)

        // Проверяем разрешение на геолокацию
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            initializeMap()
        }

        // Настраиваем кнопку вызова такси
        val callTaxiButton: Button = findViewById(R.id.call_taxi_button)
        callTaxiButton.setOnClickListener {
            Toast.makeText(this, "Такси вызвано!", Toast.LENGTH_SHORT).show()
        }

        // Настраиваем кнопку "Найти меня"
        val findMeButton: Button = findViewById(R.id.find_me_button)
        findMeButton.setOnClickListener {
            findUserLocation()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initializeMap()
        }
    }

    private fun initializeMap() {
        mapView = findViewById(R.id.mapView)
        mapView.setTileSource(TileSourceFactory.MAPNIK)

        // Устанавливаем точку фокуса (Москва по умолчанию)
        val moscow = GeoPoint(55.7558, 37.6173)
        mapView.controller.setZoom(16.0)
        mapView.controller.setCenter(moscow)

        // Добавляем маркер на карту
        val marker = Marker(mapView)
        marker.position = moscow
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = "Москва"
        mapView.overlays.add(marker)
    }

    private fun findUserLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Используем Nominatim для поиска местоположения пользователя
            CoroutineScope(Dispatchers.IO).launch {
                val geocoder = GeocoderNominatim("UberClone/1.0")
                try {
                    val addresses = geocoder.getFromLocationName("Москва", 1)
                    if (addresses.isNotEmpty()) {
                        val location = addresses[0]
                        val userLocation = GeoPoint(location.latitude, location.longitude)
                        runOnUiThread {
                            mapView.controller.setCenter(userLocation)

                            // Добавляем маркер на карту
                            val marker = Marker(mapView)
                            marker.position = userLocation
                            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                            marker.title = "Ваше местоположение"
                            mapView.overlays.clear()
                            mapView.overlays.add(marker)
                        }
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        Toast.makeText(this@MapActivity, "Ошибка получения местоположения", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }
}
