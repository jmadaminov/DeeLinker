package com.example.deeplinkapplication

import android.net.Uri
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.deeplinkapplication.deeplink.MainDirections
import com.example.deeplinkapplication.deeplink.myHosts
import dev.jmadaminov.deelinker.DeeNode
import dev.jmadaminov.deelinker.buildDeeLinker
import dev.jmadaminov.deelinker.deeConfig
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
        "domain://cabinet",
        "domain://home",
        "domain://cabinet/orders",
        "domain://cabinet/orders/123",
        "domain://cabinet/orders/all",
//        "domain://cabinet/home",
    )


    fun testLinks(): Boolean {
        deeplinks.forEach {
            if (!testUriConsumable(Uri.parse(it))) return false/*throw Exception("URI NOT CONSUMABLE! PLEASE ADD DEEPLINK LOGIC FOR $it")*/
        }
        return true
    }

    fun testUriConsumable(uri: Uri): Boolean {
        buildDeeLinker<MainDirections>(
            deeplinkUri = uri,
            config = deeConfig { hosts = myHosts },
            onSuccess = { node ->
                return traverseSegments(MainDirections.values().map { it }, node)
            },
            onFail = { return false }
        )
        return false
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
