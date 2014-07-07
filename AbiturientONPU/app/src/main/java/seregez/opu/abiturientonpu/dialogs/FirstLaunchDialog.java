
package seregez.opu.abiturientonpu.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import seregez.opu.abiturientonpu.R;


public class FirstLaunchDialog extends DialogFragment implements DialogInterface.OnClickListener{

    private Activity activity;

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public FirstLaunchDialog() {
        super();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Добро пожаловать! ")
               .setMessage(R.string.welcome_to_program)
               .setPositiveButton(R.string.dialog_continue,this);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        Log.d("TAG++++++++++",String.valueOf(which));
//        Intent settings = new Intent(activity.getBaseContext(),PreferencesActivity.class);
//        startActivity(settings);
    }
}



