package com.example.tea1

import java.io.Serializable

class ItemToDo : Serializable {
    var description: String = ""
    var fait: Boolean = false

    constructor()

    constructor(description: String) {
        this.description = description
        this.fait = false
    }

    constructor(description: String, fait: Boolean) {
        this.description = description
        this.fait = fait
    }

    override fun toString(): String {
        return "ItemToDo(description='$description', fait=$fait)"
    }
} 