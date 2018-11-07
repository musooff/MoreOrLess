package com.footballnukes.moreorlessfootballers.updater.model

/**
 * Created by musooff on 07/11/2018.
 */

class Original {
    var source: String? = null
    var width: Int = 0
    var height: Int = 0
}
class Item{
    var pageid: Int = 0
    var ns: Int = 0
    var title: String? = null
    var original: Original? = null
    var imageinfo: List<Imageinfo> = arrayListOf()
}

class Pages{
    var data: Map<String, Item>? = null
}

class Query{
    var pages: Map<String, Item>? = null
    var normalized: List<Element> = arrayListOf()
}
class Element{
    var from: String? = null
    var to: String? = null
}
class WikiTitle{
    var query: Query? = null
}

