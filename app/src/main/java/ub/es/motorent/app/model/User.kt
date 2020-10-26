package ub.es.motorent.app.model

import java.io.Serializable

const val USER_NAME = "userName"

class User : Serializable {
    var userName: String? = null

    constructor() {
    }

    /**
     * Metodo constructor de cada usuario del juego
     * @param String
     */
    constructor(
        userName: String
    ) {
        this.userName = userName
    }
}