package com.samyotech.laundry.model;

import com.google.gson.annotations.SerializedName;

public class RegisterNewDto{

	@SerializedName("address")
	private String address;

	@SerializedName("latitude")
	private String latitude;

	@SerializedName("mobile")
	private String mobile;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("device_type")
	private String deviceType;

	@SerializedName("country_code")
	private String countryCode;

	@SerializedName("password")
	private String password;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("device_token")
	private String deviceToken;

	@SerializedName("otp_sms")
	private String otpSms;

	@SerializedName("name")
	private String name;

	@SerializedName("email")
	private String email;

	@SerializedName("status")
	private String status;

	@SerializedName("longitude")
	private String longitude;

	public String getAddress(){
		return address;
	}

	public String getLatitude(){
		return latitude;
	}

	public String getMobile(){
		return mobile;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public String getDeviceType(){
		return deviceType;
	}

	public String getCountryCode(){
		return countryCode;
	}

	public String getPassword(){
		return password;
	}

	public String getUserId(){
		return userId;
	}

	public String getDeviceToken(){
		return deviceToken;
	}

	public String getOtpSms(){
		return otpSms;
	}

	public String getName(){
		return name;
	}

	public String getEmail(){
		return email;
	}

	public String getStatus(){
		return status;
	}

	public String getLongitude(){
		return longitude;
	}
}