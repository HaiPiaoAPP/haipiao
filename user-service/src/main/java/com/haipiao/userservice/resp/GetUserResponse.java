package com.haipiao.userservice.resp;

import com.google.gson.annotations.SerializedName;
import com.haipiao.common.enums.StatusCode;
import com.haipiao.common.resp.AbstractResponse;

public class GetUserResponse extends AbstractResponse<GetUserResponse.Data> {

    public GetUserResponse(StatusCode statusCode) {
        super(statusCode);
    }

    public static class Data {
        @SerializedName("id")
        private int id;

        @SerializedName("name")
        private String name;

        @SerializedName("gender")
        private String gender;

        @SerializedName("level")
        private String level;

        @SerializedName("followers_count")
        private int followersCount;

        @SerializedName("total_likes")
        private int totalLikes;

        @SerializedName("total_collects")
        private int totalCollects;

        @SerializedName("profile_image_url")
        private String profileImageUrl;

        @SerializedName("profile_image_url_small")
        private String profileImageUrlSmall;

        @SerializedName("description")
        private String description;

        @SerializedName("location")
        private String location;

        @SerializedName("organization")
        private String organization;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public int getFollowersCount() {
            return followersCount;
        }

        public void setFollowersCount(int followersCount) {
            this.followersCount = followersCount;
        }

        public int getTotalLikes() {
            return totalLikes;
        }

        public void setTotalLikes(int totalLikes) {
            this.totalLikes = totalLikes;
        }

        public int getTotalCollects() {
            return totalCollects;
        }

        public void setTotalCollects(int totalCollects) {
            this.totalCollects = totalCollects;
        }

        public String getProfileImageUrl() {
            return profileImageUrl;
        }

        public void setProfileImageUrl(String profileImageUrl) {
            this.profileImageUrl = profileImageUrl;
        }

        public String getProfileImageUrlSmall() {
            return profileImageUrlSmall;
        }

        public void setProfileImageUrlSmall(String profileImageUrlSmall) {
            this.profileImageUrlSmall = profileImageUrlSmall;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getOrganization() {
            return organization;
        }

        public void setOrganization(String organization) {
            this.organization = organization;
        }
    }
}
