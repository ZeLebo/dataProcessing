package X1

import javax.xml.parsers.SAXParserFactory

fun main() {
    // read the data from the XML file
    val factory = SAXParserFactory.newInstance()
    val parser = factory.newSAXParser()
    // at that point we have 299756 people (need to merge them)
    val merger = Merger()
    val handler = PersonHandler()
    handler.merger = merger
    parser.parse("src/people.xml", handler)

    merger.getResult()
    println(merger.result.size)
    merger.printAll()
}


