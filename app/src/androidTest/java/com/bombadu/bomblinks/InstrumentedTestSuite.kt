package com.bombadu.bomblinks

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses (
    MainActivityTest::class,
    AddLinkActivityTest::class
)
class InstrumentedTestSuite {
}