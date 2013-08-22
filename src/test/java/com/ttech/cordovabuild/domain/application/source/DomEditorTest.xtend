package com.ttech.cordovabuild.domain.application.source

import org.junit.Test
import javax.xml.parsers.DocumentBuilderFactory
import java.io.ByteArrayInputStream

import static org.junit.Assert.*

class DomEditorTest {
	val configData = '
	<widget xmlns = "http://www.w3.org/ns/widgets"
        xmlns:gap = "http://phonegap.com/ns/1.0"
        id = "com.turkcell.restaurantee"
        version = "1.0.0">

    	<name>RestaurantReviews</name>

    	<description>
        	Application for location based reviewing restaurants.
    	</description>

    	<author href="http://www.turkcell.com.tr" email="support@turkcelltech.com">
        	Turkcell OpenLab
    	</author>

    	<preference name="phonegap-version" value="2.5.0" />

    	<feature name="http://api.phonegap.com/1.0/network" />
    	<feature name="http://api.phonegap.com/1.0/geolocation"/>
    	<feature name="http://api.phonegap.com/1.0/notification"/>

    	<icon src="icons/rest_icon_57.png" width="57" height="57" />
    	<icon src="icons/rest_icon_72.png" width="72" height="72" />

    	<access origin="*" />
	</widget>'

	@Test
	def testDomEditor() {
		val document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
			new ByteArrayInputStream(configData.bytes))
		val domEditor = new DomEditor(document)
		assertEquals(domEditor.name, 'RestaurantReviews')
		assertEquals(domEditor.package, 'com.turkcell.restaurantee')
		assertEquals(domEditor.version, '1.0.0')
		assertEquals(domEditor.phoneGapVersion, '2.5.0')
		
		domEditor.name = 'example'
		domEditor.version = '2.0.0'
		domEditor.package = 'package'
		domEditor.phoneGapVersion = '3.0.0'
		
		assertEquals(domEditor.name, 'example')
		assertEquals(domEditor.package, 'package')
		assertEquals(domEditor.version, '2.0.0')
		assertEquals(domEditor.phoneGapVersion, '3.0.0')
	}
}
