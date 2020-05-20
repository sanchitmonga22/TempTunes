package com.example.mapsnmusic;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;

import java.lang.reflect.Array;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    private static final String CLIENT_ID = "180a2841bd1d47e2af4ae41aa6bae23c";
    private static final String REDIRECT_URI = "maps-n-music-login://callback";
    private SpotifyAppRemote mSpotifyAppRemote;

    public Button playbt;
    public String[] location_data;
    public static String mood;
    public static TextView song;
    public static TextView cond;
    public static TextView temp;

    public String[] high ={"spotify:playlist:37i9dQZF1DWWMOmoXKqHTD","spotify:playlist:37i9dQZF1DX3rxVfibe1L0","spotify:playlist:37i9dQZF1DX4fpCWaHOned","spotify:playlist:37i9dQZF1DX6ziVCJnEm59"};
    public String[] med={"spotify:playlist:37i9dQZF1DXdPec7aLTmlC","spotify:playlist:37i9dQZF1DX7gIoKXt0gmx","spotify:playlist:37i9dQZF1DX0UrRvztWcAU","spotify:playlist:37i9dQZF1DWXmlLSKkfdAk","37i9dQZF1DWSRc3WJklgBs"};
    public String[] low={"spotify:playlist:37i9dQZF1DWSqmBTGDYngZ","spotify:playlist:37i9dQZF1DX7KNKjOK0o75","spotify:playlist:37i9dQZF1DX3YSRoSdA634"};
    private static final String TAG = "MainActivity";
    //    StringBuilder str=new StringBuilder();
    //    URL url;
    //    String inputLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
  //     Log.d(TAG, "onCreate started");
//        ImageView imageView2 = (ImageView) findViewById(R.id.imageView2);

//        int imageResource = getResources().getIdentifier("drawable/atmosphere.png",null, this.getPackageName());
//        imageView2.setImageResource(imageResource);
        playbt = (Button) findViewById(R.id.playbtn); // Create Play Button
//       // loc = (TextView)findViewById(R.id.location);
//        img = (ImageView)findViewById(R.id.imageView2);
       song =(TextView)findViewById(R.id.song);
       temp = (TextView)findViewById(R.id.temp);
       cond = (TextView)findViewById(R.id.condition);
//
        playbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchdata background = new fetchdata();
                background.execute();

                //imageView2.setImageResource(imageResource);
                //String id = background.getID();
                connected();
                //startActivity(new Intent(MainActivity.this, Displaypage.class));
                //loc.setText(id);
                //onStart();

                // Code For selecting music



            }
        });

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                //                makeUseOfNewLocation(location);
                extract(location);


            }

            private void extract(Location location) {
                String templocation = location.toString();
                String s[];
                s = (String[]) templocation.split(" ");
                location_data = s[1].split(",");
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);



    }

    protected String runtemp(String[] temp)
    {
        int rnd = new Random().nextInt(temp.length);
        return temp[rnd];
    }

    @Override
    protected void onStart() {
        super.onStart();
        // We will start writing our code here.
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();
        SpotifyAppRemote.connect(this, connectionParams,
                new Connector.ConnectionListener() {


                    @Override
                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        Log.d("MainActivity", "Connected! Yay!");

                        // Now you can start interacting with App Remote
                        //connected();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.e("MainActivity", throwable.getMessage(), throwable);

                        // Something went wrong when attempting to connect! Handle errors here
                    }
                });


    }

    private void connected() {
        // Then we will write some more code here.
        if(mood == "high")
        {
            mSpotifyAppRemote.getPlayerApi().play(runtemp(high));//"spotify:playlist:37i9dQZF1DX6Rl8uES4jYu");//37i9dQZF1DX2sUQwD7tbmL

        }
        else if(mood == "medium")
        {
            mSpotifyAppRemote.getPlayerApi().play(runtemp(med));//"spotify:playlist:37i9dQZF1DX6Rl8uES4jYu");//37i9dQZF1DX2sUQwD7tbmL

        }
        else
        {
            mSpotifyAppRemote.getPlayerApi().play(runtemp(low));//"spotify:playlist:37i9dQZF1DX6Rl8uES4jYu");//37i9dQZF1DX2sUQwD7tbmL
        }
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    final Track track = playerState.track;
                    if (track != null) {
                        Log.d("MainActivity", track.name + " by " + track.artist.name);
                        song.setText(track.name + " by " + track.artist.name);

                    }
                });
}


    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
        }

        // Aaand we will finish off here.



}
