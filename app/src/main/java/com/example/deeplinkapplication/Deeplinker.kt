package com.example.deeplinkapplication

import com.example.deeplinkapplication.deeplink.DeeSegment

interface Deeplinker<T : DeeSegment> {

    fun onDeeplinking(node: T)

}
