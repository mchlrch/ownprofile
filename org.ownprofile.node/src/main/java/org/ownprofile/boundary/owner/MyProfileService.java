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
import org.ownprofile.profile.entity.MyProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.persist.Transactional;

public class MyProfileService {

	final Logger logger = LoggerFactory.getLogger(MyProfileService.class);

	@Inject
	private MyProfileRepository profileRepo;

	@Inject
	private ProfileConverter converter;

	@Inject
	private UriBuilders uriBuilders;

	@Transactional
	public List<ProfileDTO> getMyProfiles() {
		final List<ProfileEntity> profiles = profileRepo.getMyProfiles();

		final List<ProfileDTO> result = profiles.stream()
				.map(p -> converter.convertMyProfileToView(p, uriBuilders.owner()))
				.collect(Collectors.toList());

		return result;
	}

	@Transactional
	public Optional<ProfileDTO> getMyProfileById(long id) {
		final Optional<ProfileEntity> profile = profileRepo.getMyProfileById(id);

		final Optional<ProfileDTO> result = profile
				.map(p -> Optional.of(converter.convertMyProfileToView(p, uriBuilders.owner())))
				.orElse(Optional.empty());

		return result;
	}

	@Transactional
	public Long addNewMyProfile(ProfileNewDTO profile) {
		final ProfileEntity newProfile = converter.createEntityForMyProfile(profile);
		profileRepo.addMyProfile(newProfile);
		return newProfile.getId().get();
	}

}
