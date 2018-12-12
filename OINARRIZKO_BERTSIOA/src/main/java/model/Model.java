package model;

import java.nio.file.Path;

import persistence.Database;

public final class Model {

	private static Model mModel;
	
	private Model() {
	
		
	}
	public static Model setup(Database mDB, Path appDir) {
		if (mModel == null)
			mModel = new Model();
		
		return mModel;
	}

	public void load() {
		// TODO Auto-generated method stub
		
	}

}
