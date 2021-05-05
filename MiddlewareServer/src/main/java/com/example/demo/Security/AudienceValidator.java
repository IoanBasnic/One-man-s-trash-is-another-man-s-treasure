package com.example.demo.Security;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

class AudienceValidator implements OAuth2TokenValidator<Jwt> {
    private final String audience;

    AudienceValidator(String audience) {
        this.audience = audience;
    }

    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        OAuth2Error error = new OAuth2Error("invalid_token", "The required audience is missing", null);

        if (jwt.getAudience().contains(audience)) {
            return OAuth2TokenValidatorResult.success();
        }
        return OAuth2TokenValidatorResult.failure(error);
    }
}

//import org.springframework.security.oauth2.core.OAuth2Error;
//import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
//import org.springframework.security.oauth2.core.OAuth2TokenValidator;
//import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
//import org.springframework.security.oauth2.jwt.Jwt;
//import org.springframework.util.Assert;

//import java.util.List;
//import java.util.Objects;
//
//class AudienceValidator implements OAuth2TokenValidator<Jwt> {
//    private final String audience;
//
//    AudienceValidator(String audience) {
//        Assert.hasText(audience, "audience is null or empty");
//        this.audience = audience;
//    }
//
//    public OAuth2TokenValidatorResult validate(Jwt jwt) {
//        List<String> audiences = jwt.getAudience();
//        if (audiences.contains(this.audience)) {
//            return OAuth2TokenValidatorResult.success();
//        }
//        OAuth2Error err = new OAuth2Error(OAuth2ErrorCodes.INVALID_TOKEN);
//        return OAuth2TokenValidatorResult.failure(err);
//    }
//}