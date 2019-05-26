package com.haipiao.persist.entity;

import com.haipiao.persist.enums.Gender;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "hp_user")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "password_digest")
    private byte[] passwordDigest;

    @Column(name = "nick_name")
    private String userName;

    private String email;

    private String phone;

    @Column(name = "tc_accepted")
    private boolean tcAccepted;

    private Date birthday;

    private Gender gender;

    @Column(name = "real_name")
    private String realName;

    @Column(name = "address_1")
    private String address1;

    @Column(name = "address_2")
    private String address2;

    private String city;

    private String region;

    private String country;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "profile_img_url")
    private String profileImgUrl;

    private String organization;

    private String signature;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public byte[] getPasswordDigest() {
        return passwordDigest;
    }

    public void setPasswordDigest(byte[] passwordDigest) {
        this.passwordDigest = passwordDigest;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isTcAccepted() {
        return tcAccepted;
    }

    public void setTcAccepted(boolean tcAccepted) {
        this.tcAccepted = tcAccepted;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getProfileImgUrl() {
        return profileImgUrl;
    }

    public void setProfileImgUrl(String profileImgUrl) {
        this.profileImgUrl = profileImgUrl;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return tcAccepted == user.tcAccepted &&
            Objects.equals(userId, user.userId) &&
            Arrays.equals(passwordDigest, user.passwordDigest) &&
            Objects.equals(userName, user.userName) &&
            Objects.equals(email, user.email) &&
            Objects.equals(phone, user.phone) &&
            Objects.equals(birthday, user.birthday) &&
            gender == user.gender &&
            Objects.equals(realName, user.realName) &&
            Objects.equals(address1, user.address1) &&
            Objects.equals(address2, user.address2) &&
            Objects.equals(city, user.city) &&
            Objects.equals(region, user.region) &&
            Objects.equals(country, user.country) &&
            Objects.equals(zipCode, user.zipCode) &&
            Objects.equals(profileImgUrl, user.profileImgUrl) &&
            Objects.equals(organization, user.organization) &&
            Objects.equals(signature, user.signature);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(userId, userName, email, phone, tcAccepted, birthday, gender, realName, address1, address2, city, region, country, zipCode, profileImgUrl, organization, signature);
        result = 31 * result + Arrays.hashCode(passwordDigest);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
            "userId=" + userId +
            ", passwordDigest=" + Arrays.toString(passwordDigest) +
            ", userName='" + userName + '\'' +
            ", email='" + email + '\'' +
            ", phone='" + phone + '\'' +
            ", tcAccepted=" + tcAccepted +
            ", birthday=" + birthday +
            ", gender=" + gender +
            ", realName='" + realName + '\'' +
            ", address1='" + address1 + '\'' +
            ", address2='" + address2 + '\'' +
            ", city='" + city + '\'' +
            ", region='" + region + '\'' +
            ", country='" + country + '\'' +
            ", zipCode='" + zipCode + '\'' +
            ", profileImgUrl='" + profileImgUrl + '\'' +
            ", organization='" + organization + '\'' +
            ", signature='" + signature + '\'' +
            '}';
    }
}
