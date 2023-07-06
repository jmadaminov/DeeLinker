package com.example.deeplinkapplication

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
//        "domain://cabinet",
//        "domain://home",
//        "domain://cabinet/orders",
//        "domain://cabinet/orders/123",
//        "domain://cabinet/orders/all",
////    "domain://cabinet/home",
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