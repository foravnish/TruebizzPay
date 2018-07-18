package app.application.recharge.titoriya.Fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.application.recharge.titoriya.R;


/**
 * Created by user on 2/6/2017.
 */

public class RetailerFragment extends Fragment implements View.OnClickListener {
    CheckedTextView rchg_ctv, rchg_hist__ctv, pmt_hist__ctv, rmt_hist__ctv, pwd_chg_ctv,
            prof__ctv, r_disp__ctv, bal__ctv, spt_cent_ctv, ifsc_code_ctv, bill_summ_ctv,
            upt_pmt_ctv, mnyrmt_ctv, pstpd_ctv, bill_pay_ctv;
    ////////////declare widget for recharge module
    LinearLayout sub_rch_ly, bill_pmt_ly, pstpd_ly;
    TextView mobilerch_txv, dthrch_txv, pstrch_txv, dtc_rch_txv, eltc_rch_txv, gas_rch_txv, ins_rch_txv, pst_dtc_rch_txv;


    ///////////////////////////////////
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.retailerfragment, container, false);
        inIt(view);

        return view;
    }

    private void inIt(View view) {
        rchg_ctv = (CheckedTextView) view.findViewById(R.id.rchg_ctv);
        pstpd_ctv = (CheckedTextView) view.findViewById(R.id.pstpd_ctv);
        bill_pay_ctv = (CheckedTextView) view.findViewById(R.id.bill_pay_ctv);

        rchg_hist__ctv = (CheckedTextView) view.findViewById(R.id.rchg_hist__ctv);
        pmt_hist__ctv = (CheckedTextView) view.findViewById(R.id.pmt_hist__ctv);
        rmt_hist__ctv = (CheckedTextView) view.findViewById(R.id.rmt_hist__ctv);
        pwd_chg_ctv = (CheckedTextView) view.findViewById(R.id.pwd_chg_ctv);
        prof__ctv = (CheckedTextView) view.findViewById(R.id.prof__ctv);
        r_disp__ctv = (CheckedTextView) view.findViewById(R.id.r_disp__ctv);
        spt_cent_ctv = (CheckedTextView) view.findViewById(R.id.spt_cent_ctv);
        bal__ctv = (CheckedTextView) view.findViewById(R.id.bal__ctv);
        ifsc_code_ctv = (CheckedTextView) view.findViewById(R.id.ifsc_code_ctv);
        bill_summ_ctv = (CheckedTextView) view.findViewById(R.id.bill_summ_ctv);
        upt_pmt_ctv = (CheckedTextView) view.findViewById(R.id.upt_pmt_ctv);
        mnyrmt_ctv = (CheckedTextView) view.findViewById(R.id.mnyrmt_ctv);

        ////////////////recharge type layout widget intialize
        sub_rch_ly = (LinearLayout) view.findViewById(R.id.sub_rch_ly);
        sub_rch_ly.setVisibility(View.GONE);
        bill_pmt_ly = (LinearLayout) view.findViewById(R.id.bill_pmt_ly);
        bill_pmt_ly.setVisibility(View.GONE);

        pstpd_ly = (LinearLayout) view.findViewById(R.id.pstpd_ly);
        pstpd_ly.setVisibility(View.GONE);


        mobilerch_txv = (TextView) view.findViewById(R.id.mobilerch_txv);
        dthrch_txv = (TextView) view.findViewById(R.id.dthrch_txv);
        pstrch_txv = (TextView) view.findViewById(R.id.pstrch_txv);
        dtc_rch_txv = (TextView) view.findViewById(R.id.dtc_rch_txv);
        pst_dtc_rch_txv = (TextView) view.findViewById(R.id.pst_dtc_rch_txv);
        eltc_rch_txv = (TextView) view.findViewById(R.id.eltc_rch_txv);
        gas_rch_txv = (TextView) view.findViewById(R.id.gas_rch_txv);
        ins_rch_txv = (TextView) view.findViewById(R.id.ins_rch_txv);

        mobilerch_txv.setOnClickListener(this);
        dthrch_txv.setOnClickListener(this);
        pstrch_txv.setOnClickListener(this);
        dtc_rch_txv.setOnClickListener(this);
        pst_dtc_rch_txv.setOnClickListener(this);
        eltc_rch_txv.setOnClickListener(this);
        gas_rch_txv.setOnClickListener(this);
        ins_rch_txv.setOnClickListener(this);

        //////////////////////////////////////////
        pmt_hist__ctv.setOnClickListener(this);
        rchg_hist__ctv.setOnClickListener(this);
        rchg_ctv.setOnClickListener(this);
        bill_pay_ctv.setOnClickListener(this);
        pstpd_ctv.setOnClickListener(this);

        rmt_hist__ctv.setOnClickListener(this);
        pwd_chg_ctv.setOnClickListener(this);
        prof__ctv.setOnClickListener(this);
        r_disp__ctv.setOnClickListener(this);
        bal__ctv.setOnClickListener(this);
        spt_cent_ctv.setOnClickListener(this);
        ifsc_code_ctv.setOnClickListener(this);
        bill_summ_ctv.setOnClickListener(this);
        upt_pmt_ctv.setOnClickListener(this);
        mnyrmt_ctv.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pmt_hist__ctv:
                sendToHistoryFragment("Payment History");
                break;
            case R.id.rchg_hist__ctv:
                sendToHistoryFragment("Recharge History");
                break;
            case R.id.rmt_hist__ctv:
                sendToHistoryFragment("Remittance History");
                break;
            case R.id.bill_summ_ctv:
                sendToHistoryFragment("Billing Summary");
                break;
            case R.id.rchg_ctv:
                if (rchg_ctv.isChecked()) {
                    // Drawable img = getResources().getDrawable(android.R.drawable.cursor);
                    Drawable img = ContextCompat.getDrawable(getActivity(), R.drawable.up);
                    rchg_ctv.setCheckMarkDrawable(img);
                    sub_rch_ly.setVisibility(View.VISIBLE);
                    rchg_ctv.setChecked(false);

                } else {
                    Drawable img = ContextCompat.getDrawable(getActivity(), R.drawable.down);
                    //   rchg_hist__ctv.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                    rchg_ctv.setCheckMarkDrawable(img);
                    sub_rch_ly.setVisibility(View.GONE);
                    rchg_ctv.setChecked(true);
                }
                break;
            case R.id.pstpd_ctv:
                if (pstpd_ctv.isChecked()) {
                    // Drawable img = getResources().getDrawable(android.R.drawable.cursor);
                    Drawable img = ContextCompat.getDrawable(getActivity(), R.drawable.up);
                    pstpd_ctv.setCheckMarkDrawable(img);
                    pstpd_ly.setVisibility(View.VISIBLE);
                    pstpd_ctv.setChecked(false);

                } else {
                    Drawable img = ContextCompat.getDrawable(getActivity(), R.drawable.down);
                    //   rchg_hist__ctv.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                    pstpd_ctv.setCheckMarkDrawable(img);
                    pstpd_ly.setVisibility(View.GONE);
                    pstpd_ctv.setChecked(true);
                }
                break;
            case R.id.bill_pay_ctv:
                if (bill_pay_ctv.isChecked()) {
                    // Drawable img = getResources().getDrawable(android.R.drawable.cursor);
                    Drawable img = ContextCompat.getDrawable(getActivity(), R.drawable.up);
                    bill_pay_ctv.setCheckMarkDrawable(img);
                    bill_pmt_ly.setVisibility(View.VISIBLE);
                    bill_pay_ctv.setChecked(false);

                } else {
                    Drawable img = ContextCompat.getDrawable(getActivity(), R.drawable.down);
                    //   rchg_hist__ctv.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                    bill_pay_ctv.setCheckMarkDrawable(img);
                    bill_pmt_ly.setVisibility(View.GONE);
                    bill_pay_ctv.setChecked(true);
                }

                break;
            case R.id.pwd_chg_ctv:
                replaceFragment(new ChangePwdFragment());

                break;

            case R.id.prof__ctv:
                replaceFragment(new ProfileFragment());

                break;
            case R.id.r_disp__ctv:
               replaceFragment(new DisputeFragment());
                break;
            case R.id.bal__ctv:
                replaceFragment(new Balance());
                break;
            case R.id.spt_cent_ctv:
                replaceFragment(new SupportCenterFragment());
                break;
            case R.id.ifsc_code_ctv:
                replaceFragment(new IfscCodeFinder());
                break;
            case R.id.upt_pmt_ctv:
                replaceFragment(new UpdatePaymentFragment());
                break;
            case R.id.mnyrmt_ctv:
                replaceFragment(new MoneyTransferWalletLoginFragment());
                break;
//////////////////////////click for recharge type

            case R.id.ins_rch_txv:
                sendToRechargeFragment("insurance");
                break;

            case R.id.gas_rch_txv:
                sendToRechargeFragment("gas");

                break;

            case R.id.eltc_rch_txv:
                sendToRechargeFragment("electricity");

                break;

            case R.id.dtc_rch_txv:
                sendToRechargeFragment("datacard");

                break;
            case R.id.pst_dtc_rch_txv:
                sendToRechargeFragment("datacard");
                break;
            case R.id.pstrch_txv:
                sendToRechargeFragment("postpaid");

                break;

            case R.id.dthrch_txv:
                sendToRechargeFragment("dth");
                break;

            case R.id.mobilerch_txv:
                sendToRechargeFragment("mobile");
                break;


/////////////////////////////////////////////////////
        }
    }

    private void sendToRechargeFragment(String title) {
        Fragment fragment = new RechargeFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putSerializable("title", title);
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.container, fragment).addToBackStack("one");
        fragmentTransaction.commit();
    }
//
    public void sendToHistoryFragment(String hist_type) {
        Fragment fragment = new Historyfragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putSerializable("hist_type", hist_type);
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.container, fragment).addToBackStack("one");
        fragmentTransaction.commit();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.container, fragment).addToBackStack("one");
        fragmentTransaction.commit();
    }
}
