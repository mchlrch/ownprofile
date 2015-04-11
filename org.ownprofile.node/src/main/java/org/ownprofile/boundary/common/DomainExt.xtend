package org.ownprofile.boundary.common

import org.ownprofile.boundary.ProfileDTO
import org.ownprofile.boundary.ProfileHeaderDTO
import org.ownprofile.boundary.owner.ContactDTO
import org.ownprofile.boundary.owner.ContactHeaderDTO
import org.ownprofile.boundary.owner.ContactAggregateDTO
import java.util.Map
import java.util.List

class DomainExt {

	extension HtmlExt htmlExt = new HtmlExt

	def asLink(ContactDTO it) '''«header.asLink»'''

	def asLink(ContactAggregateDTO it) '''«header.asLink»'''

	def asLink(ContactHeaderDTO it) '''«href.link(petname)»'''

	def asTitle(ContactAggregateDTO it) '''«header.asTitle»'''

	def asTitle(ContactHeaderDTO it) '''contact: «petname»'''

	// ----------------------------------------
	def asText(ProfileHeaderDTO it) {
		if (getContainer().present) {
			val ContactHeaderDTO contact = getContainer().get();
			return '''«contact.petname»/«profileName»'''

		} else {
			return '''«profileName»'''
		}
	}

	def asLink(ProfileDTO it) '''«header.asLink»'''

	def asLink(ProfileHeaderDTO it) '''«href.link(asText)»'''

	def asLinkWithSimpleName(ProfileHeaderDTO it) '''«href.link(profileName)»'''

	def asTitle(ProfileDTO it) '''«header.asTitle»'''

	def asTitle(ProfileHeaderDTO it) '''«typePrefix»: «asText»'''

	def asLinkedTitle(ProfileDTO it) '''«header.asLinkedTitle»'''

	def asLinkedTitle(ProfileHeaderDTO it) '''«typePrefix»: «asLink»'''
	
	def typePrefix(ProfileHeaderDTO it) '''«type.toLowerCase»-profile'''

	def section(ProfileDTO profile) '''
		<h3>header</h3>
		«profile.header.details.ul»
		
		<h3>body</h3>
		«profile.body.ul»
	'''

	def List<String> details(ProfileHeaderDTO it) {
		val result = newArrayList();
		if (getContainer.present) {
			result.add('''container: «getContainer().get().asLink»''');

		}
		result.add('''profileName: «profileName»''');
		result.add('''handle: «handle»''');
		result.add('''source: «source»''');

		return result;
	}

	// ----------------------------------------
	def CharSequence ul(Map<String, Object> body) {
		if (body.empty) {
			"<i>empty</i>"
		} else {
			body.entrySet.map['''«key»: «value.valueAsText»'''].ul
		}
	}

	private def dispatch valueAsText(Map<String, Object> value) {
		value.ul
	}

	private def dispatch valueAsText(Object value) {
		value.toString
	}
}
