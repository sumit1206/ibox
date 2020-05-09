package com.sumit.ibox.model;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sumit.ibox.R;
import com.sumit.ibox.common.Constant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

/**
 * Created by shivam-sumit on 07/01/2020.
 */
public class CalendarView extends LinearLayout
{
	// for logging
	private static final String LOGTAG = "Calendar View";

	private static final int DAYS_COUNT = 42;

	// default date format
	private static final String DATE_FORMAT = "MMM yyyy";

	private String dateFormat;

	private static Calendar currentDate = Calendar.getInstance();

	private EventHandler eventHandler = null;

	private ArrayList<EventSet> eventSets = null;

	private ArrayList<Integer> weekEndList = null;

	public void setWeekEndList(ArrayList<Integer> weekEndList) {
		this.weekEndList = weekEndList;
	}

	public void setEventSets(ArrayList<EventSet> eventSets) {
		this.eventSets = eventSets;
	}

	//click handler flags
	private boolean longPressWithoutEventEnabled = false;
	private boolean longPressOnEventEnabled = false;
	private boolean shortPressWithoutEventEnabled = false;
	private boolean shortPressOnEventEnabled = false;

	public void setLongPressWithoutEventEnabled(boolean longPressWithoutEventEnabled) {
		this.longPressWithoutEventEnabled = longPressWithoutEventEnabled;
	}

	public void setLongPressOnEventEnabled(boolean longPressOnEventEnabled) {
		this.longPressOnEventEnabled = longPressOnEventEnabled;
	}

	public void setShortPressWithoutEventEnabled(boolean shortPressWithoutEventEnabled) {
		this.shortPressWithoutEventEnabled = shortPressWithoutEventEnabled;
	}

	public void setShortPressOnEventEnabled(boolean shortPressOnEventEnabled) {
		this.shortPressOnEventEnabled = shortPressOnEventEnabled;
	}

	private LinearLayout header;
	private ImageView btnPrev;
	private ImageView btnNext;
	private TextView txtDate;
	private GridView grid;

	int[] rainbow = new int[] {
			R.color.summer,
			R.color.fall,
			R.color.winter,
			R.color.spring
	};


	int[] monthSeason = new int[] {2, 2, 3, 3, 3, 0, 0, 0, 1, 1, 1, 2};

	public CalendarView(Context context)
	{
		super(context);
	}

	public CalendarView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initControl(context, attrs);
	}

	public CalendarView(Context context, AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		initControl(context, attrs);
	}

	/**
	 * Load xml layout
	 */
	private void initControl(Context context, AttributeSet attrs)
	{
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.control_calendar, this);

		loadDateFormat(attrs);
		assignUiElements();
		assignClickHandlers();

		updateCalendar();
	}

	private void loadDateFormat(AttributeSet attrs)
	{
		TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CalendarView);

		try
		{
			// try to load provided date format, and fallback to default otherwise
			dateFormat = ta.getString(R.styleable.CalendarView_dateFormat);
			if (dateFormat == null)
				dateFormat = DATE_FORMAT;
//			Utils.logPrint(getClass(),"dateFormat",dateFormat);
		}
		finally
		{
			ta.recycle();
		}
	}
	private void assignUiElements()
	{
		header = (LinearLayout)findViewById(R.id.calendar_header);
		btnPrev = (ImageView)findViewById(R.id.calendar_prev_button);
		btnNext = (ImageView)findViewById(R.id.calendar_next_button);
		txtDate = (TextView)findViewById(R.id.calendar_date_display);
		grid = (GridView)findViewById(R.id.calendar_grid);
	}

	private void assignClickHandlers()
	{
		// add one month and refresh UI
		btnNext.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				currentDate.add(Calendar.MONTH, 1);
				int year = currentDate.get(Calendar.YEAR);
				int month = currentDate.get(Calendar.MONTH);
				if (eventHandler != null) {
					eventHandler.onNextPressed(year, month);
				}
//				updateCalendar();
			}
		});

		// subtract one month and refresh UI
		btnPrev.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				currentDate.add(Calendar.MONTH, -1);
				int year = currentDate.get(Calendar.YEAR);
				int month = currentDate.get(Calendar.MONTH);
				if (eventHandler != null) {
					eventHandler.onPrevPressed(year, month);
				}
