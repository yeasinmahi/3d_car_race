// Copyright:           2015, anisbulbul
// Home page:           http://www.codecanyon.net/user/anisbulbul

package com.anisbulbul.car.race;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.anisbulbul.car.race.assets.GameAssets;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;

public class MainActivity extends AndroidApplication implements
		GameHelper.GameHelperListener, ActionResolver {

	protected AdView adViewMain;
	private GameHelper gameHelper;
	protected View gameView;
	AdView adView;
	RelativeLayout layout;
	private InterstitialAd interstitialAd;

	private static final String AD_UNIT_ID_BANNER = "ca-app-pub-8606268298440779/7190429247";
	private static final String AD_UNIT_ID_INTERSTITIAL = "ca-app-pub-8606268298440779/5713696049";

	private static final String MORE_APPS_URL = "http://codecanyon.net/user/anisbulbul/portfolio";
	private static boolean IS_ADMOB_ACTIVE = true;
	private static boolean DEBUG = false;
	private static boolean IS_PLAY_SERVICE_ACTIVE = true;
	private static String GOOGLE_PLAY_URL;
	private static String FACEBOOK_SHARE_URL;
	private static String TWITTER_SHARE_URL;
	private static String GOOGLE_PLUS_SHARE_URL;
	private static String BLOG_URL;

	@Override
	public String[] getAchievmentIDs() {
		String achievmentIDs[] = {
				getResources().getString(R.string.achievement_bigginer),
				getResources().getString(R.string.achievement_starter_good),
				getResources().getString(R.string.achievement_race_cup_2),
				getResources().getString(R.string.achievement_after_death),
				getResources().getString(R.string.achievement_in_the_last_station) };

		return achievmentIDs;
	}

	private AdView createAdView() {
		this.adViewMain = new AdView(this);
		this.adViewMain.setAdSize(AdSize.SMART_BANNER);
		this.adViewMain.setAdUnitId(AD_UNIT_ID_BANNER);
		this.adViewMain.setId(12345);
		LayoutParams var1 = new LayoutParams(-1, -2);
		var1.addRule(12, -1);
		var1.addRule(14, -1);
		this.adViewMain.setLayoutParams(var1);
		this.adViewMain.setBackgroundColor(-16777216);
		return this.adViewMain;
	}

	private View createGameView(AndroidApplicationConfiguration var1) {
		this.gameView = this.initializeForView(new CarRace3D(this), var1);
		LayoutParams var2 = new LayoutParams(-1, -2);
		var2.addRule(10, -1);
		var2.addRule(14, -1);
		var2.addRule(2, this.adViewMain.getId());
		this.gameView.setLayoutParams(var2);
		return this.gameView;
	}

	private void startAdvertising(AdView var1) {
		var1.loadAd((new AdRequest.Builder()).build());
	}

	@Override
	public void faceBookPost(String fbMessage) {

		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent
				.putExtra(
						Intent.EXTRA_TEXT,
						"https://facebook.com/share?url=https://play.google.com/store/apps/details?id="+getPackageName()+"&title="+fbMessage);
		sendIntent.setType("text/plain");
		this.startActivity(sendIntent);

	}
	
	@Override
	public void googlePlusPost(String gplusMessage) {

		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent
				.putExtra(
						Intent.EXTRA_TEXT,
						"https://plus.google.com/share?url=https://play.google.com/store/apps/details?id="+getPackageName()+"&title="+gplusMessage);
		sendIntent.setType("text/plain");
		this.startActivity(sendIntent);

	}

	@Override
	public void twitterPost(String twMessage) {

		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent
				.putExtra(
						Intent.EXTRA_TEXT,
						"https://twitter.com/intent/tweet?url=https://play.google.com/store/apps/details?id="+getPackageName()+"&related=codecanyon.net&text=i got high score"+twMessage);
		sendIntent.setType("text/plain");
		this.startActivity(sendIntent);

	}
	
	@Override
	public void getAchievementsGPGS() {
		if (this.gameHelper.isSignedIn()) {
			this.startActivityForResult(Games.Achievements
					.getAchievementsIntent(this.gameHelper.getApiClient()), 101);
		} else if (!this.gameHelper.isConnecting()) {
			this.loginGPGS();
			return;
		}

	}
	

	@Override
	public void getLeaderboardHighScoreGPGS() {
		if (this.gameHelper.isSignedIn()) {
			this.startActivityForResult(
					Games.Leaderboards.getLeaderboardIntent(
							this.gameHelper.getApiClient(),
							this.getString(R.string.leaderboard_high_score_race)),
					100);
		} else if (!this.gameHelper.isConnecting()) {
			this.loginGPGS();
			return;
		}

	}

	
	@Override
	public void getLeaderboardTrophyGPGS() {
		if (this.gameHelper.isSignedIn()) {
			this.startActivityForResult(
					Games.Leaderboards.getLeaderboardIntent(
							this.gameHelper.getApiClient(),
							this.getString(R.string.leaderboard_race_trophy)),
					100);
		} else if (!this.gameHelper.isConnecting()) {
			this.loginGPGS();
			return;
		}

	}

	
	@Override
	public void getAllLeaderboardGPGS() {
		if (this.gameHelper.isSignedIn()) {
			this.startActivityForResult(Games.Leaderboards
					.getAllLeaderboardsIntent(this.gameHelper.getApiClient()),
					1);
		} else if (!this.gameHelper.isConnecting()) {
			this.loginGPGS();
			return;
		}

	}

	@Override
	public boolean getSignedInGPGS() {
		return this.gameHelper.isSignedIn();
	}

	@Override
	public void loginGPGS() {
		if(IS_PLAY_SERVICE_ACTIVE){
			try {
				this.runOnUiThread(new Runnable() {
					public void run() {
						MainActivity.this.gameHelper.beginUserInitiatedSignIn();
					}
				});
			} catch (Exception var2) {
				;
			}
		}
	}

	public void onActivityResult(int var1, int var2, Intent var3) {
		super.onActivityResult(var1, var2, var3);
		this.gameHelper.onActivityResult(var1, var2, var3);
	}

	public void onBackPressed() {
		
		GameAssets.isGamePause = true;
		if (GameAssets.isGameSound) {
			GameAssets.raceMusic.pause();
			GameAssets.accidentMusic.pause();
			GameAssets.moveMusic.pause();
		}
		
		final Dialog dialog = new Dialog(this);

		dialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
		dialog.setTitle(R.string.app_name);

		ScrollView scrollView = new ScrollView(MainActivity.this);

		LinearLayout dialogLayout = new LinearLayout(this);
		dialogLayout.setOrientation(LinearLayout.VERTICAL);

		TextView textView = new TextView(this);
		textView.setText(R.string.back_option_text);
		textView.setPadding(4, 0, 4, 10);
		dialogLayout.addView(textView);

		Button resumeButton = new Button(this);
		resumeButton.setText("Resume");
		resumeButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
				GameAssets.isGamePause = false;
			}
		});
		dialogLayout.addView(resumeButton);

		Button moreGamesApps = new Button(this);
		moreGamesApps.setText("More Games & Apps");
		moreGamesApps.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(Intent.ACTION_VIEW, Uri
						.parse(MORE_APPS_URL)));
				dialog.dismiss();
				GameAssets.isGamePause = false;
			}
		});
		dialogLayout.addView(moreGamesApps);

		Button rateButton = new Button(this);
		rateButton.setText("Rate Game");
		rateButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(Intent.ACTION_VIEW, Uri
						.parse(GOOGLE_PLAY_URL)));
				dialog.dismiss();
				GameAssets.isGamePause = false;
			}
		});
		dialogLayout.addView(rateButton);

		Button facebookButton = new Button(this);
		facebookButton.setText("Facebook");
		facebookButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(Intent.ACTION_VIEW, Uri
						.parse(FACEBOOK_SHARE_URL)));
				dialog.dismiss();
				GameAssets.isGamePause = false;
			}
		});
		dialogLayout.addView(facebookButton);

		Button twitterButton = new Button(this);
		twitterButton.setText("Twitter");
		twitterButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(Intent.ACTION_VIEW, Uri
						.parse(TWITTER_SHARE_URL)));
				dialog.dismiss();
				GameAssets.isGamePause = false;
			}
		});
		dialogLayout.addView(twitterButton);

		Button googlePlusButton = new Button(this);
		googlePlusButton.setText("Google Plus");
		googlePlusButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(Intent.ACTION_VIEW, Uri
						.parse(GOOGLE_PLUS_SHARE_URL)));
				dialog.dismiss();
				GameAssets.isGamePause = false;
			}
		});
		dialogLayout.addView(googlePlusButton);

		Button blogButton = new Button(this);
		blogButton.setText("Blog");
		blogButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(Intent.ACTION_VIEW, Uri
						.parse(BLOG_URL)));
				dialog.dismiss();
				GameAssets.isGamePause = false;
			}
		});
		dialogLayout.addView(blogButton);

		Button quitButton = new Button(this);
		quitButton.setText("Quit");
		quitButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		dialogLayout.addView(quitButton);

		scrollView.addView(dialogLayout);
		dialog.setContentView(scrollView);
		dialog.show();
		dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
				R.drawable.icon);

	}

	public void onCreate(Bundle var1) {
		super.onCreate(var1);
		this.getWindow().addFlags(128);

		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		cfg.useGL20 = false;
		cfg.useAccelerometer = true;
		cfg.useCompass = false;

		FACEBOOK_SHARE_URL = "https://www.facebook.com/sharer/sharer.php?display=popup&u=http%3A%2F%2Fcodecanyon.net%2Fitem%2Frecorder-and-equalizer-player-with-effect%2F9904887%3Futm_source%3Dsharefb";
		GOOGLE_PLAY_URL = "https://play.google.com/store/apps/developer?id="
				+ getPackageName();
		TWITTER_SHARE_URL = "https://twitter.com/intent/tweet?text=Check+out+%27Recorder+and+Equalizer+Player+with+Effect%27+on+%23EnvatoMarket+%23codecanyon&url=http%3A%2F%2Fcodecanyon.net%2Fitem%2Frecorder-and-equalizer-player-with-effect%2F9904887%3Futm_source%3Dsharetw";
		GOOGLE_PLUS_SHARE_URL = "https://plus.google.com/share?url=http%3A%2F%2Fcodecanyon.net%2Fitem%2Frecorder-and-equalizer-player-with-effect%2F9904887%3Futm_source%3Dsharegp";
		BLOG_URL = "http://anisbulbul.blogspot.com/";

		this.requestWindowFeature(1);
		this.getWindow().setFlags(1024, 1024);
		this.getWindow().clearFlags(2048);

		layout = new RelativeLayout(this);
		layout.setLayoutParams(new LayoutParams(-1, -1));

		adView = this.createAdView();
		// layout.addView(adView);
		layout.addView(this.createGameView(cfg));

		this.setContentView(layout);
		this.startAdvertising(adView);

		this.interstitialAd = new InterstitialAd(this);
		this.interstitialAd.setAdUnitId(AD_UNIT_ID_INTERSTITIAL);
		interstitialAd.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				if(DEBUG){
					Toast.makeText(getApplicationContext(),
							"Finished Loading Interstitial", Toast.LENGTH_SHORT)
							.show();
				}
			}

			@Override
			public void onAdClosed() {
				if(DEBUG){
					Toast.makeText(getApplicationContext(), "Closed Interstitial",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		if (this.gameHelper == null) {
			this.gameHelper = new GameHelper(this, 1);
			this.gameHelper.enableDebugLog(true);
		}
		
		this.gameHelper.setup(this);
		
	}
	
	@Override
	public void onSignInFailed() {
	}

	@Override
	public void onSignInSucceeded() {
	}

	@Override
	public void onStart() {
		super.onStart();
		if(IS_PLAY_SERVICE_ACTIVE){
			this.gameHelper.onStart(this);			
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		if(IS_PLAY_SERVICE_ACTIVE){
			this.gameHelper.onStop();			
		}
	}

	@Override
	public void openUri(String uri) {
		this.startActivity(new Intent("android.intent.action.VIEW", Uri
				.parse(uri)));
	}

	@Override
	public void quitApplication() {

	}
	

	@Override
	public void submitScoreGPGS(long score) {

		if(this.gameHelper.isSignedIn()){
			Games.Leaderboards.submitScore(this.gameHelper.getApiClient(),
					this.getString(R.string.leaderboard_high_score_race), score);		
		} else if (!this.gameHelper.isConnecting()) {
			this.loginGPGS();
			return;
		}
	}

	@Override
	public void submitTrophyGPGS(long score) {

		if(this.gameHelper.isSignedIn()){
			Games.Leaderboards.submitScore(this.gameHelper.getApiClient(),
					this.getString(R.string.leaderboard_race_trophy), score);		
		} else if (!this.gameHelper.isConnecting()) {
			this.loginGPGS();
			return;
		}
		
	}
	
	
	@Override
	public void unlockAchievementGPGS(String achievementId) {
		if(this.gameHelper.isSignedIn()){
			Games.Achievements
			.unlock(this.gameHelper.getApiClient(), achievementId);			
		} else if (!this.gameHelper.isConnecting()) {
			this.loginGPGS();
			return;
		}
	}

	@Override
	public void showRated() {

		try {
			startActivity(new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://play.google.com/store/apps/details?id="
							+ getPackageName())));
		} catch (ActivityNotFoundException e) {

		}

	}

	@Override
	public void showShare() {

		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, " Brain Puzzle Game "
				+ " Visit: http://play.google.com/store/apps/details?id="
				+ getPackageName());
		sendIntent.setType("text/plain");
		startActivity(sendIntent);

	}

	public void addLayout() {
		try {
			runOnUiThread(new Runnable() {
				public void run() {
					if (interstitialAd.isLoaded()) {
						layout.addView(adView);
					}
				}
			});
		} catch (Exception e) {
		}
	}

	@Override
	public void showOrLoadInterstital() {
		if (IS_ADMOB_ACTIVE) {

			try {
				runOnUiThread(new Runnable() {

					public void run() {

						addLayout();

						if (interstitialAd.isLoaded()) {

							interstitialAd.show();

							if(DEBUG){
								Toast.makeText(getApplicationContext(),
										"Showing Interstitial", Toast.LENGTH_SHORT)
										.show();
							}
							
						} else {
							AdRequest interstitialRequest = new AdRequest.Builder()
									.build();
							interstitialAd.loadAd(interstitialRequest);
							
							if(DEBUG){
								Toast.makeText(getApplicationContext(),
										"Loading Interstitial", Toast.LENGTH_SHORT)
										.show();
							}
							
						}
					}
				});
			} catch (Exception e) {
			}

		}
	}

}
