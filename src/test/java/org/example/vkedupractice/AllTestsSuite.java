package org.example.vkedupractice;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("org.example")
public class AllTestsSuite {
    // This suite will automatically run all tests in the org.example.vkedupractice package and its subpackages
}
