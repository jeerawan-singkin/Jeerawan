package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Profile;
import com.example.demo.repository.ProfileRepository;
import java.util.Base64;

@Service
public class ProfileService {

    @Autowired
    ProfileRepository profileRepository;

    public String convertToBase64(byte[] image) {
        return Base64.getEncoder().encodeToString(image);
    }

    
    public Profile getProfileByAuthorId(Long authorId) {
        Profile profile = profileRepository.findByAuthorId(authorId);
        if (profile == null) {
            System.out.println("Profile not found for authorId: " + authorId);
        } else {
            System.out.println("Profile found for authorId: " + authorId);
        }
        return profile;
    }


    public Profile saveProfile(Profile profile) {
        return profileRepository.save(profile);
    }

    public Profile updateProfile(Long authorId, Profile updatedProfile) {
        Profile existingProfile = profileRepository.findByAuthorId(authorId);

        if (existingProfile != null) {
            existingProfile.setBio(updatedProfile.getBio());
            if (updatedProfile.getImage() != null) {
                existingProfile.setImage(updatedProfile.getImage());
            }
            return profileRepository.save(existingProfile);
        }

        return null;
    }
    
}
