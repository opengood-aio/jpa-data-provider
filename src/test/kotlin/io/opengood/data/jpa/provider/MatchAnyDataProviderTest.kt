package io.opengood.data.jpa.provider

import data.MatchAnyDataProviderTestInput
import io.opengood.data.jpa.provider.support.AbstractDataProviderTest
import org.springframework.beans.factory.annotation.Autowired

class MatchAnyDataProviderTest(@Autowired override val testInput: MatchAnyDataProviderTestInput) :
    AbstractDataProviderTest()