//				updateCalendar();
			}
		});

	}

	/**
	 * Display dates grid
	 */
	public void updateCalendar()
	{
		updateCalendar(null);
	}

	/**
	 * Display in grid
	 */
	public void updateCalendar(HashSet<Event> events)
	{
		ArrayList<Date> cells = new ArrayList<>();
		Calendar calendar = (Calendar)currentDate.clone();


		calendar.set(Calendar.DAY_OF_MONTH, 1);
		int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1;


		calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);

		// fill cells
		while (cells.size() < DAYS_COUNT)
		{
			cells.add(calendar.getTime());
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}

		// update grid
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		txtDate.setText(sdf.format(currentDate.getTime()));

		// set header color according to current season
		int year = currentDate.get(Calendar.YEAR);
		int month = currentDate.get(Calendar.MONTH);
		int season = monthSeason[month];
		int color = rainbow[season];
		header.setBackgroundColor(getResources().getColor(color));
	//update evens here
		grid.setAdapter(new CalendarAdapter(getContext(), cells, events));
	}

	public Calendar getCurrentDate() {
		return currentDate;
	}

	private class CalendarAdapter extends ArrayAdapter<Date>
	{
		// days with events
		Context context;
		private HashSet<Event>eventDays;

		private LayoutInflater inflater;

		public CalendarAdapter(Context context, ArrayList<Date> days, HashSet<Event> eventDays)
		{
			super(context, R.layout.control_calendar_day, days);
			this.context = context;
			this.eventDays = eventDays;
			inflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View view, ViewGroup parent)
		{
			final Date date = getItem(position);
			int day = date.getDate();
			int month = date.getMonth();
			int year = date.getYear();


			// today
			Date today = new Date();

			// inflate item if it does not exist yet
			if (view == null)
				view = inflater.inflate(R.layout.control_calendar_day, parent, false);

			// if this day has an event, specify event image
			view.setBackgroundResource(0);
			if(longPressWithoutEventEnabled)
			view.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					if(eventHandler == null)
						return false;
					eventHandler.onDayLongPress(date, null);
					return true;
				}
			});
			if(shortPressWithoutEventEnabled){
				view.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if(eventHandler == null)
							return;
						eventHandler.onDayShortPress(date, null);
					}
				});
			}
			//////////////////////////////////////////////////////
			int weekDay = date.getDay() + 1 ;
			if(weekEndList != null){
				if(weekEndList.contains(weekDay)){
					if(eventSets != null){
						for (EventSet eventSet : eventSets) {
							if (Constant.ATTENDENCE_WEEKEND == eventSet.getEventType()) {
								view.setBackgroundResource(eventSet.getEventColour());
							}
						}
					}
				}
			}
			//////////////////////////////////////////////////////
			if (eventDays != null)
			{
				for (final Event event : eventDays)
				{
					Date eventDate = event.getDate();
					int eventType = event.getType();
					if (eventDate.getDate() == day &&
							eventDate.getMonth() == month &&
							eventDate.getYear() == year)
					{
						if(eventSets != null) {
							for (EventSet eventSet : eventSets) {
								if (eventType == eventSet.getEventType()) {
									view.setBackgroundResource(eventSet.getEventColour());
								}
							}
						}

						if(longPressOnEventEnabled) {
							view.setOnLongClickListener(new OnLongClickListener() {
								@Override
								public boolean onLongClick(View v) {
									if (eventHandler == null)
										return false;
									eventHandler.onDayLongPress(date, event);
									return true;
								}
							});
						}
						if(shortPressOnEventEnabled){
							view.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									if(eventHandler == null)
										return;
									eventHandler.onDayShortPress(date, event);
								}
							});
						}
						break;
					}
				}
			}
			// clear styling
			if(month == currentDate.get(Calendar.MONTH) && year == currentDate.get(Calendar.YEAR)-1900) {
				((TextView) view).setTypeface(null, Typeface.NORMAL);
				((TextView) view).setTextColor(Color.BLACK);
			}else {
				((TextView)view).setTextColor(getResources().getColor(R.color.greyed_out));
			}

			if (day == today.getDate() && month == today.getMonth() && year == today.getYear())
			{
				// if it is today, set it to blue/bold
				((TextView)view).setTypeface(null, Typeface.BOLD);
				((TextView)view).setTextColor(getResources().getColor(R.color.today));
			}
			// set text
			((TextView)view).setText(String.valueOf(date.getDate()));

			return view;
		}

	}

	/**
	 * Assign event handler to be passed needed events
	 */
	public void setEventHandler(EventHandler eventHandler)
	{
		this.eventHandler = eventHandler;
	}
	/**
	 * This interface defines what events to be reported to
	 * the outside world
	 */
	public abstract static class EventHandler
	{
		public void onDayShortPress(Date date, Event event){}
		public void onDayLongPress(Date date, Event event){}
		public void onPrevPressed(int year, int month){}
		public void onNextPressed(int year, int month){}
	}

	public static class EventSet{
		int eventType;
		int eventColour;

		public EventSet(int eventType, int eventColour) {
			this.eventType = eventType;
			this.eventColour = eventColour;
		}

		public int getEventType() {
			return eventType;
		}

		public int getEventColour() {
			return eventColour;
		}

	}

	public static class Event {
		Date date;
		int type;
		String name;

		public Event(Date date, int type, String name) {
			this.date = date;
			this.type = type;
			this.name = name;
		}

		public Date getDate() {
			return date;
		}

		public int getType() {
			return type;
		}

		public String getName() {
			return name;
		}
	}

}
