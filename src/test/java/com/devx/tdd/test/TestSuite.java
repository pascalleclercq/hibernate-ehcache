package com.devx.tdd.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.devx.tdd.test.mock.PersonServiceMockTest;

@RunWith(Suite.class)
@Suite.SuiteClasses(
		PersonServiceMockTest.class
)
public class TestSuite {

}
