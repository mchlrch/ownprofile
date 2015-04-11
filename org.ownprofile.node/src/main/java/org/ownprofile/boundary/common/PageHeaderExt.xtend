package org.ownprofile.boundary.common

import org.ownprofile.boundary.owner.OwnerUriBuilder

class PageHeaderExt {

	extension HtmlExt htmlExt = new HtmlExt

	def String pageHeader(OwnerUriBuilder uriBuilder) '''
		«FOR section : Section.values SEPARATOR "|"»
			«section.getLocation(uriBuilder).link(section.title)»
		«ENDFOR»
	'''

}
