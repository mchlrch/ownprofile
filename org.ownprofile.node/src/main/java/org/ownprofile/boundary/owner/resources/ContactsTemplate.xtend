package org.ownprofile.boundary.owner.resources

import java.util.List
import javax.inject.Inject
import org.ownprofile.boundary.ProfileDTO
import org.ownprofile.boundary.UriBuilders
import org.ownprofile.boundary.common.DomainExt
import org.ownprofile.boundary.common.HtmlExt
import org.ownprofile.boundary.common.PageHeaderExt
import org.ownprofile.boundary.common.Section
import org.ownprofile.boundary.owner.ContactAggregateDTO
import org.ownprofile.boundary.owner.ContactDTO
import org.ownprofile.boundary.owner.ContactHeaderDTO

class ContactsTemplate {

	@Inject	extension DomainExt domainExt
	@Inject	extension HtmlExt htmlExt
	@Inject	extension PageHeaderExt headerExt
	@Inject UriBuilders uriBuilders
	
	def addressbookOverviewPage(List<ContactDTO> contacts) {
		'''
			«uriBuilders.owner.addNewContactHtmlFormURI.link("add contact")»
			«contacts.map [ contact |
				'''
					«contact.asLink»
					«contact.profiles.map[asLinkWithSimpleName].ul»
				'''
			].ul»
		'''.html(Section.Addressbook.title, pageHeader)
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
	
	def addNewContactForm() {
		'''
			<form action="«Section.Addressbook.getLocation(uriBuilders.owner)»" method="post">
			  <fieldset>
			    <legend>Add New Contact:</legend>
			    
			    «ContactHeaderDTO.P_PETNAME.toFirstUpper»:<br>
			    <input type="text" name="«ContactHeaderDTO.P_PETNAME»" size="64" value="petname"><br><br>
			    
			    <input type="submit" value="Submit">
			  </fieldset>
			</form>
		'''.html(Section.Addressbook.title, pageHeader)
	}

}
