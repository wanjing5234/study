package com.kk.ssj.constant;

import com.kk.ssj.exception.ApiException;

public enum ApiVersion {
    
    V1;
    
    public static ApiVersion fromString(String str) throws ApiException {
        str = str.replace('.', '_').toUpperCase();
        ApiVersion version = null; 
        try {
            version = Enum.valueOf(ApiVersion.class, str);
        } catch (IllegalArgumentException e) {}
        return version;
    }
    
}
