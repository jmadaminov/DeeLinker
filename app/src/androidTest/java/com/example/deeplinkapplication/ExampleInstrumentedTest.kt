package com.example.deeplinkapplication

import android.net.Uri
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.deeplinkapplication.deeplink.MainDirections
import com.example.deeplinkapplication.deeplink.myHosts
import dev.jmadaminov.deelinker.DeeNode
import dev.jmadaminov.deelinker.buildDeeLinker
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.deeplinkapplication", appContext.packageName)
    }


    @Test
    fun checkDeeplinks() {
        assert(testLinks())
    }


    private val deeplinks = listOf(
        "uzum://cabinet",
        "uzum://home",
        "uzum://cabinet/orders",
        "uzum://cabinet/orders/123",
        "uzum://cabinet/orders/all",
//        "uzum://cabinet/home",
    )


    fun testLinks(): Boolean {
        deeplinks.forEach {
            if (!testUriConsumable(Uri.parse(it))) return false/*throw Exception("URI NOT CONSUMABLE! PLEASE ADD DEEPLINK LOGIC FOR $it")*/
        }
        return true
    }

    fun testUriConsumable(uri: Uri): Boolean {
        val deeplinkStartingSegment = buildDeeLinker(
            uri,
            hosts = myHosts,
            rootNodes = object : DeeNode {
                override var host: String = ""
                override var segment: String = ""
                override var nextNode: DeeNode? = null
                override val childNodes = mutableListOf<DeeNode>(*MainDirections.values())

            },
        )
        return traverseSegments(MainDirections.values().map { it }, deeplinkStartingSegment)
    }

    fun traverseSegments(actual: List<DeeNode>, deeNode: DeeNode?): Boolean {
        if (deeNode == null) return false
        actual.forEach {
            if (it.segment == deeNode.segment) {
                return if (deeNode.nextNode == null) true
                else traverseSegments(it.childNodes, deeNode.nextNode)
            }
        }
        Log.e(
            "DeeLinker",
            "Trying to consume unknown node ==> ${deeNode.segment} <== which does not exist in *** $actual ***. Make sure you have registered the node in DeeSegmentTree.kt"
        )
        return false
    }


}