package com.arctro.ssn.supporting;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.arctro.ssn.supporting.exceptions.RateLimitException;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class SimpleRateLimiter<T> {
	
	Cache<T, Integer> rate;
	int max;
	long duration;
	TimeUnit unit;
	
	public SimpleRateLimiter(long duration, TimeUnit unit, int max){
		rate = CacheBuilder.newBuilder()
				.expireAfterWrite(duration, unit)
				.build();
		
		this.max = max;
		this.duration = duration;
		this.unit = unit;
	}
	
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
	
	public void throwLimited(T key) throws RateLimitException{
		if(isLimited(key)){
			throw new RateLimitException("More than " + max + " requests have been made in the last " + duration + " " + unit.name());
		}
	}

}
