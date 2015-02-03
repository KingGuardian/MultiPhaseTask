package com.mygarden.multiPhaseTask.business;


/**
 * Define a common callback interface, which is used in executing a web-requeset operation. 
 * @author Administrator
 *
 * @param <T>
 */
public interface IResponse<T> {
    
	/**
	 * Operation success.
	 * @param data
	 */
	void onSuccessed(T result);

	void onFailed(String code, String errMsg);
	
	T asObject(String rspStr);

}

