package com.mygarden.multiPhaseTask;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.content.Context;
import android.util.Log;

import com.mygarden.multiPhaseTask.business.IResponse;

public abstract class MultiPhaseTask implements IResponse<String> {

	protected final static String TAG = MultiPhaseTask.class.getSimpleName();

	protected final static int PHASE_BASE = 0x1;

	private int currPhase = -1;

	private ITaskCallback callback;

	private static Executor mExecutor;

	protected Context mContext;

	public int getPhase() {
		return currPhase;
	}

	public void setPhase(int phase) {
		this.currPhase = phase;
	}

	public MultiPhaseTask(Context context) {
		mContext = context;
		initThreadPool();
	}

	private synchronized void initThreadPool() {
		if (mExecutor == null) {
			mExecutor = Executors.newFixedThreadPool(1);
		}
	}

	abstract void onError(String errCode, String errorMsg);

	abstract void onSuccess(String content);

	public void execute(ITaskCallback callback) {
		this.callback = callback;
		nextPhase(getPhase());
	}

	protected abstract void onTask(int phase);

	protected void nextPhase(int phase) {
		currPhase = phase;
		Log.d(TAG, " next phase " + getPhase());
		mExecutor.execute(new Runnable() {

			@Override
			public void run() {
				onTask(currPhase);
				Log.d(TAG, " do phase " + getPhase());
			}
		});
	}

	protected void notifyError(String errMsg) {
		if (callback != null) {
			callback.onError(getPhase(), errMsg);
		}
	}

	protected void notifySuccess(String msg) {
		if (callback != null) {
			callback.onSuccess(msg);
		}
	}

	public interface ITaskCallback {

		void onSuccess(String msg);

		void onError(int phase, String errMsg);

	}

	@Override
	public void onSuccessed(String data) {

	}

	@Override
	public void onFailed(final String code, final String errMsg) {
		mExecutor.execute(new Runnable() {

			@Override
			public void run() {
				onError(code, errMsg);
			}
		});

	}

	@Override
	public String asObject(final String rawData) {
		Log.d(TAG, " asObject " + rawData);
		mExecutor.execute(new Runnable() {

			@Override
			public void run() {
				onSuccess(rawData);
			}
		});
		return rawData;
	}
}
