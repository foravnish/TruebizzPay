package app.application.recharge.titoriya.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.application.recharge.titoriya.R;
import app.application.recharge.titoriya.asynctask.WalletBalanceAsyncTask;
import app.application.recharge.titoriya.connection.InternetStatus;


/**
 * Created by user on 2/13/2017.
 */

public class Balance extends Fragment {
    TextView title, bal_txv;
    LinearLayout bck_ly;
    Button bal_title;
    Fragment fragment;
    String bal_prefix;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.balancefragment, container, false);
        inIt(view);
        return view;
    }

    private void inIt(View view) {

        title = (TextView) view.findViewById(R.id.title);
        bal_txv = (TextView) view.findViewById(R.id.bal_txv);
        bal_title = (Button) view.findViewById(R.id.bal_title);

//        if (MyPrefrences.getUserType(getActivity()).equalsIgnoreCase(Util.Distributor)) {
//            fragment = new DistributorFragment();
//            title.setText("Today Earning");
//            bal_title.setText("Today Earning");
//            bal_prefix = "Today Total Earning : ";
//            // bal_txv.setText("Today Total Earning : 500");
//        } else {
//            fragment = new RetailerFragment();
//            title.setText("Balance");
//            bal_title.setText("Balance");
//            bal_prefix = "Total Balance : ";
//            //bal_txv.setText("Total Balance : 500");
//        }

        fragment = new RetailerFragment();
        title.setText("Balance");
        bal_title.setText("Balance");
        bal_prefix = "Total Balance : ";
        //bal_txv.setText("Total Balance : 500");

        bck_ly = (LinearLayout) view.findViewById(R.id.bck_ly);
        bck_ly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putSerializable("trip_details", "ride rejected");
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.container, fragment).addToBackStack("one");
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (InternetStatus.isConnectingToInternet(getActivity())) {
            new WalletBalanceAsyncTask(getActivity(),bal_txv).execute();
        }
       /* if (Util.Check_Balance == 1) {
            bal_txv.setText(Util.Wallet_Balance);

        }*/
    }

}
