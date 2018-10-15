import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import com.amazonaws.services.securitytoken.model.AssumeRoleRequest;
import com.amazonaws.services.securitytoken.model.AssumeRoleResult;
import com.amazonaws.services.securitytoken.model.GetSessionTokenRequest;
import com.amazonaws.services.securitytoken.model.GetSessionTokenResult;

public class GetSTSToken {


  public static final TestAWSCredentials TEST_AWS_CREDENTIALS = new TestAWSCredentials();
  public static final TestAWSCredentialsProvider TEST_AWS_CREDENTIALS_PROVIDER = new TestAWSCredentialsProvider();

  public static void main(String[] args) {
    getSessionToken();
  }

  public static void getSessionToken() {
    AWSSecurityTokenService client = AWSSecurityTokenServiceClientBuilder.standard().withCredentials(TEST_AWS_CREDENTIALS_PROVIDER).build();
    GetSessionTokenRequest request = new GetSessionTokenRequest().withDurationSeconds(3600);
    GetSessionTokenResult response = client.getSessionToken(request);
    System.out.println("response.getCredentials() = " + response.getCredentials());
  }

  public static void assumeRole() {
    AWSSecurityTokenService client = AWSSecurityTokenServiceClientBuilder.standard().withCredentials(TEST_AWS_CREDENTIALS_PROVIDER).build();
    AssumeRoleRequest request = new AssumeRoleRequest().withRoleArn("arn:aws:iam::123456789012:role/demo").withRoleSessionName("Bob")
        .withPolicy("{\"Version\":\"2012-10-17\",\"Statement\":[{\"Sid\":\"Stmt1\",\"Effect\":\"Allow\",\"Action\":\"s3:*\",\"Resource\":\"*\"}]}")
        .withDurationSeconds(3600).withExternalId("123ABC");
    AssumeRoleResult response = client.assumeRole(request);
  }

  static class TestAWSCredentialsProvider implements AWSCredentialsProvider {

    @Override
    public AWSCredentials getCredentials() {
      return TEST_AWS_CREDENTIALS;

    }

    @Override
    public void refresh() {

    }
  }


  static class TestAWSCredentials implements AWSCredentials {

    @Override
    public String getAWSAccessKeyId() {
      return "";
    }

    @Override
    public String getAWSSecretKey() {
      return "";
    }
  }

}
