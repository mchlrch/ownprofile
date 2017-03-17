package org.ownprofile.boundary.owner;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.ownprofile.boundary.ProfileConverter;
import org.ownprofile.boundary.ProfileDTO;
import org.ownprofile.boundary.ProfileNewDTO;
import org.ownprofile.boundary.UriBuilders;
import org.ownprofile.profile.entity.ProfileEntity;
import org.ownprofile.profile.entity.ProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.persist.Transactional;

public class MyProfileService {

	final Logger logger = LoggerFactory.getLogger(MyProfileService.class);

	@Inject
	private ProfileRepository profileRepo;

	@Inject
	private ProfileConverter converter;

	@Inject
	private UriBuilders uriBuilders;

	@Transactional
	public List<ProfileDTO> getMyProfiles() {
		final List<ProfileEntity> profiles = profileRepo.getAllOwnerProfiles();

		final List<ProfileDTO> result = profiles.stream()
				.map(p -> converter.convertOwnerProfileToView(p, uriBuilders.owner()))
				.collect(Collectors.toList());

		return result;
	}

	@Transactional
	public Optional<ProfileDTO> getMyProfileById(long id) {
		final Optional<ProfileEntity> profile = profileRepo.getOwnerProfileById(id);

		final Optional<ProfileDTO> result = profile
				.map(p -> Optional.of(converter.convertOwnerProfileToView(p, uriBuilders.owner())))
				.orElse(Optional.empty());

		return result;
	}

	@Transactional
	public Long addNewMyProfile(ProfileNewDTO profile) {
		final ProfileEntity newProfile = converter.createEntityForOwnerProfile(profile);
		profileRepo.addProfile(newProfile);
		return newProfile.getId().get();
	}

}
