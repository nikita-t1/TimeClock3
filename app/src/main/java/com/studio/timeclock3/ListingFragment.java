package com.studio.timeclock3;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.ognev.kotlin.agendacalendarview.AgendaCalendarView;
import com.ognev.kotlin.agendacalendarview.CalendarController;
import com.ognev.kotlin.agendacalendarview.builder.CalendarContentManager;
import com.ognev.kotlin.agendacalendarview.models.CalendarEvent;
import com.ognev.kotlin.agendacalendarview.models.DayItem;
import com.ognev.kotlin.agendacalendarview.models.IDayItem;
import com.studio.timeclock3.calendar.MyCalendarEvent;
import com.studio.timeclock3.calendar.SampleEventAgendaAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListingFragment extends Fragment implements CalendarController {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FirstRecyclerViewAdapter adapter;
    @BindView(R.id.agenda_calendar_view) AgendaCalendarView agendaCalendarView;
    private ArrayList<CalendarEvent> eventList;

    Calendar oldDate;

    private OnFragmentInteractionListener mListener;

    public ListingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListingFragment newInstance(String param1, String param2) {
        ListingFragment fragment = new ListingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_listing, container, false);
        ButterKnife.bind(this, view);


        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }


        Calendar minDate = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();
        oldDate = Calendar.getInstance();


        minDate.add(Calendar.MONTH, -1);
        minDate.set(Calendar.DAY_OF_MONTH, 1);
        maxDate.add(Calendar.YEAR, 1);

        CalendarContentManager contentManager;
        contentManager = new CalendarContentManager(ListingFragment.this , agendaCalendarView, new SampleEventAgendaAdapter(getActivity().getApplicationContext()));
        contentManager.setLocale(Locale.GERMAN);
        contentManager.setDateRange(minDate, maxDate);
//        contentManager.loadItemsFromStart();

        int maxLenght = Calendar.getInstance().getMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 0 ; i >= maxLenght; i++) {
            Calendar day = Calendar.getInstance(Locale.GERMAN);
            day.setTimeInMillis(System.currentTimeMillis());
            day.set(Calendar.DAY_OF_MONTH, i);

            eventList.add(new MyCalendarEvent(day, day, DayItem.Companion.buildDayItemFromCal(day), null).setEventInstanceDay(day));

            contentManager.loadItemsFromStart(eventList);
            agendaCalendarView.getAgendaView().getAgendaListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getContext(), "item: " + (position), Toast.LENGTH_SHORT).show();
                }
            });
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public int getEmptyEventLayout() {
        return R.layout.view_agenda_empty_event;
    }

    @Override
    public int getEventLayout() {
        return R.layout.view_agenda_event;
    }

    @Override
    public void onDaySelected(@NotNull IDayItem iDayItem) {

    }

    @Override
    public void onScrollToDate(@NotNull Calendar calendar) {
        int lastPosition = agendaCalendarView.getAgendaView().getAgendaListView().getLastVisiblePosition() + 1;
        boolean isSameDay;
//        if(CalendarExtensionsKt.isSameDay(oldDate){
//            isSameDay=true;
//        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
