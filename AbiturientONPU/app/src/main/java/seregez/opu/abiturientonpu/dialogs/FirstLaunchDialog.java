
package seregez.opu.abiturientonpu.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import seregez.opu.abiturientonpu.R;



public class FirstLaunchDialog extends DialogFragment{

    private Activity activity;

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public FirstLaunchDialog(Activity activity) {
        this.activity = activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Добро пожаловать! ")
               .setMessage(R.string.welcome_to_program);
        return builder.create();
    }

}



