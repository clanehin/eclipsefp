package net.sf.eclipsefp.haskell.core.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllAllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for net.sf.eclipsefp.haskell.core.test");
		//$JUnit-BEGIN$
		suite.addTest(net.sf.eclipsefp.haskell.core.test.compiler.AllTests.suite());
		suite.addTest(net.sf.eclipsefp.haskell.core.test.halamo.AllTests.suite());
		suite.addTest(net.sf.eclipsefp.haskell.core.test.AllTests_Suite.suite());
		suite.addTest(net.sf.eclipsefp.haskell.core.test.project.AllTests.suite());
		//$JUnit-END$
		return suite;
	}

}
