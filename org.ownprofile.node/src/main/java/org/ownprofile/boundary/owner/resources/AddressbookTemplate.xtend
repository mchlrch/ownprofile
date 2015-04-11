package org.ownprofile.boundary.owner.resources

import java.util.List
import javax.inject.Inject
import org.ownprofile.boundary.ProfileDTO
import org.ownprofile.boundary.common.DomainExt
import org.ownprofile.boundary.common.HtmlExt
import org.ownprofile.boundary.common.PageHeaderExt
import org.ownprofile.boundary.common.Section
import org.ownprofile.boundary.owner.ContactAggregateDTO
import org.ownprofile.boundary.owner.ContactDTO

class AddressbookTemplate {

	@Inject	extension DomainExt domainExt
	@Inject	extension HtmlExt htmlExt
	@Inject	extension PageHeaderExt headerExt
	
	def addressbookOverviewPage(List<ContactDTO> contacts) {
		contacts.map [ contact |
			'''
				«contact.asLink»
				«contact.profiles.map[asLinkWithSimpleName].ul»
			'''
		].ul.html(Section.Addressbook.title, pageHeader)
	}

	def addressbookContactPage(ContactAggregateDTO contact) {
		contact.profiles.map [ profile |
			'''
				<hr/>
				«profile.asLinkedTitle.h2»
				«profile.section»
			'''
		].concat.html(contact.asTitle, pageHeader)
	}

	def addressbookContactProfilePage(ProfileDTO profile) {
		profile.section.html(profile.asTitle, pageHeader)
	}

}
