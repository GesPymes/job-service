package com.gespyme.infrastructure.config.cloud;

import com.gespyme.commons.exeptions.InternalServerError;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.auth.Credentials;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.ServiceAccountCredentials;
import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

@Configuration
@RequiredArgsConstructor
public class GoogleCloudConfig {

  private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
  private static final String APPLICATION_NAME = "GesPyme Calendar Integration";
  private final S3Client s3Client;

  @Value("${aws.s3.bucketname}")
  private String bucketName;

  @Value("${aws.s3.googlecredentials}")
  private String objectKey = "googlecloud/service-key.json";

  @Bean
  public Credentials getCredentials() {
    GetObjectRequest getObjectRequest =
        GetObjectRequest.builder().bucket(bucketName).key(objectKey).build();
    try (ResponseInputStream<GetObjectResponse> reader = s3Client.getObject(getObjectRequest)) {
      return ServiceAccountCredentials.fromStream(reader).createScoped(SCOPES);
    } catch (IOException e) {
      throw new InternalServerError("Error retrieving service account key from S3", e);
    }
  }

  @Bean
  public Calendar getCalendarService() {
    try {
      return new Calendar.Builder(
              GoogleNetHttpTransport.newTrustedTransport(),
              JSON_FACTORY,
              new HttpCredentialsAdapter(getCredentials()))
          .setApplicationName(APPLICATION_NAME)
          .build();
    } catch (GeneralSecurityException | IOException e) {
      throw new InternalServerError("Error creating calendar service", e);
    }
  }
}
