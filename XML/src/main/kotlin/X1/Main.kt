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
    merger.mergeById()
    println(merger.result.size)
    merger.printAll()
}


