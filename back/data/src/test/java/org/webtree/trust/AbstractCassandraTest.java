package org.webtree.trust;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.webtree.trust.data.boot.config.CassandraConfig;
import org.webtree.trust.extension.EmbededCassandraExtension;

@ExtendWith({SpringExtension.class, EmbededCassandraExtension.class})
@SpringBootTest(classes = CassandraConfig.class)
@ActiveProfiles("cassandra-test")
public abstract class AbstractCassandraTest {
}
