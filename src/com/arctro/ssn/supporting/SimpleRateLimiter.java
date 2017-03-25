package com.arctro.ssn.supporting;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.arctro.ssn.supporting.exceptions.RateLimitException;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * Provides a simple method of rate limiting, through the use
 * of Guava's cache
 * @author Ben McLean
 *
 * @param <T> The key of the rate counter
 */
public class SimpleRateLimiter<T> {
	
	Cache<T, Integer> rate;
	int max;
	long duration;
	TimeUnit unit;
	
	/**
	 * Instantiates a new rate limiter
	 * @param duration The duration of the rate limit
	 * @param unit The unit of the duration
	 * @param max The maximum requests in that time
	 */
	public SimpleRateLimiter(long duration, TimeUnit unit, int max){
		rate = CacheBuilder.newBuilder()
				.expireAfterWrite(duration, unit)
				.build();
		
		this.max = max;
		this.duration = duration;
		this.unit = unit;
	}
	
	/**
	 * Checks if a key should be limited
	 * @param key The key to check
	 * @return If the key should be limited
	 */
	public boolean isLimited(T key){
		try{
			Integer count = rate.get(key, new Callable<Integer>(){
				@Override
				public Integer call() throws Exception {
					return 1;
				}
			});
			
			rate.put(key, count+1);
			
			return count > max;
		}catch(ExecutionException e){
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Throws an exception if a key is rate limited
	 * @param key The key to check
	 * @throws RateLimitException Thrown if a key is rate limited
	 */
	public void throwLimited(T key) throws RateLimitException{
		if(isLimited(key)){
			throw new RateLimitException("More than " + max + " requests have been made in the last " + duration + " " + unit.name());
		}
	}

}
