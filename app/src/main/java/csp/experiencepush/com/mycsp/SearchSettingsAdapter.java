package csp.experiencepush.com.mycsp;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by cchestnut on 10/1/14.
 */
public class SearchSettingsAdapter extends ArrayAdapter<SearchSettingsListItem> {
    private static LayoutInflater inflater;
    private Context context;
    public Filter filter;
    private int resource;
    private List<SearchSettingsListItem> objects;

    public SearchSettingsAdapter(Context ctx, int resourceID, List<SearchSettingsListItem> objects) {
        super(ctx, resourceID, objects);
        this.objects = objects;
        resource = resourceID;
        inflater = LayoutInflater.from(ctx);
        context = ctx;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (this.getItemViewType(position) == 0){
            this.resource = R.layout.search_settings_row_selector;
            convertView = (RelativeLayout)inflater.inflate(resource, null);
            SearchSettingsListItem Item = getItem(position);
            TextView itemTitle = (TextView)convertView.findViewById(R.id.selector_row_text);
            itemTitle.setText(Item.getTitle());
            String desiredID;
            RadioGroup radioGroup = (RadioGroup)convertView.findViewById(R.id.selector_group);
            if (Item.getTitle().equals("Beds")){
                desiredID = "selector_segment_"+filter.beds;
                radioGroup.setTag("bed");
            } else {
                desiredID = "selector_segment_"+filter.baths;
                radioGroup.setTag("bath");
            }
            int resID = context.getResources().getIdentifier(desiredID, "id", context.getPackageName());
            SegmentedControlButton desired = (SegmentedControlButton)convertView.findViewById(resID);
            desired.setChecked(true);
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup arg0, int id) {
                    switch (id) {
                        case -1:
                            break;
                        case R.id.selector_segment_0:
                            if (arg0.getTag().equals("bed")){
                                filter.setBeds(0);
                            } else {
                                filter.setBaths(0);
                            }
                            break;
                        case R.id.selector_segment_1:
                            if (arg0.getTag().equals("bed")){
                                filter.setBeds(1);
                            } else {
                                filter.setBaths(1);
                            }
                            break;
                        case R.id.selector_segment_2:
                            if (arg0.getTag().equals("bed")){
                                filter.setBeds(2);
                            } else {
                                filter.setBaths(2);
                            }
                            break;
                        case R.id.selector_segment_3:
                            if (arg0.getTag().equals("bed")){
                                filter.setBeds(3);
                            } else {
                                filter.setBaths(3);
                            }
                            break;
                        case R.id.selector_segment_4:
                            if (arg0.getTag().equals("bed")){
                                filter.setBeds(4);
                            } else {
                                filter.setBaths(4);
                            }
                            break;
                        case R.id.selector_segment_5:
                            if (arg0.getTag().equals("bed")){
                                filter.setBeds(5);
                            } else {
                                filter.setBaths(5);
                            }
                            break;
                        default:
                            break;
                    }
                }
            });
        } else if (this.getItemViewType(position) == 1){
            this.resource = R.layout.search_settings_row_input;
            convertView = (RelativeLayout)inflater.inflate(resource, null);
            SearchSettingsListItem Item = getItem(position);
            TextView itemTitle = (TextView)convertView.findViewById(R.id.edit_text_row_title);
            itemTitle.setText(Item.getTitle());

            final EditText editText = (EditText)convertView.findViewById(R.id.rent_input_field);

            if (position == 4){
                editText.setTag("low");
            } else if (position == 5){
                editText.setTag("high");
            }

            int value;
            if (editText.getTag().equals("low")){
                value = (int)filter.lowRent;
            } else {
                value = (int)filter.highRent;
            }

            if (value != 0){
                String newText = String.valueOf(value);
                editText.setText("$"+newText);
            } else {
                if (editText.getTag().equals("low")){
                    editText.setHint("$0");
                } else {
                    editText.setHint("$4000");
                }
            }
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    if (charSequence.toString().startsWith("$")){
                        editText.setSelection(editText.getText().length());
                        double newRent = Double.parseDouble(editText.getText().toString().replace("$", ""));
                        String tag = (String)editText.getTag();
                        if (tag.equals("low")){
                            filter.setLowRent(newRent);
                        } else if (tag .equals("high")){
                            filter.setHighRent(newRent);
                        }
                    } else {
                        String newS = charSequence.toString().replace("$", "");
                        newS = "$" + newS;
                        if (!newS.equals(charSequence.toString())) {
                            editText.setText(newS);
                            editText.setSelection(editText.getText().length());
                            double newRent = Double.parseDouble(newS.replace("$", ""));
                            String tag = (String)editText.getTag();
                            if (tag.equals("low")){
                                filter.setLowRent(newRent);
                            } else if (tag .equals("high")){
                                filter.setHighRent(newRent);
                            }
                        }
                    }

                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
        } else if (getItemViewType(position) == 4){
            this.resource = R.layout.search_settings_row_header;
            convertView = (RelativeLayout)inflater.inflate(resource, null);
            SearchSettingsListItem Item = getItem(position);
            TextView itemTitle = (TextView)convertView.findViewById(R.id.header_text);
            itemTitle.setText(Item.getTitle());
        } else if (getItemViewType(position) == 5) {
            this.resource = R.layout.search_settings_row_footer;
            convertView = (RelativeLayout) inflater.inflate(resource, null);
            SearchSettingsListItem Item = getItem(position);
            TextView itemTitle = (TextView) convertView.findViewById(R.id.footer_text);
            itemTitle.setText(Item.getTitle());
        } else if (getItemViewType(position) == 2){
            this.resource = R.layout.search_settings_row_picker;
            convertView = (RelativeLayout) inflater.inflate(resource, null);
            SearchSettingsListItem Item = getItem(position);
            Spinner spinner = (Spinner) convertView.findViewById(R.id.spinner);
            String current = dateForInt(Item.getValue());
            int pos = 0;
            List<String> list = new ArrayList<String>();
            list.add("Any");
            SimpleDateFormat df = new SimpleDateFormat("M yyyy", Locale.US);
            String dateString = df.format(new Date());
            int m = Integer.parseInt(dateString.split("\\s+")[0]);
            int y = Integer.parseInt(dateString.split("\\s+")[1]);
            list.add(stringForMonth(m)+" "+y);
            for (int i = m + 1; i < m + 12; i++){
                if (i%12 == 1){
                    y++;
                }
                String month = stringForMonth(i) + " "+y;
                if (month.equals(stringForMonth(filter.month) + " " + filter.year)){
                    pos = i + 1 - m;
                }

                list.add(month);
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.context,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            spinner.setAdapter(dataAdapter);
            spinner.setSelection(pos);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (i == 0){
                        filter.setMonth(0);
                        filter.setYear(0);
                    } else {
                        SimpleDateFormat df = new SimpleDateFormat("MM yyyy", Locale.US);
                        String dateString = df.format(new Date());
                        int m = Integer.parseInt(dateString.split("\\s+")[0]);
                        int y = Integer.parseInt(dateString.split("\\s+")[1]);
                        m += i - 1;
                        if (m > 12){
                            y++;
                        }
                        m = m % 12;
                        if (m == 0){
                            m = 12;
                        }
                        filter.setMonth(m);
                        filter.setYear(y);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            /*
            Button button = (Button)convertView.findViewById(R.id.picker_select_button);
            button.setTextColor(0xff6f7dda);
            button.setText(dateForInt(Item.getValue()));
            */
        } else if (getItemViewType(position) == 3){
            this.resource = R.layout.search_settings_row_check;
            convertView = (RelativeLayout) inflater.inflate(resource, null);
            SearchSettingsListItem Item = getItem(position);
            TextView itemTitle = (TextView) convertView.findViewById(R.id.check_row_title);
            itemTitle.setText(Item.getTitle());
            Switch checkBox = (Switch) convertView.findViewById(R.id.checkBox);
            checkBox.setTag(String.valueOf((int)Item.getValue()));
            if (filter.getAmenities()[(int)Item.getValue()]){
                checkBox.setChecked(true);
            }
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    boolean newValue = ((Switch)compoundButton).isChecked();
                    int pos = Integer.parseInt(String.valueOf(compoundButton.getTag()));
                    filter.setBool(pos, newValue);
                }
            });

        }

        return convertView;
    }

    private String stringForMonth(int month){
        switch (month%12) {
            case 1:
                return "January";
            case 2:
                return "Feburary";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 0:
                return "December";

            default:
                break;
        }
        return "";
    }

    private String dateForInt(double value){
        if (value == 0){
            return "Any";
        }
        double year = (value % 1.0) * 10000;
        if (year % 1 >= 0.5 ){
            year = Math.ceil(year);
        } else {
            year = Math.floor(year);
        }
        double mon = Math.floor(value);
        switch ((int)mon){
            case (1):
                return "January "+(int)year;

            case (2):
                return "Feburary "+(int)year;

            case (3):
                return "March "+(int)year;

            case (4):
                return "April "+(int)year;

            case (5):
                return "May "+(int)year;

            case 6:
                return "June "+(int)year;

            case 7:
                return "July "+(int)year;

            case 8:
                return "August "+(int)year;

            case 9:
                return "September "+(int)year;

            case 10:
                return "October "+(int)year;

            case 11:
                return "November "+(int)year;

            case 12:
                return "December "+(int)year;
        }
        return "";
    }

    @Override
    public int getItemViewType(int position){
        String type = this.objects.get(position).getType();
        if (type.equals("selector")){
            return 0;
        } else if (type.equals("textInput")){
            return 1;
        } else if (type.equals("picker")){
            return 2;
        } else if (type.equals("switch")){
            return 3;
        } else if (type.equals("header")){
            return 4;
        } else if (type.equals("footer")){
            return 5;
        }
        return 0;
    }

    @Override
    public int getViewTypeCount(){
        return 6;
    }
}
