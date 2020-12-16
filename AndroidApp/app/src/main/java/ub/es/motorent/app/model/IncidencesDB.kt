package ub.es.motorent.app.model

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import ub.es.motorent.app.view.MapsActivity

data class IncidencesInfo (
    var id: Int,
    var moto_id: Int?,
    var user_id: Int?,
    var comment: String
)

data class IncidencesJson (
    var incidence: IncidencesInfo
)
data class IncidencesList (
    var incidences : List<IncidencesInfo>
)

object IncidencesDB {

    fun getIncidentById(id: Int, onResult: (IncidencesInfo?) -> Unit) {
        val apiService = RestApiService()
        apiService.getIncidentById(id) {
            Log.i(TAG, it.toString())
            onResult(it?.incidence)
        }
    }


    fun addIncident(moto_id: Int?, user_id: Int?, comment: String,  onResult: (IncidencesInfo?) -> Unit){
        val apiService = RestApiService()
        apiService.addIncident(moto_id, user_id, comment) {
            Log.i(TAG, it.toString())
            onResult(it?.incidence)
        }
    }


    fun deleteIncidentById(id: Int?) {
        val apiService = RestApiService()
        apiService.deleteIncidentById(id) {
            Log.i(TAG, it.toString())
        }
    }


    fun getAllIncidents() {
        val apiService = RestApiService()
        apiService.getAllIncidents() {
            Log.i(TAG, it.toString())
        }
    }


    /*
GET {URL}/incident/{id} per extreure l incident amb l'id que li pasis
POST {URL}/incident?moto_id={motoid}&user_id={userid}&comment={comment} per afegir un incident
DELETE {URL}/incident/{id} per borrar l incident amb l'id que li pasis
GET {URL}/incidents per extreure tots els incidents
 */


    private const val TAG = "Retrofit IncidencesDB"

}