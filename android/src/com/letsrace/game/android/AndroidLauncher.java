package com.letsrace.game.android;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.OnInvitationReceivedListener;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateListener;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateListener;
import com.google.android.gms.plus.Plus;
import com.google.example.games.basegameutils.BaseGameUtils;
import com.letsrace.game.FRConstants.GameState;
import com.letsrace.game.LetsRace;
import com.letsrace.game.network.FRGoogleServices;

public class AndroidLauncher extends AndroidApplication implements
		FRGoogleServices, GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener, RoomStatusUpdateListener,
		RoomUpdateListener, OnInvitationReceivedListener {
	/*
	 * API INTEGRATION SECTION. This section contains the code that integrates
	 * the game with the Google Play game services API.
	 */
	final static String TAG = "Lets-Race!";

	// Request codes for the UIs that we show with startActivityForResult:
	final static int RC_SELECT_PLAYERS = 10000;
	final static int RC_INVITATION_INBOX = 10001;
	final static int RC_WAITING_ROOM = 10002;

	// Request code used to invoke sign in user interactions.
	private static final int RC_SIGN_IN = 9001;

	// Client used to interact with Google APIs.
	private GoogleApiClient mGoogleApiClient;

	// Are we currently resolving a connection failure?
	private boolean mResolvingConnectionFailure = false;

	// Has the user clicked the sign-in button?
	private boolean mSignInClicked = false;

	// Set to true to automatically start the sign in flow when the Activity
	// starts.
	// Set to false to require the user to click the button in order to sign in.
	private boolean mAutoStartSignInFlow = true;

	// Room ID where the currently active game is taking place; null if we're
	// not playing.
	Room room;

	// Are we playing in multiplayer mode?
	boolean mMultiplayer = false;

	// The participants in the currently active game
	ArrayList<Participant> mParticipants = null;

	// My participant ID in the currently active game
	String mMyId = null;

	// If non-null, this is the id of the invitation we received via the
	// invitation listener
	String mIncomingInvitationId = null;

	// Message handler
	FRMessageHandler messageHandler;

	LetsRace game;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Create the Google Api Client with access to Plus and Games
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API)
				.addScope(Plus.SCOPE_PLUS_LOGIN).addApi(Games.API)
				.addScope(Games.SCOPE_GAMES).build();
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		game = new LetsRace(this);
		messageHandler = new FRMessageHandler();
		initialize(game, config);
	}

	public void initiateSignIn() {
		// start the sign-in flow
		Log.d(TAG, "Sign-in button clicked");
		mSignInClicked = true;
		mGoogleApiClient.connect();
	}

	public void invitePlayers() {
		// show list of invitable players
		Intent intent = Games.RealTimeMultiplayer.getSelectOpponentsIntent(
				mGoogleApiClient, 1, 3);
		game.moveToScreen(GameState.WAIT);
		startActivityForResult(intent, RC_SELECT_PLAYERS);
	}

	public void showInvites() {
		Intent intent = Games.Invitations
				.getInvitationInboxIntent(mGoogleApiClient);
		game.moveToScreen(GameState.WAIT);
		startActivityForResult(intent, RC_INVITATION_INBOX);
	}

	public void startQuickGame() {
		// quick-start a game with 1 randomly selected opponent
		final int MIN_OPPONENTS = 1, MAX_OPPONENTS = 1;
		Bundle autoMatchCriteria = RoomConfig.createAutoMatchCriteria(
				MIN_OPPONENTS, MAX_OPPONENTS, 0);
		RoomConfig.Builder rtmConfigBuilder = RoomConfig.builder(this);
		rtmConfigBuilder.setMessageReceivedListener(messageHandler);
		rtmConfigBuilder.setRoomStatusUpdateListener(this);
		rtmConfigBuilder.setAutoMatchCriteria(autoMatchCriteria);
		game.moveToScreen(GameState.WAIT);
		Games.RealTimeMultiplayer.create(mGoogleApiClient,
				rtmConfigBuilder.build());
	}

	@Override
	public void onActivityResult(int requestCode, int responseCode,
			Intent intent) {
		super.onActivityResult(requestCode, responseCode, intent);

		switch (requestCode) {
		case RC_SELECT_PLAYERS:
			// we got the result from the "select players" UI -- ready to create
			// the room
			handleSelectPlayersResult(responseCode, intent);
			break;
		case RC_INVITATION_INBOX:
			// we got the result from the "select invitation" UI (invitation
			// inbox). We're
			// ready to accept the selected invitation:
			handleInvitationInboxResult(responseCode, intent);
			break;
		case RC_WAITING_ROOM:
			// we got the result from the "waiting room" UI.
			if (responseCode == Activity.RESULT_OK) {
				// ready to start playing
				Log.d(TAG, "Starting game (waiting room returned OK).");
				game.moveToScreen(GameState.GAME_SCREEN);
			} else if (responseCode == GamesActivityResultCodes.RESULT_LEFT_ROOM) {
				// player indicated that they want to leave the room
				leaveRoom();
				game.moveToScreen(GameState.MENU);
			} else if (responseCode == Activity.RESULT_CANCELED) {
				// Dialog was cancelled (user pressed back key, for instance).
				// In our game,
				// this means leaving the room too. In more elaborate games,
				// this could mean
				// something else (like minimizing the waiting room UI).
				leaveRoom();
				game.moveToScreen(GameState.MENU);
			}
			break;
		case RC_SIGN_IN:
			Log.d(TAG,
					"onActivityResult with requestCode == RC_SIGN_IN, responseCode="
							+ responseCode + ", intent=" + intent);
			mSignInClicked = false;
			mResolvingConnectionFailure = false;
			if (responseCode == RESULT_OK) {
				mGoogleApiClient.connect();
			} else {
				OnClickListener listener = new OnClickListener() {
					Activity parentActivity = AndroidLauncher.this;

					@Override
					public void onClick(DialogInterface dialog, int which) {
						parentActivity.finish();
					}
				};
				BaseGameUtils.showActivityResultError(this, listener,
						requestCode, responseCode, R.string.sign_in_error,
						R.string.other_error);
			}
			break;
		}
		super.onActivityResult(requestCode, responseCode, intent);
	}

	// Handle the result of the "Select players UI" we launched when the user
	// clicked the
	// "Invite friends" button. We react by creating a room with those players.
	private void handleSelectPlayersResult(int response, Intent data) {
		if (response != Activity.RESULT_OK) {
			Log.w(TAG, "*** select players UI cancelled, " + response);
			game.moveToScreen(GameState.MENU);
			return;
		}

		Log.d(TAG, "Select players UI succeeded.");

		// get the invitee list
		final ArrayList<String> invitees = data
				.getStringArrayListExtra(Games.EXTRA_PLAYER_IDS);
		Log.d(TAG, "Invitee count: " + invitees.size());

		// get the automatch criteria
		Bundle autoMatchCriteria = null;
		int minAutoMatchPlayers = data.getIntExtra(
				Multiplayer.EXTRA_MIN_AUTOMATCH_PLAYERS, 0);
		int maxAutoMatchPlayers = data.getIntExtra(
				Multiplayer.EXTRA_MAX_AUTOMATCH_PLAYERS, 0);
		if (minAutoMatchPlayers > 0 || maxAutoMatchPlayers > 0) {
			autoMatchCriteria = RoomConfig.createAutoMatchCriteria(
					minAutoMatchPlayers, maxAutoMatchPlayers, 0);
			Log.d(TAG, "Automatch criteria: " + autoMatchCriteria);
		}

		// create the room
		Log.d(TAG, "Creating room...");
		RoomConfig.Builder rtmConfigBuilder = RoomConfig.builder(this);
		rtmConfigBuilder.addPlayersToInvite(invitees);
		rtmConfigBuilder.setMessageReceivedListener(messageHandler);
		rtmConfigBuilder.setRoomStatusUpdateListener(this);
		if (autoMatchCriteria != null) {
			rtmConfigBuilder.setAutoMatchCriteria(autoMatchCriteria);
		}
		game.moveToScreen(GameState.WAIT);
		Games.RealTimeMultiplayer.create(mGoogleApiClient,
				rtmConfigBuilder.build());
		Log.d(TAG, "Room created, waiting for it to be ready...");
	}

	// Handle the result of the invitation inbox UI, where the player can pick
	// an invitation
	// to accept. We react by accepting the selected invitation, if any.
	private void handleInvitationInboxResult(int response, Intent data) {
		if (response != Activity.RESULT_OK) {
			Log.w(TAG, "*** invitation inbox UI cancelled, " + response);
			game.moveToScreen(GameState.MENU);
			return;
		}

		Log.d(TAG, "Invitation inbox UI succeeded.");
		Invitation inv = data.getExtras().getParcelable(
				Multiplayer.EXTRA_INVITATION);

		// accept invitation
		acceptInviteToRoom(inv.getInvitationId());
	}

	// Accept the given invitation.
	void acceptInviteToRoom(String invId) {
		// accept the invitation
		Log.d(TAG, "Accepting invitation: " + invId);
		RoomConfig.Builder roomConfigBuilder = RoomConfig.builder(this);
		roomConfigBuilder.setInvitationIdToAccept(invId)
				.setMessageReceivedListener(messageHandler)
				.setRoomStatusUpdateListener(this);
		game.moveToScreen(GameState.WAIT);
		Games.RealTimeMultiplayer.join(mGoogleApiClient,
				roomConfigBuilder.build());
	}

	// Activity is going to the background. We have to leave the current room.
	@Override
	public void onStop() {
		Log.d(TAG, "**** got onStop");
		// if we're in a room, leave it.
		leaveRoom();
		super.onStop();
	}

	// Activity just got to the foreground. We switch to the wait screen because
	// we will now
	// go through the sign-in flow (remember that, yes, every time the Activity
	// comes back to the
	// foreground we go through the sign-in flow -- but if the user is already
	// authenticated,
	// this flow simply succeeds and is imperceptible).
	@Override
	public void onStart() {
		// TODO: Implement logic
		// game.goToWaitScreen();
		// if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
		// Log.w(TAG, "GameHelper: client was already connected on onStart()");
		// } else {
		// Log.d(TAG, "Connecting client.");
		// mGoogleApiClient.connect();
		// }
		super.onStart();
	}

	// Handle back key to make sure we cleanly leave a game if we are in the
	// middle of one
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent e) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& game.gameState == GameState.GAME_SCREEN) {
			leaveRoom();
			return true;
		}
		return super.onKeyDown(keyCode, e);
	}

	// Leave the room.
	void leaveRoom() {
		Log.d(TAG, "Leaving room.");
		if (room != null) {
			Games.RealTimeMultiplayer.leave(mGoogleApiClient, this,
					room.getRoomId());
			room = null;
		}
	}

	// Show the waiting room UI to track the progress of other players as they
	// enter the
	// room and get connected.
	void showWaitingRoom(Room room) {
		// minimum number of players required for our game
		// For simplicity, we require everyone to join the game before we start
		// it
		// (this is signaled by Integer.MAX_VALUE).
		final int MIN_PLAYERS = Integer.MAX_VALUE;
		Intent i = Games.RealTimeMultiplayer.getWaitingRoomIntent(
				mGoogleApiClient, room, MIN_PLAYERS);

		// show waiting room UI
		startActivityForResult(i, RC_WAITING_ROOM);
	}

	// Called when we get an invitation to play a game. We react by showing that
	// to the user.
	@Override
	public void onInvitationReceived(Invitation invitation) {
		// We got an invitation to play a game! So, store it in
		// mIncomingInvitationId
		// and show the popup on the screen.
		// TODO: implement this
	}

	@Override
	public void onInvitationRemoved(String invitationId) {
		if (mIncomingInvitationId.equals(invitationId)) {
			mIncomingInvitationId = null;
		}
		// TODO: implement this
	}

	/*
	 * CALLBACKS SECTION. This section shows how we implement the several games
	 * API callbacks.
	 */

	@Override
	public void onConnected(Bundle connectionHint) {
		Log.d(TAG, "onConnected() called. Sign in successful!");
		Log.d(TAG, "Sign-in succeeded.");
		// register listener so we are notified if we receive an invitation to
		// play
		// while we are in the game
		Games.Invitations.registerInvitationListener(mGoogleApiClient, this);
		if (connectionHint != null) {
			Log.d(TAG,
					"onConnected: connection hint provided. Checking for invite.");
			Invitation inv = connectionHint
					.getParcelable(Multiplayer.EXTRA_INVITATION);
			if (inv != null && inv.getInvitationId() != null) {
				// retrieve and cache the invitation ID
				Log.d(TAG, "onConnected: connection hint has a room invite!");
				acceptInviteToRoom(inv.getInvitationId());
				return;
			}
		}
		// TODO: Pop up already signed in
		game.moveToScreen(GameState.MENU);
	}

	@Override
	public void onConnectionSuspended(int i) {
		Log.d(TAG, "onConnectionSuspended() called. Trying to reconnect.");
		mGoogleApiClient.connect();
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		Log.d(TAG, "onConnectionFailed() called, result: " + connectionResult);

		if (mResolvingConnectionFailure) {
			Log.d(TAG,
					"onConnectionFailed() ignoring connection failure; already resolving.");
			return;
		}

		if (mSignInClicked || mAutoStartSignInFlow) {
			mAutoStartSignInFlow = false;
			mSignInClicked = false;
			mResolvingConnectionFailure = BaseGameUtils
					.resolveConnectionFailure(this, mGoogleApiClient,
							connectionResult, RC_SIGN_IN,
							getString(R.string.other_error));
		}
		// TODO: Quit game
	}

	// Called when we are connected to the room. We're not ready to play yet!
	// (maybe not everybody
	// is connected yet).
	@Override
	public void onConnectedToRoom(Room room) {
		Log.d(TAG, "onConnectedToRoom.");

		// get room ID, participants and my ID:
		this.room = room;
		mParticipants = room.getParticipants();
		mMyId = room.getParticipantId(Games.Players
				.getCurrentPlayerId(mGoogleApiClient));

		// print out the list of participants (for debug purposes)
		Log.d(TAG, "Room ID: " + room.getRoomId());
		Log.d(TAG, "My ID " + mMyId);
		Log.d(TAG, "<< CONNECTED TO ROOM >>");
	}

	// Called when we've successfully left the room (this happens a result of
	// voluntarily leaving
	// via a call to leaveRoom(). If we get disconnected, we get
	// onDisconnectedFromRoom()).
	@Override
	public void onLeftRoom(int statusCode, String roomId) {
		// we have left the room; return to main screen.
		Log.d(TAG, "onLeftRoom, code " + statusCode);
		game.moveToScreen(GameState.MENU);
	}

	// Called when we get disconnected from the room. We return to the main
	// screen.
	@Override
	public void onDisconnectedFromRoom(Room room) {
		room = null;
		showGameError();
	}

	// Show error message about game being cancelled and return to main screen.
	void showGameError() {
		OnClickListener listener = new OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
			}
		};
		BaseGameUtils.makeSimpleDialog(this, getString(R.string.other_error),
				listener);
	}

	// Called when room has been created
	@Override
	public void onRoomCreated(int statusCode, Room room) {
		Log.d(TAG, "onRoomCreated(" + statusCode + ", " + room + ")");
		if (statusCode != GamesStatusCodes.STATUS_OK) {
			Log.e(TAG, "*** Error: onRoomCreated, status " + statusCode);
			showGameError();
			return;
		}

		// show the waiting room UI
		showWaitingRoom(room);
	}

	// Called when room is fully connected.
	@Override
	public void onRoomConnected(int statusCode, Room room) {
		Log.d(TAG, "onRoomConnected(" + statusCode + ", " + room + ")");
		if (statusCode != GamesStatusCodes.STATUS_OK) {
			Log.e(TAG, "*** Error: onRoomConnected, status " + statusCode);
			showGameError();
			return;
		}
		updateRoom(room);
	}

	@Override
	public void onJoinedRoom(int statusCode, Room room) {
		Log.d(TAG, "onJoinedRoom(" + statusCode + ", " + room + ")");
		if (statusCode != GamesStatusCodes.STATUS_OK) {
			Log.e(TAG, "*** Error: onRoomConnected, status " + statusCode);
			showGameError();
			return;
		}

		// show the waiting room UI
		showWaitingRoom(room);
	}

	// We treat most of the room update callbacks in the same way: we update our
	// list of
	// participants and update the display. In a real game we would also have to
	// check if that
	// change requires some action like removing the corresponding player avatar
	// from the screen,
	// etc.
	@Override
	public void onPeerDeclined(Room room, List<String> arg1) {
		updateRoom(room);
	}

	@Override
	public void onPeerInvitedToRoom(Room room, List<String> arg1) {
		updateRoom(room);
	}

	@Override
	public void onP2PDisconnected(String participant) {
	}

	@Override
	public void onP2PConnected(String participant) {
	}

	@Override
	public void onPeerJoined(Room room, List<String> arg1) {
		updateRoom(room);
	}

	@Override
	public void onPeerLeft(Room room, List<String> peersWhoLeft) {
		updateRoom(room);
	}

	@Override
	public void onRoomAutoMatching(Room room) {
		updateRoom(room);
	}

	@Override
	public void onRoomConnecting(Room room) {
		updateRoom(room);
	}

	@Override
	public void onPeersConnected(Room room, List<String> peers) {
		updateRoom(room);
	}

	@Override
	public void onPeersDisconnected(Room room, List<String> peers) {
		updateRoom(room);
	}

	void updateRoom(Room room) {
		if (room != null) {
			mParticipants = room.getParticipants();
		}
	}

	public ArrayList<String> getParticipantIds() {
		return room.getParticipantIds();
	}

	public String getMyId() {
		return mMyId;
	}

	public void sendReliableMessage(byte[] message) {

	}

	public void broadcastMessage(byte[] message) {
		for (Participant p : mParticipants) {
			if (!p.getParticipantId().equals(mMyId)) {
				Games.RealTimeMultiplayer.sendUnreliableMessage(
						mGoogleApiClient, message, room.getRoomId(),
						p.getParticipantId());
			}
		}
	}
}
