package link.webarata3.dro.copypictonas;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;

public class MainActivityFragment extends Fragment implements View.OnClickListener, DirSelectDialog.OnDirSelectDialogListener {

    AppCompatButton selectDir;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_main, container, false);
        AppCompatButton selectDir = (AppCompatButton) fragment.findViewById(R.id.selectDir);
        selectDir.setOnClickListener(this);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.selectDir:
                DirSelectDialog dialog = new DirSelectDialog(this.getContext());
                dialog.setOnDirSelectDialogListener(this);

                // 表示
                dialog.show(Environment.getExternalStorageDirectory().getPath());
                break;
        }
    }

    @Override
    public void onClickDirSelect(File file) {

    }
}
