import com.amazonaws.auth.AWSCredentials;

public class TestAWSCredentials implements AWSCredentials {

  public static final TestAWSCredentials TEST_AWS_CREDENTIALS = new TestAWSCredentials(
      "", "");

  private final String accessKeyId;
  private final String secretKey;

  public TestAWSCredentials(String accessKeyId, String secretKey) {
    this.accessKeyId = accessKeyId;
    this.secretKey = secretKey;
  }

  @Override
  public String getAWSAccessKeyId() {
    return accessKeyId;
  }

  @Override
  public String getAWSSecretKey() {
    return secretKey;
  }
}