package com.haipiao.imageservice.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Component
@Validated
@ConfigurationProperties
public class ImageUploadProperties {

    private STS sts;

    public STS getSts() {
        return sts;
    }

    public void setSts(STS sts) {
        this.sts = sts;
    }

    @Override
    public String toString() {
        return "ImageUploadProperty{" +
                "sts=" + sts +
                '}';
    }

    public static class STS {

        @NotEmpty
        private String stsEndpoint;

        @NotEmpty
        private String ossEndpoint;

        @NotEmpty
        private String accessKeyId;

        @NotEmpty
        private String accessKeySecret;

        @NotEmpty
        private String roleArn;

        public String getStsEndpoint() {
            return stsEndpoint;
        }

        public void setStsEndpoint(String stsEndpoint) {
            this.stsEndpoint = stsEndpoint;
        }

        public String getOssEndpoint() {
            return ossEndpoint;
        }

        public void setOssEndpoint(String ossEndpoint) {
            this.ossEndpoint = ossEndpoint;
        }

        public String getAccessKeyId() {
            return accessKeyId;
        }

        public void setAccessKeyId(String accessKeyId) {
            this.accessKeyId = accessKeyId;
        }

        public String getAccessKeySecret() {
            return accessKeySecret;
        }

        public void setAccessKeySecret(String accessKeySecret) {
            this.accessKeySecret = accessKeySecret;
        }

        public String getRoleArn() {
            return roleArn;
        }

        public void setRoleArn(String roleArn) {
            this.roleArn = roleArn;
        }

        @Override
        public String toString() {
            return "STS{" +
                    "stsEndpoint='" + stsEndpoint + '\'' +
                    ", ossEndpoint='" + ossEndpoint + '\'' +
                    ", accessKeyId='" + accessKeyId + '\'' +
                    ", accessKeySecret='" + accessKeySecret + '\'' +
                    ", roleArn='" + roleArn + '\'' +
                    '}';
        }
    }
}
