package com.example.corte1_app.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import org.osmdroid.views.overlay.infowindow.InfoWindow

@Composable
fun MapLearningScreen(navController: NavController) {
    val context = LocalContext.current
    val activity = context as? ComponentActivity
    val mapView = remember { MapView(context) }
    val locationOverlay by rememberUpdatedState(newValue = MyLocationNewOverlay(GpsMyLocationProvider(context), mapView))
    val hasLocationPermission = remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    LaunchedEffect(Unit) {
        Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE))
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)
        mapView.controller.setZoom(5.0)
        mapView.controller.setCenter(GeoPoint(48.8583, 2.2944))

        val landmarks = listOf(
            Triple("Eiffel Tower", GeoPoint(48.8583, 2.2944), "Learn about iron lattice structures and Eiffel's contribution to civil engineering."),
            Triple("Golden Gate Bridge", GeoPoint(37.8199, -122.4783), "Explore suspension bridge design and its impact on transportation systems."),
            Triple("Burj Khalifa", GeoPoint(25.1972, 55.2744), "Understand the tallest skyscraper and innovations in vertical load engineering."),
            Triple("Big Ben", GeoPoint(51.5007, -0.1246), "Dive into historical architecture and mechanical clockwork engineering."),
            Triple("Empire State Building", GeoPoint(40.7484, -73.9857), "Learn steel frame construction and Art Deco architectural elements."),
            Triple("Sydney Opera House", GeoPoint(-33.8568, 151.2153), "Study acoustic design and sail-shaped precast concrete roofs."),
            Triple("CN Tower", GeoPoint(43.6426, -79.3871), "Explore tower design for broadcasting and observation."),
            Triple("Tokyo Tower", GeoPoint(35.6586, 139.7454), "Review structural symmetry and communication use."),
            Triple("Petronas Towers", GeoPoint(3.1579, 101.7116), "Learn about twin towers and Islamic geometric influence in engineering."),
            Triple("Shanghai Tower", GeoPoint(31.2336, 121.5055), "Explore vertical city designs and environmental engineering concepts."),
            Triple("Colosseum", GeoPoint(41.8902, 12.4922), "Roman concrete engineering and amphitheater structures."),
            Triple("Taj Mahal", GeoPoint(27.1751, 78.0421), "Marble craftsmanship, domes and symmetry in Mughal architecture."),
            Triple("Statue of Liberty", GeoPoint(40.6892, -74.0445), "Internal structure by Eiffel and copper skin engineering."),
            Triple("Christ the Redeemer", GeoPoint(-22.9519, -43.2105), "Learn about soapstone material and mountain-based construction."),
            Triple("Great Wall of China", GeoPoint(40.4319, 116.5704), "Explore ancient defense structures and mass labor coordination."),
            Triple("Pyramid of Giza", GeoPoint(29.9792, 31.1342), "Review early geometric calculations and heavy stone transportation."),
            Triple("Mount Fuji", GeoPoint(35.3606, 138.7274), "Understand volcanic monitoring systems and nature tourism impact."),
            Triple("Cali - Cristo Rey", GeoPoint(3.4264, -76.5890), "Local example of monument construction and reinforced concrete use."),
            Triple("Berlin TV Tower", GeoPoint(52.5208, 13.4095), "Study telecommunications towers and urban planning."),
            Triple("Space Needle", GeoPoint(47.6205, -122.3493), "Explore futuristic design and earthquake resistance features.")
        )

        landmarks.forEach { (title, geoPoint, lesson) ->
            val marker = Marker(mapView)
            marker.position = geoPoint
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker.title = title
            marker.snippet = lesson
            marker.subDescription = "Tap to learn"
            // Usamos el InfoWindow estándar de OSMDroid en lugar del bonuspack
            mapView.overlays.add(marker)
        }

        if (hasLocationPermission.value) {
            locationOverlay.enableMyLocation()
            mapView.overlays.add(locationOverlay)
        }
    }

    LaunchedEffect(hasLocationPermission.value) {
        if (hasLocationPermission.value) {
            locationOverlay.enableMyLocation()
            if (!mapView.overlays.contains(locationOverlay)) {
                mapView.overlays.add(locationOverlay)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { mapView },
            modifier = Modifier.fillMaxSize(),
            update = { it.invalidate() }
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.End
        ) {
            IconButton(
                onClick = {
                    if (hasLocationPermission.value) {
                        locationOverlay.enableMyLocation()
                        locationOverlay.myLocation?.let {
                            mapView.controller.animateTo(it)
                            mapView.controller.setZoom(15.0)
                        } ?: Toast.makeText(context, "Waiting for location...", Toast.LENGTH_SHORT).show()
                    } else {
                        activity?.let {
                            ActivityCompat.requestPermissions(
                                it,
                                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                1001
                            )
                        }
                    }
                },
                modifier = Modifier.background(Color.White, CircleShape)
            ) {
                Text("📍")
            }

            IconButton(
                onClick = {
                    val zoom = mapView.zoomLevelDouble
                    mapView.controller.setZoom(zoom + 1.0)
                },
                modifier = Modifier.background(Color.White, CircleShape)
            ) {
                Text("➕")
            }

            IconButton(
                onClick = {
                    val zoom = mapView.zoomLevelDouble
                    mapView.controller.setZoom(zoom - 1.0)
                },
                modifier = Modifier.background(Color.White, CircleShape)
            ) {
                Text("➖")
            }
        }
    }

    DisposableEffect(Unit) { onDispose { mapView.onDetach() } }

    LaunchedEffect(context) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            hasLocationPermission.value = true
        }
    }
}