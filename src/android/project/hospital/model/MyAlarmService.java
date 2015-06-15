package android.project.hospital.model;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.project.hospital.MainActivity;
import android.project.hospital.R;

public class MyAlarmService extends Service {

	private NotificationManager mManager;

	@Override
	public IBinder onBind(Intent arg0) { // TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@SuppressWarnings({ "static-access", "deprecation" })
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Bundle bundle = intent.getExtras();
		mManager = (NotificationManager) this.getApplicationContext()
				.getSystemService(
						this.getApplicationContext().NOTIFICATION_SERVICE);
		Intent intent1 = new Intent(this.getApplicationContext(),
				MainActivity.class);
		Bundle bundle1 = new Bundle();

		bundle1.putInt("Fragment",bundle.getInt("Fragment"));
		
		intent1.putExtras(bundle1);
		
		Notification notification = new Notification(R.drawable.ic_appointment,
				"This is a test message!", System.currentTimeMillis());
		intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pendingNotificationIntent = PendingIntent.getActivity(
				this.getApplicationContext(), 0, intent1,
				PendingIntent.FLAG_UPDATE_CURRENT);
		
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.setLatestEventInfo(this.getApplicationContext(),
				bundle.getString("Title"), bundle.getString("Mess"),
				pendingNotificationIntent);
		mManager.notify(bundle.getInt("code"), notification);
		try {
            Uri notification_uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification_uri);
            r.play();
        } catch (Exception e) {}
	}

	@Override
	public void onDestroy() { // TODO Auto-generated method stub
								// super.onDestroy(); }
	}

}
