import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import com.amazonaws.services.securitytoken.model.AssumeRoleRequest;
import com.amazonaws.services.securitytoken.model.AssumeRoleResult;
import com.amazonaws.services.securitytoken.model.Credentials;
import com.amazonaws.services.securitytoken.model.GetSessionTokenRequest;
import com.amazonaws.services.securitytoken.model.GetSessionTokenResult;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class GetSTSToken {

  public static final String BUCKET = "bucket";

  public static void main(String[] args) throws IOException {
    accessS3UsingGetSessionToken();
  }

  public static void accessS3UsingGetSessionToken() throws IOException {
    GetSessionTokenResult response = getGetSessionTokenResult();

    final AmazonS3 amazonS3 = getS3Client(response.getCredentials());
    final InputStream inputStream = getObjectInputStreamFromS3(amazonS3);
    printInputStream(inputStream);

  }

  private static void printInputStream(InputStream inputStream) throws IOException {
    if (inputStream == null) {
      System.out.println("Null inputStream received from S3 GetObject while fetching s3 object");
    }

    StringBuilder textBuilder = new StringBuilder();
    try (Reader reader = new BufferedReader(new InputStreamReader
        (inputStream, Charset.forName(StandardCharsets.UTF_8.name())))) {
      int c = 0;
      while ((c = reader.read()) != -1) {
        textBuilder.append((char) c);
      }
    }

    System.out.println("textBuilder.toString() = " + textBuilder.toString());
  }

  private static InputStream getObjectInputStreamFromS3(AmazonS3 amazonS3) {
    final GetObjectRequest getObjectRequest = new GetObjectRequest(BUCKET, "key");
    final S3Object s3Object = amazonS3.getObject(getObjectRequest);
    return s3Object.getObjectContent();
  }

  private static GetSessionTokenResult getGetSessionTokenResult() {
    AWSSecurityTokenService client = AWSSecurityTokenServiceClientBuilder.standard()
        .withCredentials(TestAWSCredentialsProvider.TEST_AWS_CREDENTIALS_PROVIDER).build();

    GetSessionTokenRequest request = new GetSessionTokenRequest().withDurationSeconds(3600);
    GetSessionTokenResult response = client.getSessionToken(request);

    System.out.println("response.getCredentials() = " + response.getCredentials());
    return response;
  }

  public static void assumeRole() {
    AWSSecurityTokenService client = AWSSecurityTokenServiceClientBuilder.standard()
        .withCredentials(TestAWSCredentialsProvider.TEST_AWS_CREDENTIALS_PROVIDER).build();
    AssumeRoleRequest request = new AssumeRoleRequest()
        .withRoleArn("arn:aws:iam::123456789012:role/demo").withRoleSessionName("Bob")
        .withPolicy(
            "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Sid\":\"Stmt1\",\"Effect\":\"Allow\",\"Action\":\"s3:*\",\"Resource\":\"*\"}]}")
        .withDurationSeconds(3600).withExternalId("123ABC");
    AssumeRoleResult response = client.assumeRole(request);
  }

  //  https://docs.aws.amazon.com/AmazonS3/latest/dev/AuthUsingTempSessionTokenJava.html
  static AmazonS3 getS3Client(Credentials credentials) {

    final BasicSessionCredentials basicSessionCredentials = new BasicSessionCredentials(
        credentials.getAccessKeyId(), credentials.getSecretAccessKey(),
        credentials.getSessionToken()
    );

    return AmazonS3ClientBuilder
        .standard()
        .withCredentials(new AWSStaticCredentialsProvider(basicSessionCredentials))
        .withRegion("us-west-2")
        .build();
  }

}
