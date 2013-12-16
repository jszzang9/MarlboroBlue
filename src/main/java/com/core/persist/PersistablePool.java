package com.core.persist;

import java.util.HashMap;
import java.util.Map;

import com.core.persist.PersistableFactory.PersistType;
import com.core.persist.base.Persistable;
import com.exception.DcpException;

public class PersistablePool {
	private static PersistablePool persistablePool;
	private Map<PersistType, Persistable> persistableMap = new HashMap<PersistableFactory.PersistType, Persistable>();
	public static PersistablePool getInstance() throws DcpException {
		if(persistablePool == null) {
			persistablePool = new PersistablePool();
		}
		return persistablePool;
	}

	private PersistablePool() throws DcpException {
		for (PersistType persistType : PersistType.values()) {
			persistableMap.put(persistType, PersistableFactory.create(persistType));
		}
	}

	public Persistable getPersistable(PersistType persistType) {
		return persistableMap.get(persistType);
	}
}
