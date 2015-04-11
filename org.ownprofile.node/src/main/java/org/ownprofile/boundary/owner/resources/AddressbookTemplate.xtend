package org.ownprofile.boundary.owner.resources

import java.util.List
import org.ownprofile.boundary.ProfileDTO
import org.ownprofile.boundary.common.DomainExt
import org.ownprofile.boundary.common.HtmlExt
import org.ownprofile.boundary.common.PageHeaderExt
import org.ownprofile.boundary.common.Section
import org.ownprofile.boundary.owner.ContactAggregateDTO
import org.ownprofile.boundary.owner.ContactDTO
import org.ownprofile.boundary.owner.OwnerUriBuilder

class AddressbookTemplate {

	extension DomainExt domainExt = new DomainExt
	extension HtmlExt htmlExt = new HtmlExt
	extension PageHeaderExt headerExt = new PageHeaderExt

	def addressbookOverviewPage(List<ContactDTO> contacts, OwnerUriBuilder uriBuilder) {
		contacts.map [ contact |
			'''
				«contact.asLink»
				«contact.profiles.map[asLinkWithSimpleName].ul»
			'''
		].ul.html(Section.Addressbook.title, pageHeader(uriBuilder))
	}

	def addressbookContactPage(ContactAggregateDTO contact, OwnerUriBuilder uriBuilder) {
		contact.profiles.map [ profile |
			'''
				<hr/>
				«profile.asLinkedTitle.h2»
				«profile.section»
			'''
		].concat.html(contact.asTitle, pageHeader(uriBuilder))
	}

	def addressbookContactProfilePage(ProfileDTO profile, OwnerUriBuilder uriBuilder) {
		profile.section.html(profile.asTitle, pageHeader(uriBuilder))
	}

}
