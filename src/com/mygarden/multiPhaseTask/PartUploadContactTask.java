package com.mygarden.multiPhaseTask;

import android.content.Context;

public class PartUploadContactTask extends MultiPhaseTask {

	protected final static String TAG = PartUploadContactTask.class.getSimpleName();

	private final static int PHASE_POST_DATA = PHASE_BASE << 3;

	private final static int PHASE_SYNC_MARKID = PHASE_BASE << 4;

	private String userID;

	public PartUploadContactTask(Context context, String userID) {
		super(context);
		setPhase(PHASE_SYNC_MARKID);
		this.userID = userID;
	}

	@Override
	protected void onTask(int phase) {
		switch (phase) {
		case PHASE_POST_DATA:
			notifySuccess("");
			break;
		case PHASE_SYNC_MARKID:
			nextPhase(PHASE_POST_DATA);
			break;
		default:
			break;
		}

	}

	@Override
	void onError(String errCode, String errorMsg) {
		notifyError(errorMsg);
	}

	@Override
	void onSuccess(String content) {
		switch (getPhase()) {
		case PHASE_SYNC_MARKID:
			nextPhase(PHASE_POST_DATA);
			break;
		case PHASE_POST_DATA:
			notifySuccess("success");
			break;
		default:
			break;
		}

	}
}
