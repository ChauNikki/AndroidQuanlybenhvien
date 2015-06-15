package android.project.hospital;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.project.hospital.adapter.NavDrawerListAdapter;
import android.project.hospital.model.NavDrawerItem;
import android.project.hospital.model.SessionManager;
import android.project.hospital.model.WebService;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	final private static int DIALOG_LOGIN = 1;
	final private static int DIALOG_REGISTER = 2;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private Boolean register = false;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;

	// login dialog
	Button btnSingIn;
	Button btnSingUp;

	static boolean errored = false;
	String editTextUsername;
	String editTextPassword;
	SessionManager session;

	boolean loginStatus;

	Fragment fragment = null;

	AlertDialog alertDialog = null;
	AlertDialog errorDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		// Home
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons
				.getResourceId(0, -1)));
		// Doctor
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons
				.getResourceId(1, -1)));
		// Prescription
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons
				.getResourceId(2, -1)));
		// Appointment
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons
				.getResourceId(3, -1)));
		// Profile
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons
				.getResourceId(4, -1)));

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#4df1ff")));

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for
									// accessibility
				R.string.app_name // nav drawer close - description for
									// accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		session = new SessionManager(getApplicationContext());
		errorDialog = new AlertDialog.Builder(this).create();

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			int fragment = 0;
			try {
				Bundle bundle = getIntent().getExtras();
				fragment = bundle.getInt("Fragment");
			} catch (Exception e) {

			}
			if (fragment > 0) {
				displayView(fragment);
			} else
				displayView(0);

		}
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click

		return super.onOptionsItemSelected(item);
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	@SuppressWarnings("deprecation")
	private void displayView(int position) {
		// update the main content by replacing fragments
		if (position == 5) {
			mDrawerLayout.closeDrawer(mDrawerList);
			showDialog(DIALOG_REGISTER);
		} else if (!session.isLoggedIn()) {

			mDrawerLayout.closeDrawer(mDrawerList);
			showDialog(DIALOG_LOGIN);

		} else {
			switch (position) {
			case 0:
				fragment = new HomeFragment();
				break;
			case 1:
				fragment = new DoctorFragment();
				break;
			case 2:
				fragment = new PrescriptionFragment();
				break;
			case 3:
				fragment = new AppointmentFragment();
				break;
			case 4:
				fragment = new ProfileFragment();
				break;
			case 5:
				mDrawerLayout.closeDrawer(mDrawerList);
				showDialog(DIALOG_REGISTER);
				break;
			default:
				break;
			}
		}
		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	// dialogn login

	@SuppressLint("InflateParams")
	@Override
	protected Dialog onCreateDialog(int id) {

		AlertDialog dialogDetails = null;

		switch (id) {
		case DIALOG_LOGIN:
			LayoutInflater inflater = LayoutInflater.from(this);
			View dialogview = inflater.inflate(R.layout.dialog_sign_in, null);

			AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(this);
			dialogbuilder.setTitle("Đăng nhập");

			dialogbuilder.setView(dialogview);
			dialogDetails = dialogbuilder.create();
			break;

		case DIALOG_REGISTER:
			LayoutInflater inf = LayoutInflater.from(this);
			View view = inf.inflate(R.layout.diaglog_register, null);

			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("Đăng ký");

			dialog.setView(view);
			dialogDetails = dialog.create();
			break;
		}

		return dialogDetails;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case DIALOG_LOGIN:
			alertDialog = (AlertDialog) dialog;

			btnSingIn = (Button) alertDialog.findViewById(R.id.btnSingIn);

			final EditText userName = (EditText) alertDialog
					.findViewById(R.id.txtTaiKhoan);
			final EditText password = (EditText) alertDialog
					.findViewById(R.id.txtPassword);

			final TextView link = (TextView) alertDialog
					.findViewById(R.id.link_to_register);

			btnSingIn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					if (userName.getText().length() != 0
							&& userName.getText().toString() != "") {
						if (password.getText().length() != 0
								&& password.getText().toString() != "") {
							editTextUsername = userName.getText().toString();
							editTextPassword = password.getText().toString();
							// Create instance for AsyncCallWS
							register = false;
							AsyncCallWS task = new AsyncCallWS();
							// Call execute
							task.execute();
						}
						// If Password text control is empty
						else {
							Toast.makeText(MainActivity.this,
									"Chưa nhập mật khẩu", Toast.LENGTH_LONG)
									.show();
						}
						// If Username text control is empty
					} else {
						// statusTV.setText("Please enter Username");
						Toast.makeText(MainActivity.this,
								"Chưa nhập tài khoản", Toast.LENGTH_LONG)
								.show();
					}
				}
			});

			link.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					alertDialog.dismiss();
					register = true;
					displayView(5);
				}
			});

			break;

		case DIALOG_REGISTER:
			alertDialog = (AlertDialog) dialog;

			btnSingUp = (Button) alertDialog.findViewById(R.id.btnRegister);

			final EditText user = (EditText) alertDialog
					.findViewById(R.id.txtUser);
			final EditText pass = (EditText) alertDialog
					.findViewById(R.id.txtPass);

			btnSingUp.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					String regex = "^([a-zA-Z0-9_-]+)$";
					Pattern pattern = Pattern.compile(regex);
					Matcher matcheruser = pattern.matcher(user.getText());

					String reg = "^([a-zA-Z0-9]+)$";
					Pattern patt = Pattern.compile(reg);
					Matcher matcherpass = patt.matcher(pass.getText());

					if (user.getText().length() >= 6
							&& user.getText().toString() != ""
							&& matcheruser.matches() == true) {
						if (pass.getText().length() >= 6
								&& matcherpass.matches() == true
								&& pass.getText().toString() != "") {
							editTextUsername = user.getText().toString();
							editTextPassword = pass.getText().toString();

							register = true;
							// Create instance for AsyncCallWS
							AsyncCallWS task = new AsyncCallWS();
							// Call execute
							task.execute();

						}
						// If Password text control is empty
						else {
							Toast.makeText(
									MainActivity.this,
									"Password phải có ít nhất 6 ký tự và không được có ký tự đặc biệt",
									Toast.LENGTH_LONG).show();
						}
						// If Username text control is empty
					} else {
						// statusTV.setText("Please enter Username");
						Toast.makeText(
								MainActivity.this,
								"Username phải có ít nhất 6 ký tự và chỉ chấp nhận ký tự đặc biệt (_-).",
								Toast.LENGTH_LONG).show();
					}
				}
			});
			break;
		}
	}

	// Login
	private class AsyncCallWS extends AsyncTask<String, String, String> {

		int reg = -1;

		@Override
		protected String doInBackground(String... params) {
			// Call Web Method
			if (!register) {

				reg = WebService.invokeLoginWS(editTextUsername,
						editTextPassword, "checkLogin");
				Log.e("test", reg + "");
				
				if (reg == 1)
					loginStatus = true;
				else if (reg == 0)
					loginStatus = false;
				else
					errored = true;
				Log.e("test", errored + "");

			} else {
				try {
					reg = WebService.registerWS(editTextUsername,
							editTextPassword, "register");
				} catch (Exception e) {
					errored = true;
				}
			}
			Log.e("test", errored + "");
			return null;

		}

		@Override
		protected void onPreExecute() {
			if (!register) {
				btnSingIn.setEnabled(false);
			} else {
				btnSingUp.setEnabled(false);
			}
		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);
			// Make Progress Bar invisible
			// Error status is false
			if (!errored) {

				// Based on Boolean value returned from WebService
				if (!register) {

					if (loginStatus) {

						session.login(editTextUsername);

						alertDialog.dismiss();
						Toast.makeText(MainActivity.this,
								"Đăng nhập thành công", Toast.LENGTH_LONG)
								.show();
						displayView(0);
					} else {
						errorDialog.setTitle("Đăng nhập thất bại....");
						errorDialog
								.setMessage("Tài khoản hoặc mật khẩu không đúng");
						errorDialog.setButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										errorDialog.dismiss();
									}
								});
						errorDialog.show();
					}
				}

				// this is register
				else {
					if (reg == -1) {
						errorDialog.setTitle("Lỗi....");
						errorDialog
								.setMessage("Không thể kết nối với server! Kiểm tra lại internet....");
						errorDialog.setButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										errorDialog.dismiss();
									}
								});
						errorDialog.show();
					} else if (reg == 0) {
						Toast.makeText(MainActivity.this,
								"Tên tài khoản đã được sử dụng",
								Toast.LENGTH_LONG).show();
					} else if (reg == 1) {
						Toast.makeText(MainActivity.this, "Đăng ký thành công",
								Toast.LENGTH_LONG).show();

						session.logoutUser();
						session.login(editTextUsername);
						alertDialog.dismiss();
						displayView(4);
					}
				}

				// Error status is true
			} else {
				Log.e("test va", errored + "");
				errorDialog.setTitle("Lỗi....");
				errorDialog
						.setMessage("Không thể kết nối với server! Kiểm tra lại internet....");
				errorDialog.setButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								errorDialog.dismiss();
							}
						});
				errorDialog.show();
			}
			// Re-initialize Error Status to False
			errored = false;
			if (!register) {
				btnSingIn.setEnabled(true);
			} else {
				btnSingUp.setEnabled(true);
			}
		}

		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values[0]);
		}

	}
}
