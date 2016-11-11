package link.webarata3.dro.copypictonas;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SelectDirDialogFragment extends DialogFragment  {
    private static final String CURRENT_DIR = "current_dir";

    private ArrayAdapter<String> adapter;

    public static SelectDirDialogFragment newInstance(Fragment target, String initDir) {
        SelectDirDialogFragment fragment = new SelectDirDialogFragment();

        Bundle args = new Bundle();
        args.putString(CURRENT_DIR, initDir);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        String currentDir = args.getString(CURRENT_DIR);

        ListView listView = new ListView(getActivity());
        this.adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
        listView.setAdapter(this.adapter);

        updateView();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(currentDir)
            .setView(listView)
            .setPositiveButton("決定", (dialog, value) -> {

            })
//            .setNeutralButton("上の階層", (dialog, value) -> {
//                getDialog().setTitle("こｙらあ");
//            })
            .setNegativeButton("キャンセル", (dialog, value) -> {
            });

        return builder.create();
    }

    private void updateView() {
        adapter.clear();

        Bundle bundle = this.getArguments();
        String currentDir = bundle.getString(CURRENT_DIR);

        // <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />をつけないと動かない
        File[] fileList = new File(currentDir).listFiles();

        List<String> fileNameList = new ArrayList<>();
        adapter.add("..（上の階層）");
        for (String fileName : fileNameList) {
            adapter.add(fileName);
        }
    }
}
