package com.msa.cache;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import com.msa.utils.MSAUtils;

public class InitCache {
	
	private final static Logger logger = LoggerFactory.getLogger(InitCache.class);

	@Autowired
	private CacheDBData cacheDBData;
	
	@Autowired
	private CacheManager cacheManager;
	
	public void init(){
		try {
			//Loading KMART Members In Cache
			long startKmartQueryTime = System.currentTimeMillis();
			Date startKmartQueryDate = MSAUtils.convertTime(startKmartQueryTime);
			System.out.println("Loading Kmart Members In Cache Started At : " + startKmartQueryDate);
			cacheDBData.getkmartMembers();
			long endKmartQueryTime = System.currentTimeMillis();
			Date endKmartQueryDate = MSAUtils.convertTime(endKmartQueryTime);
			System.out.println("Loading Kmart Members In Cache Completed At : " + endKmartQueryDate);
			System.out.println("Kmart members query took : " + MSAUtils.getTimeDiff(startKmartQueryDate, endKmartQueryDate));
			
			//Loading SEARS Members In Cache
			long startSearsQueryTime = System.currentTimeMillis();
			Date startSearsQueryDate = MSAUtils.convertTime(startSearsQueryTime);
			System.out.println("Loading Sears Members In Cache Started At : " + startSearsQueryDate);
			cacheDBData.getSearsMembers();
			long endSearsQueryTime = System.currentTimeMillis();
			Date endSearsQueryDate = MSAUtils.convertTime(endSearsQueryTime);
			System.out.println("Loading Sears Members In Cache Completed At : " + endSearsQueryDate);
			System.out.println("Sears members query took : " + MSAUtils.getTimeDiff(startSearsQueryDate, endSearsQueryDate));
			//this.showCacheStatistics();
		}catch(SQLException ex){
			logger.error("Error loading cache data " + ex.getMessage());
		}
	}
	
	private void showCacheStatistics(){
		Collection<String> cacheNames = cacheManager.getCacheNames();
		if(cacheNames != null && cacheNames.size() > 0){
			for(String cacheName : cacheNames){
				if(StringUtils.isNotEmpty(cacheName)){
					Cache cache = cacheManager.getCache(cacheName);
					if(cache != null){
						Object nativeCache = cache.getNativeCache();
						if (nativeCache instanceof net.sf.ehcache.Ehcache) {
				        	net.sf.ehcache.Ehcache ehCache = (net.sf.ehcache.Ehcache) nativeCache;
				        	System.out.println("Cache Name " + ehCache.getName());
				        	System.out.println("Cache Size " + ehCache.getStatistics().getSize());
				        	System.out.println("Heap Size In MB " + FileUtils.byteCountToDisplaySize(ehCache.getStatistics().getLocalHeapSizeInBytes()));
						}
					}
				}
			}
		}
	}
	public void cleanUp(){}
}
