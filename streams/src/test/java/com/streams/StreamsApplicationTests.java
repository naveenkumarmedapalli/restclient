package com.streams;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StreamsApplicationTests {

	@Test
	void contextLoads() throws IOException {
		
		RestClient rc = new RestClient("clientID", "clientSecret", "serverURL");
		rc.authorize("username", "password", "granttype");

	}
	

}
