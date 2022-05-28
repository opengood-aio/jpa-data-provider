package io.opengood.data.jpa.provider

import data.MatchAllDataProviderTestInput
import io.opengood.data.jpa.provider.support.AbstractDataProviderTest
import org.springframework.beans.factory.annotation.Autowired

class MatchAllDataProviderTest(@Autowired override val testInput: MatchAllDataProviderTestInput) :
    AbstractDataProviderTest()
