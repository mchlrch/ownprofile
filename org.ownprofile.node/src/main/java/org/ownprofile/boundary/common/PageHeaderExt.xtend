package org.ownprofile.boundary.common

import javax.inject.Inject
import org.ownprofile.boundary.UriBuilders

class PageHeaderExt {

	@Inject	extension HtmlExt htmlExt
	
	@Inject	UriBuilders uriBuilders

	def String pageHeader() '''
		«FOR section : Section.values SEPARATOR " | "»
			«section.getLocation(uriBuilders.owner).link(section.title)»
		«ENDFOR»
	'''

}
