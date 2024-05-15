package com.gpt.akiradart.captcha;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.time.Duration;
import org.springframework.stereotype.Service;

@Service("reCaptchaAttemptService")
public class ReCaptchaAttemptService {
  private final int MAX_ATTEMPT = 4;

  private final LoadingCache<String, Integer> attemptsCache;

  public ReCaptchaAttemptService() {
    attemptsCache = CacheBuilder.newBuilder()
        .expireAfterWrite(Duration.ofHours(4))
        .build(new CacheLoader<>() {
          @Override
          public Integer load(final String key) {
            return 0;
          }
        });
  }

  public void reCaptchaSucceeded(final String key) {
    attemptsCache.invalidate(key);
  }

  public void reCaptchaFailed(final String key) {
    int attempts = attemptsCache.getUnchecked(key);
    attempts++;
    attemptsCache.put(key, attempts);
  }

  public boolean isBlocked(final String key) {
    return attemptsCache.getUnchecked(key) >= MAX_ATTEMPT;
  }
}
