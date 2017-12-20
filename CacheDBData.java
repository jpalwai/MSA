package com.msa.cache;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import com.msa.dto.MemberInfo;

public interface CacheDBData{
	
	public Map<String, List<MemberInfo>> getkmartMembers() throws SQLException;

	public Map<String, List<MemberInfo>> getSearsMembers() throws SQLException;
}
