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
import org.ownprofile.boundary.ProfileHeaderDTO

class ContactsTemplate {

	@Inject	extension DomainExt domainExt
	@Inject	extension HtmlExt htmlExt
	@Inject	extension PageHeaderExt headerExt
	@Inject UriBuilders uriBuilders
	
	def contactsOverviewPage(List<ContactDTO> contacts) {
		'''
			«uriBuilders.owner.addContactHtmlFormURI.link("add contact")»
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
			«contactDeleteAndEditButtons(contact.header.id)»
			<hr/>
			«uriBuilders.owner.resolveAddContactProfileHtmlFormURI(contact.header.id).link("add profile")»
			«contact.profiles.map [ profile |
				'''
					<hr/>
					«profile.asLinkedTitle.h2»
					«contactProfileDeleteAndEditButtons(profile.header.id)»
					«profile.section»
				'''
			].concat»
		'''.html(contact.asTitle, pageHeader)
	}

	def contactProfilePage(ProfileDTO profile) {
		'''
			«contactProfileDeleteAndEditButtons(profile.header.id)»
			<hr/>
			«profile.section»
		'''.html(profile.asTitle, pageHeader)
	}
	
	def addContactForm() {
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
	
	def addContactProfileForm(long contactId) {
		'''
			<form action="«uriBuilders.owner.resolveContactProfilesCollectionURI(contactId)»" method="post">
			  <fieldset>
			    <legend>Add New Profile:</legend>
			    
			    «ProfileHeaderDTO.P_PROFILENAME.toFirstUpper»:<br>
			    <input type="text" name="«ProfileHeaderDTO.P_PROFILENAME»" size="64" value="profilename"><br><br>
			    
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
	
	def editContactProfileForm(ProfileDTO profile) {
		'''
			<form action="«uriBuilders.owner.resolveContactProfileURI(profile.header.id)»" method="post">
			  <fieldset>
			    <legend>Edit Contact Profile:</legend>
			    
			    «ProfileHeaderDTO.P_PROFILENAME.toFirstUpper»:<br>
			    <input type="text" name="«ProfileHeaderDTO.P_PROFILENAME»" size="64" value="«profile.header.profileName»"><br><br>
			    
			    «ProfileDTO.P_BODY.toFirstUpper»:<br>
			    <textarea name="«ProfileDTO.P_BODY»" rows="32" cols="80">«profile.bodyAsJson»</textarea><br>
			    
			    <input type="submit" name="«BoundaryConstants.ContactProfileForm.ACTION_INPUT_NAME»" value="«BoundaryConstants.ContactProfileForm.ACTION_INPUT_VALUE_SUBMIT_EDIT»">
			    <input type="submit" name="«BoundaryConstants.ContactProfileForm.ACTION_INPUT_NAME»" value="«BoundaryConstants.ContactProfileForm.ACTION_INPUT_VALUE_CANCEL_EDIT»">
			    <input type="reset">
			  </fieldset>
			</form>
		'''.html(profile.asTitle, pageHeader)
	}
	
	private def contactDeleteAndEditButtons(long contactId) {
		'''
			<form action="«uriBuilders.owner.resolveContactURI(contactId)»" method="post">
			  <fieldset>
			    <input type="submit" name="«BoundaryConstants.ContactForm.ACTION_INPUT_NAME»" value="«BoundaryConstants.ContactForm.ACTION_INPUT_VALUE_EDIT»">
			    <input type="submit" name="«BoundaryConstants.ContactForm.ACTION_INPUT_NAME»" value="«BoundaryConstants.ContactForm.ACTION_INPUT_VALUE_DELETE»">
			  </fieldset>
			</form>
		'''
	}
	
	private def contactProfileDeleteAndEditButtons(long contactId) {
		'''
			<form action="«uriBuilders.owner.resolveContactProfileURI(contactId)»" method="post">
			  <fieldset>
			    <input type="submit" name="«BoundaryConstants.ContactProfileForm.ACTION_INPUT_NAME»" value="«BoundaryConstants.ContactForm.ACTION_INPUT_VALUE_EDIT»">
			    <input type="submit" name="«BoundaryConstants.ContactProfileForm.ACTION_INPUT_NAME»" value="«BoundaryConstants.ContactForm.ACTION_INPUT_VALUE_DELETE»">
			  </fieldset>
			</form>
		'''
	}

}
