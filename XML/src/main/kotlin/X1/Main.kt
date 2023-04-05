package X1

import javax.xml.parsers.SAXParserFactory

fun main() {
    // read the data from the XML file
    val factory = SAXParserFactory.newInstance()
    val parser = factory.newSAXParser()
    val handler = PersonHandler()
    parser.parse("src/people.xml", handler)

    println(handler.people.size)
    // at that point we have 299756 people (need to merge them)
    val merger = Merger(handler)

    var i = 0
    merger.inputPeople.forEach { it ->
        if (it.siblingsIds.size != 0) {
            it.siblingsIds.forEach { println(it) }
            i++
        }
//        if (it.name != null && it.surname != null) {
//            println(it.name + " " + it.surname)
//            i++
//        }
    }
    println(i)
//    merger.mergeById()
//    println(merger.result.size)
//    merger.printAll()
}


