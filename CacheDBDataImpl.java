package com.msa.cache;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.msa.constants.MSAConstants;
import com.msa.dao.MemberInfoDAO;
import com.msa.dto.MemberInfo;

@Component
public class CacheDBDataImpl implements CacheDBData{
		
	@Autowired
	private MemberInfoDAO memberInfoDAO;
	
	@Override
	@Cacheable(value="KMARTMEMBERSCACHE")
	public Map<String, List<MemberInfo>> getkmartMembers() throws SQLException {
		return this.getMembersMap(memberInfoDAO.getMembers(MSAConstants.KMART_FORMAT));
	}

	@Override
	@Cacheable(value="SEARSMEMBERSCACHE")
	public Map<String, List<MemberInfo>> getSearsMembers() throws SQLException {
		return this.getMembersMap(memberInfoDAO.getMembers(MSAConstants.SEARS_FORMAT));
	}
	
	private Map<String, List<MemberInfo>> getMembersMap(List<MemberInfo> memberInfos){
		Multimap<String, MemberInfo> sortMembers = this.sortMembers(memberInfos);
		Map<String, List<MemberInfo>> members = new HashMap<String, List<MemberInfo>>();
		if(sortMembers != null && sortMembers.size() > 0 && 
				memberInfos != null && memberInfos.size() > 0){
			for(MemberInfo memberInfo : memberInfos){
				if(memberInfo != null){
					String store = memberInfo.getStore();
					if(StringUtils.isNoneEmpty(store)){
						Collection<MemberInfo> memberInfoColl = sortMembers.get(store);
						if(memberInfoColl != null && memberInfoColl.size() > 0){
							members.put(store, (List<MemberInfo>)memberInfoColl);
						}
					}
				}
			}
		}
		return members;
	}
	
	private Multimap<String, MemberInfo> sortMembers(List<MemberInfo> memberInfos){
		Multimap<String, MemberInfo> sortedMem = ArrayListMultimap.create();
		if(memberInfos != null && memberInfos.size() > 0){
			for(MemberInfo memberInfo : memberInfos){
				if(memberInfo != null){
					String store = memberInfo.getStore();
					if(StringUtils.isNoneEmpty(store)){
						sortedMem.put(store, memberInfo);
					}
				}
			}
		}
		return sortedMem;
	}
}
