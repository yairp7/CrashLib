package com.pech.crashlibtester;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.pech.crashlib.CrashLibException;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.uncaughtBtn)
    public void onUncaughtExceptionBtnClicked()
    {
        float bd = 235 / 0;
    }

    @OnClick(R.id.caughtBtn)
    public void onCaughtExceptionBtnClicked()
    {
        try
        {
            throw new CrashLibException("Caught");
        }
        catch(CrashLibException e)
        {
            e.logException();
        }
    }
}
