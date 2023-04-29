package X2

import java.io.File
import javax.xml.XMLConstants
import javax.xml.stream.XMLOutputFactory
import javax.xml.stream.XMLStreamWriter
import javax.xml.validation.SchemaFactory
import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller
import javax.xml.bind.UnmarshalException
import javax.xml.bind.ValidationEvent
import javax.xml.bind.ValidationEventHandler

var skipped: Int = 0

class XmlWriter {
    private val outputFile = File("src/output-people.xml")
    private val schemaFile = File("src/people.xsd")
    private val writer: XMLStreamWriter = XMLOutputFactory.newFactory().createXMLStreamWriter(outputFile.outputStream())
    private val schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)

    fun write(people: MutableSet<Person>) {}

    fun write(people: People) {
        people.filterPeopleNonNull()
        println(noName)
        println(noSurname)
        println(noGender)
//        val jc = JAXBContext.newInstance(People::class.java)
//        val marshaller = jc.createMarshaller()
//        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
//        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8")
//        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true)
//        marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://www.example.org/people people.xsd")
//        marshaller.schema = schemaFactory.newSchema(schemaFile)
//        marshaller.marshal(people, writer)
//        writer.close()
//        println("XML file written successfully")
//        println("Skipped $skipped objects")
    }
}


class CustomEventHandler: ValidationEventHandler {
    override fun handleEvent(event: ValidationEvent): Boolean {
        if (event.severity == ValidationEvent.ERROR && event.linkedException is UnmarshalException) {
            val ex = event.linkedException as UnmarshalException
            println("----Error----")
            println(ex.message)
            if (ex.message?.contains("ID/IDREF") == true) {
                skipped++
                return true // skip object with missing ID
            }
        }
        return false
    }
}