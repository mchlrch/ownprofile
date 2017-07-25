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
import org.ownprofile.boundary.BoundaryConstants

class ContactsTemplate {

	@Inject	extension DomainExt domainExt
	@Inject	extension HtmlExt htmlExt
	@Inject	extension PageHeaderExt headerExt
	@Inject UriBuilders uriBuilders
	
	def contactsOverviewPage(List<ContactDTO> contacts) {
		'''
			«uriBuilders.owner.addNewContactHtmlFormURI.link("add contact")»
			«contacts.map [ contact |
				'''
					«contact.asLink»
					«contact.profiles.map[asLinkWithSimpleName].ul»
				'''
			].ul»
		'''.html(Section.Contacts.title, pageHeader)
	}

	def contactPage(ContactAggregateDTO contact) {
		'''
			«deleteAndEditButtons(contact.header.id)»
			<hr/>
			«contact.profiles.map [ profile |
				'''
					<hr/>
					«profile.asLinkedTitle.h2»
					«profile.section»
				'''
			].concat»
		'''.html(contact.asTitle, pageHeader)
	}

	def contactProfilePage(ProfileDTO profile) {
		profile.section.html(profile.asTitle, pageHeader)
	}
	
	def addNewContactForm() {
		'''
			<form action="«Section.Contacts.getLocation(uriBuilders.owner)»" method="post">
			  <fieldset>
			    <legend>Add New Contact:</legend>
			    
			    «ContactHeaderDTO.P_PETNAME.toFirstUpper»:<br>
			    <input type="text" name="«ContactHeaderDTO.P_PETNAME»" size="64" value="petname"><br><br>
			    
			    <input type="submit" value="Submit">
			  </fieldset>
			</form>
		'''.html(Section.Contacts.title, pageHeader)
	}
	
	def editContactForm(ContactAggregateDTO contact) {
		'''
			<form action="«uriBuilders.owner.resolveContactURI(contact.header.id)»" method="post">
			  <fieldset>
			    <legend>Edit Contact:</legend>
			    
			    «ContactHeaderDTO.P_PETNAME.toFirstUpper»:<br>
			    <input type="text" name="«ContactHeaderDTO.P_PETNAME»" size="64" value="«contact.header.petname»"><br><br>
			    
			    <input type="submit" name="«BoundaryConstants.ContactForm.ACTION_INPUT_NAME»" value="«BoundaryConstants.ContactForm.ACTION_INPUT_VALUE_SUBMIT_EDIT»">
			    <input type="submit" name="«BoundaryConstants.ContactForm.ACTION_INPUT_NAME»" value="«BoundaryConstants.ContactForm.ACTION_INPUT_VALUE_CANCEL_EDIT»">
			    <input type="reset">
			  </fieldset>
			</form>
		'''.html(contact.asTitle, pageHeader)
	}
	
	def deleteAndEditButtons(long contactId) {
		'''
			<form action="«uriBuilders.owner.resolveContactURI(contactId)»" method="post">
			  <fieldset>
			    <input type="submit" name="«BoundaryConstants.ContactForm.ACTION_INPUT_NAME»" value="«BoundaryConstants.ContactForm.ACTION_INPUT_VALUE_EDIT»">
			    <input type="submit" name="«BoundaryConstants.ContactForm.ACTION_INPUT_NAME»" value="«BoundaryConstants.ContactForm.ACTION_INPUT_VALUE_DELETE»">
			  </fieldset>
			</form>
		'''
	}

}
