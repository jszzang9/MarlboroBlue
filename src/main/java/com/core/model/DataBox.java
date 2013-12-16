package com.core.model;

import com.core.persist.PersistablePool;
import com.exception.DcpException;


public abstract class DataBox {
	protected PersistablePool persistPool = null;

	public DataBox() throws DcpException {
		persistPool = PersistablePool.getInstance();
	}
}
