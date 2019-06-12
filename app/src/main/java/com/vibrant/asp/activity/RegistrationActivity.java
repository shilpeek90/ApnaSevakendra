package com.vibrant.asp.activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vibrant.asp.R;
import com.vibrant.asp.adapter.StateAdapter;
import com.vibrant.asp.constants.Cons;
import com.vibrant.asp.constants.ProgressDialog;
import com.vibrant.asp.model.StateModel;
import android.app.AlertDialog.Builder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import static com.vibrant.asp.constants.Util.hideKeyboard;
import static com.vibrant.asp.constants.Util.isInternetConnected;

public class RegistrationActivity extends AppCompatActivity {
    private static final String TAG = "RegistrationActivity";
    TextView tvHeader;
    EditText editDist, editGrowerName, editMobile;
    AutoCompleteTextView editState;
    Button btnSubmit;
    private Builder builder;
    AlertDialog ad;
    private boolean doubleBackToExitPressedOnce = false;
    ProgressDialog pd;
    String search = "";
    ArrayList<StateModel> stateArray = new ArrayList<>();
    private boolean isSelect = false;
    StateAdapter stateAdapter;
    String[] Countries = { "India", "USA", "Australia", "UK", "Italy", "Ireland", "Africa" };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        hideKeyboard(RegistrationActivity.this);


        stateAdapter = new StateAdapter(RegistrationActivity.this,R.layout.support_simple_spinner_dropdown_item);
        editState.setThreshold(1);
        editState.setAdapter(stateAdapter);
        stateAdapter.notifyDataSetChanged();


        //when autocomplete is clicked
        editState.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String stateName = stateAdapter.getItem(position).getStateName();
                String stateId = stateAdapter.getItem(position).getStateId();
                Log.d(TAG, "onItemClick: "+stateName+">>>"+stateId);
                editState.setText(stateName);
            }
        });

        init();
    }

    private void init() {
        tvHeader = findViewById(R.id.tvHeader);
        tvHeader.setText(getString(R.string.registration));
        editState = findViewById(R.id.editState);
        editDist = findViewById(R.id.editDist);
        editGrowerName = findViewById(R.id.editGrowerName);
        editMobile = findViewById(R.id.editMobile);

/*
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, Countries);

        editState.setThreshold(1);
        editState.setAdapter(adapter);
        editState.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: "+ parent.getSelectedItem());
                Toast.makeText(getApplicationContext(), "Selected Item: " + parent.getSelectedItem(), Toast.LENGTH_SHORT).show();
            }
        });*/


      /* editState.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search = s.toString();
                Log.d(TAG, "onTextChanged: " + s + ">>>>>>" + search);
                if (search.length() > 0) {
                    getState();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


*/
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetConnected(getApplicationContext())) {
                    startActivity(new Intent(RegistrationActivity.this, DashboardActivity.class));
                } else {
                    Toast.makeText(RegistrationActivity.this, getResources().getString(R.string.check_network), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


/*

    private void getState() {
        String url = Cons.GET_STATE;
        pd = ProgressDialog.show(RegistrationActivity.this, "Please Wait...");
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
                pd.dismiss();
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

                    if (stateArray.size() > 0) {
                      //  isSelect = true;
                        getCustomPopUp();
                    } else {
                        Toast.makeText(RegistrationActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());
                pd.dismiss();
                Toast.makeText(RegistrationActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjReq);
    }


    private void getCustomPopUp() {
        builder = new AlertDialog.Builder(new ContextThemeWrapper(RegistrationActivity.this, R.style.AlertDialogCustom));
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.state_layout, null);
        builder.setView(dialogLayout);
        RelativeLayout rlRootLayout = dialogLayout.findViewById(R.id.rlRootLayout);
        RecyclerView recyclerView = dialogLayout.findViewById(R.id.recyclerView);
        TextView tvNoRecord = dialogLayout.findViewById(R.id.tvNoRecord);

        isSelect = false;
        if (stateArray.size() > 0) {
            tvNoRecord.setVisibility(View.GONE);
            StateAdapter mAdapter = new StateAdapter(this, stateArray);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        } else {
            tvNoRecord.setVisibility(View.VISIBLE);
        }
        ad = builder.show();
        // builder.show();
    }

    public class StateAdapter extends RecyclerView.Adapter<StateAdapter.MyViewHolder> {
        private List<StateModel> arrayList;
        Context mContext;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tvStateName;
            CardView cardView;

            public MyViewHolder(View view) {
                super(view);
                tvStateName = (TextView) view.findViewById(R.id.tvStateName);
                cardView =(CardView)view.findViewById(R.id.cardView);
            }
        }

        public StateAdapter(Context context, List<StateModel> arrayList) {
            this.mContext = context;
            this.arrayList = arrayList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.state_row_item, parent, false);
            return new StateAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            holder.tvStateName.setText(arrayList.get(position).getStateName());
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String mStateName = arrayList.get(position).getStateName();
                    Log.d(TAG, "onClick: "+arrayList.get(position).getStateId());
                    Log.d(TAG, "onClick: "+arrayList.get(position).getStateName());
                    editState.setText("");
                    editState.setText(arrayList.get(position).getStateName());
                    ad.dismiss();
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

*/


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
