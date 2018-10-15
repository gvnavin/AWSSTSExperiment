import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;

public class TestAWSCredentialsProvider implements AWSCredentialsProvider {

  public static final TestAWSCredentialsProvider TEST_AWS_CREDENTIALS_PROVIDER =
      new TestAWSCredentialsProvider(TestAWSCredentials.TEST_AWS_CREDENTIALS);

  private final AWSCredentials awsCredentials;

  public TestAWSCredentialsProvider(AWSCredentials awsCredentials) {
    this.awsCredentials = awsCredentials;
  }

  @Override
  public AWSCredentials getCredentials() {
    return awsCredentials;

  }

  @Override
  public void refresh() {

  }
}