package fr.kosdev.realestatemanager.Controllers.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.kosdev.realestatemanager.R;

public class LoanSimulatorActivity extends AppCompatActivity {

    @BindView(R.id.loan_amount_txt)
    TextInputEditText loanAmount;
    @BindView(R.id.loan_period_txt)
    TextInputEditText loanPeriod;
    @BindView(R.id.interest_txt)
    TextInputEditText interestRate;
    @BindView(R.id.calculation_btn)
    MaterialButton calculationButton;
    @BindView(R.id.monthly_txt)
    TextInputEditText monthlyPayment;
    @BindView(R.id.total_interest_txt)
    TextInputEditText totalInterest;
    @BindView(R.id.total_payment_txt)
    TextInputEditText totalPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_simulator);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.calculation_btn)
    public void calculateLoan(){

        String la = loanAmount.getText().toString();
        String lp = loanPeriod.getText().toString();
        String ir = interestRate.getText().toString();

        if (Integer.parseInt(la) >= 10000 && Integer.parseInt(la) <= 50000){
             interestRate.setText(String.valueOf(2));
        }

        if (TextUtils.isEmpty(la)){
            loanAmount.setError(getResources().getString(R.string.error_loan_amount));
            loanAmount.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(lp)){
            loanPeriod.setError(getResources().getString(R.string.error_loan_period));
            loanPeriod.requestFocus();
            return;
        }
        double amount = Double.parseDouble(la);
        double period = Double.parseDouble(lp);
        double rate = Double.parseDouble(ir);
        double r = rate/1200;
        double r1 =  Math.pow(r+1, period);
        double emi = amount * (r+(r/(r1-1)));
        emi = Math.round(emi*100.0) / 100.0;
        double tp = emi * period;
        double ti = tp - amount ;
        monthlyPayment.setText(String.valueOf(emi) );
        monthlyPayment.setVisibility(View.VISIBLE);
        totalInterest.setText(String.valueOf(ti));
        totalInterest.setVisibility(View.VISIBLE);
        totalPayment.setText(String.valueOf(tp));
        totalPayment.setVisibility(View.VISIBLE);
    }
}