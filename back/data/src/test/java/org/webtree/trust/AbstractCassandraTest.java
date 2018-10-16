package org.webtree.trust;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.webtree.trust.data.boot.config.CassandraConfig;
import org.webtree.trust.rule.CassandraTestRule;
import org.webtree.trust.rule.ClearDBRule;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CassandraConfig.class)
@ActiveProfiles("cassandra-test")
public abstract class AbstractCassandraTest {

    @ClassRule
    public static CassandraTestRule cassandraTestRule = new CassandraTestRule();
    @Rule
    public ClearDBRule clearDBRule = new ClearDBRule();
}

