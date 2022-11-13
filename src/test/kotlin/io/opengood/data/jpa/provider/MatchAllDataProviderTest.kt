package io.opengood.data.jpa.provider

import org.springframework.beans.factory.annotation.Autowired
import test.data.MatchAllDataProviderTestInput

class MatchAllDataProviderTest(@Autowired override val testInput: MatchAllDataProviderTestInput) :
    AbstractDataProviderTest()
