package io.opengood.data.jpa.provider

import org.springframework.beans.factory.annotation.Autowired
import test.data.MatchAnyDataProviderTestInput

class MatchAnyDataProviderTest(@Autowired override val testInput: MatchAnyDataProviderTestInput) :
    AbstractDataProviderTest()
