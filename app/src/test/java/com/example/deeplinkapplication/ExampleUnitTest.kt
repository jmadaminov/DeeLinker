package com.example.deeplinkapplication

import android.net.Uri
import com.example.deeplinkapplication.deeplink.DeeNode
import com.example.deeplinkapplication.deeplink.RootDirections
import com.example.deeplinkapplication.deeplink.buildDeeLinker
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
//    @Test
//    fun checkDeeplinks() {
//
//    }
//
//
//    private val deeplinks = listOf(
//        "uzum://cabinet",
//        "uzum://home",
//        "uzum://cabinet/orders",
//        "uzum://cabinet/orders/123",
//        "uzum://cabinet/orders/all",
////    "uzum://cabinet/home",
//    )
//
//
//    fun testLinks(uri:Uri): Boolean {
//        deeplinks.forEach {
//            if (!testUriConsumable(Uri.parse(it))) return false/*throw Exception("URI NOT CONSUMABLE! PLEASE ADD DEEPLINK LOGIC FOR $it")*/
//        }
//        return true
//    }
//
//    fun testUriConsumable(uri: Uri): Boolean {
//        val deeplinkStartingSegment = buildDeeLinker(uri)
//        return traverseSegments(RootDirections.values().map { it }, deeplinkStartingSegment)
//    }
//
//    fun traverseSegments(actual: List<DeeNode>, deeNode: DeeNode?): Boolean {
//        if (deeNode == null) return false
//        actual.forEach {
//            if (it.segment == deeNode.segment) {
//                return if (deeNode.nextNode == null) true
//                else traverseSegments(it.possibleDirections, deeNode.nextNode)
//            }
//        }
//        return false
//    }


}