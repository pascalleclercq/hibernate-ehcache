package com.devx.tdd.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.devx.tdd.test.integration.PersonServiceTest;

@RunWith(Suite.class)
@Suite.SuiteClasses(
		PersonServiceTest.class
)
public class IntegrationTestSuite {

}
