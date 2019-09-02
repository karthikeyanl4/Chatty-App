package com.example.chatty_4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Main2Activity extends AppCompatActivity {

    public Button advertise;
    public Button discover;
    private ConnectionsClient connectionsClient;
    private String endpointId;

    private ArrayList<LeftRight> mLeftRight = new ArrayList<>();
    private EditText messageinput;
    private Button submit;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    String s;


    private final ConnectionLifecycleCallback mConnectionLifecycleCallback =
            new ConnectionLifecycleCallback() {
                @Override
                public void onConnectionInitiated(String endpointId, ConnectionInfo connectionInfo) {
                    // Automatically accept the connection on both sides.
                    connectionsClient.acceptConnection(endpointId, payloadCallback);
                    Log.e("karthi"," onConnectionInitiated");
                }

                @Override
                public void onConnectionResult(String endpointId, ConnectionResolution result) {
                    switch (result.getStatus().getStatusCode()) {
                        case ConnectionsStatusCodes.STATUS_OK:
                            Log.e("karthi"," ConnectionsStatusCodes.STATUS_OK");
                            Main2Activity.this.endpointId=endpointId;
                            connectionsClient.stopDiscovery();
                            connectionsClient.stopAdvertising();
                            mRecyclerView.setVisibility(View.VISIBLE);
                            messageinput.setVisibility(View.VISIBLE);
                            submit.setVisibility(View.VISIBLE);
                            advertise.setVisibility(View.GONE);
                            discover.setVisibility(View.GONE);
                            break;
                        case ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED:
                            Log.e("karthi"," ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED");
                            break;
                        default:
                            Log.e("karthi"," default");
                            break;
                    }
                }

                @Override
                public void onDisconnected(String endpointId) {
                    Log.e("karthi"," disconnect");
                }
            };

    private EndpointDiscoveryCallback mEndpointDiscoveryCallback = new EndpointDiscoveryCallback() {
        @Override
        public void onEndpointFound(@NonNull String endPointId, @NonNull DiscoveredEndpointInfo discoveredEndpointInfo) {
            connectionsClient.requestConnection(
                    /* endpointName= */ "Device B",
                    endPointId,
                    mConnectionLifecycleCallback);
            Log.e("karthi"," onEndpointFound");
        }

        @Override
        public void onEndpointLost(@NonNull String s) {
            Log.e("karthi"," onEndpointLost");
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Bundle extras = getIntent().getExtras();
        advertise = (Button) findViewById(R.id.Advertise);
        discover = (Button) findViewById(R.id.Discover);

        connectionsClient = Nearby.getConnectionsClient(this);

        advertise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectionsClient.startAdvertising(
                        /* endpointName= */ "Device A",
                        /* serviceId= */ getPackageName(),
                        mConnectionLifecycleCallback,
                        new AdvertisingOptions.Builder().setStrategy(Strategy.P2P_CLUSTER).build());
            }
        });

        discover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectionsClient.startDiscovery(
                        /* serviceId= */ getPackageName(),
                        mEndpointDiscoveryCallback,
                        new DiscoveryOptions.Builder().setStrategy(Strategy.P2P_CLUSTER).build());
            }
        });

        mRecyclerView = findViewById(R.id.recyclerView);
        messageinput = (EditText) findViewById(R.id.messageinput);
        submit = (Button) findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                s = messageinput.getText().toString();
                if (s != null) {
                    createExampleList(true, s);
                    buildRecyclerView();


                    String message = s;
                    byte[] stringToByte = message.getBytes();
                    Payload payload = Payload.fromBytes(stringToByte);
                    connectionsClient.sendPayload(endpointId, payload);

                    messageinput.getText().clear();


                }
            }
        });
    }
    private final PayloadCallback payloadCallback =
            new PayloadCallback() {
                @Override
                public void onPayloadReceived(String endpointId, Payload payload) {

                    Log.e("karthi","onpayloadreceived");
                    byte[] payloadrecived=payload.asBytes();
                    if(payloadrecived!=null) {
                        String convertedpayloadbyte = new String(payloadrecived);
                        Log.e("karthiu","coverted pay load string is ---------------------- " +convertedpayloadbyte );
                        createExampleList(false,convertedpayloadbyte);
                        buildRecyclerView();
                    }


                }

                @Override
                public void onPayloadTransferUpdate(String endpointId, PayloadTransferUpdate update) {
                    Log.e("karthi","onpayloadtransferupdate");


                }
            };

    public void createExampleList(boolean isleft,String s){
        mLeftRight.add(new LeftRight(isleft,s));
    }

    public void buildRecyclerView(){
        mRecyclerView =findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager =new LinearLayoutManager(this);
        mAdapter =new ExampleAdapter(mLeftRight);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }

}