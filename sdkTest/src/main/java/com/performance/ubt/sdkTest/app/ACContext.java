/*
 * Copyright (c) 2008-2016 UBT Corporation.  All rights reserved.  Redistribution,
 *  modification, and use in source and binary forms are not permitted unless otherwise authorized by UBT.
 *
 */

package com.performance.ubt.sdkTest.app;

import android.content.Context;
import android.text.TextUtils;

import com.ubtechinc.framework.fs.DirectoryManager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author paul.zhang@ubtrobot.com
 * @date 2016/8/3
 * @Description
 * @modifier
 * @modify_time
 */


public class ACContext extends ServiceContext {

	private final static String GC_ROOT_FOLDER = "alpha2factory";
	public static final String DIR_MANAGER = "dir";
	private static String ua = null;

	public static boolean initInstance(Context context) {
		if (_instance == null) {
			ACContext gcContext = new ACContext(context);

			_instance = gcContext;
			return gcContext.init();
		}

		return true;
	}

	private Map<String, Object> objsMap;
	DirectoryManager mDirectoryManager = null;

	public ACContext(Context context) {
		super(context);
		objsMap = new HashMap<String, Object>();
	}

	private boolean init() {
		DirectoryManager dm = new DirectoryManager(new ACDirectoryContext(getApplicationContext(), GC_ROOT_FOLDER));
		boolean ret = dm.buildAndClean();
		if (!ret) {
			return false;
		}

		registerSystemObject(DIR_MANAGER, dm);
		mDirectoryManager = dm;
		return ret;
	}

	public static DirectoryManager getDirectoryManager() {
		if (_instance == null) return null;

		return ((ACContext) _instance).mDirectoryManager;
	}

	public static File getDirectory(DirType type) {
		DirectoryManager manager = getDirectoryManager();
		if (manager == null) return null;

		return manager.getDir(type.value());
	}

	public static String getDirectoryPath(DirType type) {
		DirectoryManager manager = getDirectoryManager();
		if (manager == null) return null;
		return manager.getDirPath(type.value());
	}

	public static void cleanPath(DirType type) {
		DirectoryManager manager = getDirectoryManager();
		if (manager == null) return;
		manager.cleanCache(getDirectory(type), 0);
	}

	@Override
	public void registerSystemObject(String name, Object obj) {
		objsMap.put(name, obj);
	}

	@Override
	public Object getSystemObject(String name) {
		return objsMap.get(name);
	}


	public static String getUA() {
		if (TextUtils.isEmpty(ua)) {
			ua = "alpha2factory";
		}
		return ua;
	}

}
