package org.ownprofile.boundary.owner.resources

import java.util.List
import org.ownprofile.boundary.ProfileDTO
import org.ownprofile.boundary.common.DomainExt
import org.ownprofile.boundary.common.HtmlExt
import org.ownprofile.boundary.owner.OwnerUriBuilder
import org.ownprofile.boundary.common.PageHeaderExt
import org.ownprofile.boundary.common.Section

class OwnerApiProfileTemplate {

	extension DomainExt domainExt = new DomainExt
	extension HtmlExt htmlExt = new HtmlExt
	extension PageHeaderExt headerExt = new PageHeaderExt

	def ownerProfilesOverviewPage(List<ProfileDTO> ownerProfiles, OwnerUriBuilder uriBuilder) {
		ownerProfiles.map [ profile |
			'''
				«profile.asLink»
			'''
		].ul.html(Section.OwnerProfiles.title, pageHeader(uriBuilder))
	}
	
	def ownerProfilePage(ProfileDTO profile, OwnerUriBuilder uriBuilder) {
		profile.section.html(profile.asTitle, pageHeader(uriBuilder))
	}
}
