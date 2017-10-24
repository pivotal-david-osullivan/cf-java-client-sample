package cf_java_client.test;
import org.cloudfoundry.operations.DefaultCloudFoundryOperations;
import org.cloudfoundry.operations.organizations.OrganizationSummary;
import org.cloudfoundry.reactor.DefaultConnectionContext;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.cloudfoundry.reactor.tokenprovider.PasswordGrantTokenProvider;

import java.util.concurrent.CountDownLatch;

public final class JavaSample {

    public static void main(String[] args) {
        String target = args[0];
        String user = args[1];
        String password = args[2];

        DefaultConnectionContext connectionContext  = DefaultConnectionContext.builder()
        .apiHost(target)
        .build();
        
        PasswordGrantTokenProvider tokenProvider = PasswordGrantTokenProvider.builder()
        .password(password)
        .username(user)
        .build();
        
        ReactorCloudFoundryClient cfClient = ReactorCloudFoundryClient.builder()
        .connectionContext(connectionContext)
        .tokenProvider(tokenProvider)
        .build();
        
         DefaultCloudFoundryOperations cloudFoundryOperations = DefaultCloudFoundryOperations.builder()
        .cloudFoundryClient(cfClient)
        .build();
        
         CountDownLatch latch = new CountDownLatch(1);
         
         cloudFoundryOperations.organizations()
         .list()
         .map(OrganizationSummary::getName)
         .subscribe(System.out::println, t->{
        	 		t.printStackTrace();
        	 		latch.countDown();
         }, latch::countDown);
         
         try {
			latch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}