package com.example.deeplinkapplication.deeplink

import java.io.Serializable

interface DeeSegment : Serializable {

    val id: String
    val nextSegments: List<DeeSegment>
        get() = listOf()

}
