package com.banno.gordon

import org.xmlpull.v1.XmlPullParserFactory
import org.xmlpull.v1.XmlSerializer
import java.io.StringWriter

internal fun xmlDocument(block: XmlSerializer.() -> Unit) = StringWriter().also {
    XmlPullParserFactory.newInstance(System.getProperty(XmlPullParserFactory.PROPERTY_NAME), null).newSerializer().run {
        setOutput(it)
        indentOutput()
        startDocument("UTF-8", null)
        block()
        endDocument()
    }
}.toString()

private fun XmlSerializer.indentOutput() {
    try {
        setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true)
    } catch (ignored: Throwable) {
        try {
            setProperty("http://xmlpull.org/v1/doc/properties.html#serializer-indentation", "  ")
            setProperty("http://xmlpull.org/v1/doc/properties.html#serializer-line-separator", "\n")
        } catch (ignored: Throwable) {
        }
    }
}

internal fun XmlSerializer.attribute(name: String, value: String) {
    attribute("", name, value)
}

internal fun XmlSerializer.element(name: String, block: XmlSerializer.() -> Unit = {}) {
    startTag("", name)
    block()
    endTag("", name)
}

internal fun XmlSerializer.element(name: String, text: String, block: XmlSerializer.() -> Unit = {}) = element(name) {
    block()
    text(text)
}
