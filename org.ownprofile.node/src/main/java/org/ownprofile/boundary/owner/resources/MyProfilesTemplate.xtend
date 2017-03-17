package org.ownprofile.boundary.owner.resources

import java.util.List
import javax.inject.Inject
import org.ownprofile.boundary.ProfileDTO
import org.ownprofile.boundary.common.DomainExt
import org.ownprofile.boundary.common.HtmlExt
import org.ownprofile.boundary.common.PageHeaderExt
import org.ownprofile.boundary.common.Section

class MyProfilesTemplate {

	@Inject extension DomainExt domainExt
	@Inject extension HtmlExt htmlExt
	@Inject extension PageHeaderExt headerExt

	def myProfilesOverviewPage(List<ProfileDTO> myProfiles) {
		myProfiles.map [ profile |
			'''
				«profile.asLink»
			'''
		].ul.html(Section.MyProfiles.title, pageHeader)
	}
	
	def myProfilePage(ProfileDTO profile) {
		profile.section.html(profile.asTitle, pageHeader)
	}
}
