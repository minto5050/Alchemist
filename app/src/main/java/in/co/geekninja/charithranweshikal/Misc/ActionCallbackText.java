package in.co.geekninja.charithranweshikal.Misc;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import in.co.geekninja.charithranweshikal.R;

/**
 * Created by PS on 3/1/2016.
 */
public class ActionCallbackText implements ActionMode.Callback {
    private final View bodyView;
    private final Context context;

    public ActionCallbackText(View bodyView,Context context) {
        this.bodyView=bodyView;
        this.context=context;
    }


    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
        menu.removeItem(android.R.id.selectAll);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

        int start = ((TextView)bodyView).getSelectionStart();
        int end = ((TextView)bodyView).getSelectionEnd();


        switch(item.getItemId()) {

            case R.id.all:
                ((EditText)bodyView).selectAll();
                return true;

            case R.id.copy:
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                String text=((EditText)bodyView).getText().subSequence(start,end).toString();
                ClipData data=ClipData.newPlainText("Text",text);
                clipboard.setPrimaryClip(data);
                Toast.makeText(context,"Copied",Toast.LENGTH_SHORT).show();
                mode.finish();
                return true;

            case R.id.share:
                String textShare=((EditText)bodyView).getText().subSequence(start,end).toString();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, textShare);
                sendIntent.setType("text/plain");
                context.startActivity(sendIntent);
                mode.finish();
                return true;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }
}
