package org.ownprofile.boundary.owner.resources

import java.util.List
import javax.inject.Inject
import org.ownprofile.boundary.ProfileDTO
import org.ownprofile.boundary.common.DomainExt
import org.ownprofile.boundary.common.HtmlExt
import org.ownprofile.boundary.common.PageHeaderExt
import org.ownprofile.boundary.common.Section

class OwnprofilesTemplate {

	@Inject extension DomainExt domainExt
	@Inject extension HtmlExt htmlExt
	@Inject extension PageHeaderExt headerExt

	def ownerProfilesOverviewPage(List<ProfileDTO> ownerProfiles) {
		ownerProfiles.map [ profile |
			'''
				«profile.asLink»
			'''
		].ul.html(Section.OwnerProfiles.title, pageHeader)
	}
	
	def ownerProfilePage(ProfileDTO profile) {
		profile.section.html(profile.asTitle, pageHeader)
	}
}
