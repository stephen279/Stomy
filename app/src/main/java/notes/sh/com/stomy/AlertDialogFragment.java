package notes.sh.com.stomy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by stephenholland on 03/06/2016.
 */
public class AlertDialogFragment extends DialogFragment {

    //fragment simiialr to activity


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Context context = getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(R.string.error_title)
                .setMessage(R.string.error_message)
                .setPositiveButton(R.string.error_ok_button, null);

    AlertDialog dialog = builder.create();
        return dialog;
    }


}
