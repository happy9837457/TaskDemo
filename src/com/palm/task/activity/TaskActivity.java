package com.palm.task.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.palm.task.R;
import com.palm.task.bean.req.P20007Req;
import com.palm.task.bean.resp.BaseResp;
import com.palm.task.bean.resp.P20007Resp;
import com.palm.task.task.HttpTask;

/**
 * 
 * @author weixiang.qin
 * 
 */
public class TaskActivity extends Activity implements OnClickListener {
	private Activity mActivity;
	private Button taskBtn;
	private TextView resultTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task);
		mActivity = this;
		taskBtn = (Button) findViewById(R.id.task_btn);
		resultTv = (TextView) findViewById(R.id.result_tv);
		taskBtn.setOnClickListener(this);
	}

	private void loadData() {
//		P10001Req req = new P10001Req();
//		req.userName = "lili";
//		req.password = "111111";
//		new DataTask().execute(mActivity, req);
		P20007Req req = new P20007Req();
		req.clientType = "0";
		req.appType = "android";
		new DataTask().execute(mActivity, req);
	}

	class DataTask extends HttpTask {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onSuccess(BaseResp response) {
			super.onSuccess(response);
//			P10001Resp resp = (P10001Resp) response;
//			resultTv.setText(resp.returnMsg);
			P20007Resp resp = (P20007Resp) response;
			resultTv.setText(resp.msg);
		}

		@Override
		protected void onError(int errorId) {
			super.onError(errorId);
			resultTv.setText("返回异常");
		}

		@Override
		protected void onPostExecute() {
			super.onPostExecute();
		}

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == taskBtn.getId()) {
			resultTv.setText("");
			loadData();
		}
	}
}
