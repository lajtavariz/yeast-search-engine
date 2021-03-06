package hu.ppke.yeast.config;

import io.github.jhipster.config.JHipsterProperties;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
@AutoConfigureAfter(value = { MetricsConfiguration.class })
@AutoConfigureBefore(value = { WebConfigurer.class, DatabaseConfiguration.class })
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(Expirations.timeToLiveExpiration(Duration.of(ehcache.getTimeToLiveSeconds(), TimeUnit.SECONDS)))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(hu.ppke.yeast.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(hu.ppke.yeast.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(hu.ppke.yeast.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(hu.ppke.yeast.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(hu.ppke.yeast.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(hu.ppke.yeast.domain.PersistentToken.class.getName(), jcacheConfiguration);
            cm.createCache(hu.ppke.yeast.domain.User.class.getName() + ".persistentTokens", jcacheConfiguration);
            cm.createCache(hu.ppke.yeast.domain.Document.class.getName(), jcacheConfiguration);
            cm.createCache(hu.ppke.yeast.domain.Index.class.getName(), jcacheConfiguration);
            cm.createCache(hu.ppke.yeast.domain.DocumentIndex.class.getName(), jcacheConfiguration);
            cm.createCache(hu.ppke.yeast.domain.DocumentIndexWeight.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
