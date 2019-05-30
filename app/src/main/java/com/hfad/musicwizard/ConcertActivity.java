package com.hfad.musicwizard;


import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ticketmaster.api.discovery.DiscoveryApi;
import com.ticketmaster.discovery.model.Event;
import com.ticketmaster.discovery.model.Events;
import com.ticketmaster.api.discovery.operation.SearchEventsOperation;
import com.ticketmaster.api.discovery.response.PagedResponse;

import java.io.IOException;
import java.util.List;

public class ConcertActivity extends AppCompatActivity {

    private SearchView searchViewEvents;
    private TextView textViewEventName, textViewEventTime, textViewEventLocation, textViewEventDescription;
    private ImageView imageViewEvent;
    private Button buttonNextEvent, buttonPreviousEvent;

    public String API_KEY = "5wi7kOO20VAi3xDlMNUWhwS9CS2yaIh7";
    public DiscoveryApi api = new DiscoveryApi(API_KEY);

    private Event event;
    private int eventIndex;
    private String searchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concert);
        event = new Event();
        eventIndex = 0;
        wireWidgets();
    }

    private void wireWidgets() {

        textViewEventName = findViewById(R.id.textview_concertactivity_eventname);
        textViewEventTime = findViewById(R.id.textview_concertactivity_eventtime);
        textViewEventLocation = findViewById(R.id.textview_concertactivity_eventlocation);
        textViewEventDescription = findViewById(R.id.textView_concertactivity_eventdescription);
        imageViewEvent = findViewById(R.id.imageview_concertactivity);
        buttonNextEvent = findViewById(R.id.button_concertactivity_nextevent);
        buttonPreviousEvent = findViewById(R.id.button_concertactivity_previousevent);

        buttonNextEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventIndex++;
                new Search(event).execute(searchQuery);
            }
        });

        buttonPreviousEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventIndex--;
                new Search(event).execute(searchQuery);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent intent1 = new Intent(ConcertActivity.this, MainActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.navigation_lyrics:
                        Intent intent2 = new Intent(ConcertActivity.this, LyricActivity.class);
                        startActivity(intent2);
                        break;

                    case R.id.navigation_concerts:
                        break;
                }

                return false;
            }
        });

        searchViewEvents = findViewById(R.id.searchview_concertactivity);
        searchViewEvents.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery = query;
                eventIndex = 0;
                new Search(event).execute(searchQuery);
                searchViewEvents.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    public class Search extends AsyncTask<String,String,String> {

        String eventName, eventTime, eventLocation, eventInfo, eventImageURL;
        Event event;
        List<Event> events;

        Search(Event event) {
            this.event = event;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                PagedResponse<Events> page;
                page = api.searchEvents(new SearchEventsOperation().keyword(strings[0]));
                if (page.getContent() != null && page.getContent().getEvents() != null) {
                    events = page.getContent().getEvents();
                    if (events.get(eventIndex) != null) {
                        event = events.get(eventIndex);
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);

            if (events != null && events.size() >0 && event != null) {

                if (eventIndex < events.size())
                    buttonNextEvent.setVisibility(View.VISIBLE);
                else
                    buttonPreviousEvent.setVisibility(View.INVISIBLE);
                if (eventIndex > 0)
                    buttonPreviousEvent.setVisibility(View.VISIBLE);
                else
                    buttonPreviousEvent.setVisibility(View.INVISIBLE);

                if (event.getName() != null)
                    eventName = event.getName();
                else
                    eventName = "No related events are currently available.";


                if (event.getDates() != null)
                    eventTime = event.getDates().getStart().getLocalDate() + " | " + event.getDates().getStart().getLocalTime();
                else
                    eventTime = "No time information is currently available";


                if (event.getVenues() != null)
                    eventLocation = event.getVenues().get(0).getCity().getName() + ", " + event.getVenues().get(0).getCountry().getName();
                else
                    eventLocation = "No location information is currently available";


                if (event.getInfo() != null)
                    eventInfo = event.getInfo();
                else
                    eventInfo = "No description is currently available.";


                if (event.getImages() != null && event.getImages().get(0) != null && event.getImages().get(0).getUrl() != null) {
                    eventImageURL = event.getImages().get(0).getUrl();
                    Picasso.get().load(eventImageURL).into(imageViewEvent);
                }
            }

            textViewEventName.setText(eventName);
            textViewEventTime.setText(eventTime);
            textViewEventLocation.setText(eventLocation);
            textViewEventDescription.setText(eventInfo);
        }
    }

}