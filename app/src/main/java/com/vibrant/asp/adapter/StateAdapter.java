package com.vibrant.asp.adapter;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Filter;
import android.widget.Toast;
import java.util.ArrayList;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vibrant.asp.R;
import com.vibrant.asp.constants.Cons;
import com.vibrant.asp.model.StateModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class StateAdapter extends ArrayAdapter<StateModel> implements Filterable {
    private static final String TAG = "StateAdapter";
    Context mContext;
    private ArrayList <StateModel>stateArray;

    public StateAdapter(Context context, int resource) {
        super(context, resource);
        this.mContext = context;
        stateArray = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return stateArray.size();
    }

    @Override
    public StateModel getItem(int position) {
        return stateArray.get(position);
    }

    @Override
    public Filter getFilter() {
        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    try {
                        //get data from the web
                         String search = constraint.toString();
                        Log.d(TAG, "performFiltering: "+search);
                        stateArray.clear();
                         getState(search);
                    } catch (Exception e) {
                        Log.d("HUS", "EXCEPTION " + e);
                    }
                    filterResults.values = stateArray;
                    filterResults.count = stateArray.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {

                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };

        return myFilter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.state_row_item, parent, false);
        StateModel stateModel = stateArray.get(position);
        TextView tvStateName = (TextView) view.findViewById(R.id.tvStateName);
        tvStateName.setText(stateModel.getStateName());
        return view;
    }

    private void getState(String search) {
        String url = Cons.GET_STATE;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("StateName", search);
            Log.d(TAG, "getState: " + jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                stateArray.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("d");
                    // if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jObj = jsonArray.getJSONObject(i);
                        StateModel stateModel = new StateModel();
                        stateModel.setStateId(jObj.getString("StateId"));
                        stateModel.setStateName(jObj.getString("StateName"));
                        stateArray.add(stateModel);
                    }
                    Log.d(TAG, "Array Size --" + stateArray.size());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(jsonObjReq);


    }
}
