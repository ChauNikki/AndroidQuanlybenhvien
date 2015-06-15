package android.project.hospital.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class MyReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Intent service1 = new Intent(context, MyAlarmService.class);
		Bundle bundle = intent.getExtras();
	
		service1.putExtra("Title", bundle.getString("Title"));
		service1.putExtra("Mess", bundle.getString("Mess"));
		service1.putExtra("Code", bundle.getInt("Code"));
		service1.putExtra("Fragment", bundle.getInt("Fragment"));
		context.startService(service1);
	}
	
}
