package ub.es.motorent.app.model

import android.util.Log


data class MapCoordInfo (
    var from_latitude: Float,
    var from_longitude: Float,
    var to_latitude: Float,
    var to_longitude: Float
)

data class MapCoordJson (
    var mapcoord: MapCoordInfo
)
data class MapCoordList (
    var mapcoords : List<MapCoordInfo>
)

object MapCoordDB {

    fun getAllMapCoords() {
        val apiService = RestApiService()
        apiService.getAllMapCoords() {
            Log.i(TAG, it.toString())
        }
    }

    fun getMapCoordsByPair(from_longitude: Float, from_latitude: Float) {
        val apiService = RestApiService()
        apiService.getMapCoordsByPair(from_longitude,from_latitude) {
            Log.i(TAG, it.toString())
        }
    }

    fun addMapCoords(from_longitude: Float, from_latitude: Float, to_longitude: Float, to_latitude: Float){
        val apiService = RestApiService()
        apiService.addMapCoords(from_longitude, from_latitude, to_longitude, to_latitude) {
            Log.i(TAG, it.toString())
        }
    }

    fun updateMapCoordsByOrigin(from_longitude: Float, from_latitude: Float, to_longitude: Float?, to_latitude: Float?){
        val apiService = RestApiService()
        apiService.updateMapCoordsByOrigin(from_longitude, from_latitude, to_longitude, to_latitude) {
            Log.i(MapCoordDB.TAG, it.toString())
        }
    }

    fun deleteMapCoordByOrigin(from_longitude: Float, from_latitude: Float) {
        val apiService = RestApiService()
        apiService.deleteMapCoordByOrigin(from_longitude, from_latitude) {
            Log.i(TAG, it.toString())
        }
    }

    private const val TAG = "Retrofit MapCoordDB"

}