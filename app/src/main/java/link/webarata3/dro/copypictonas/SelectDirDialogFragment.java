package link.webarata3.dro.copypictonas;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SelectDirDialogFragment extends DialogFragment {
    private static final String CURRENT_DIR = "current_dir";

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

        // <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />をつけないと動かない
        File[] fileList = new File(currentDir).listFiles();

        List<String> fileNameList = new ArrayList<>();
        fileNameList.add("..（上の階層）");
        for (File file : fileList) {
            if (file.isFile()) continue;
            String fileName = file.getName();
            fileNameList.add(fileName);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(currentDir)
            .setItems(fileNameList.toArray(new String[fileNameList.size()]), (dialog, value) -> {
                getDialog().setTitle("あいうえお");
            })
            .setPositiveButton("決定", (dialog, value) -> {

            })
//            .setNeutralButton("上の階層", (dialog, value) -> {
//                getDialog().setTitle("こｙらあ");
//            })
            .setNegativeButton("キャンセル", (dialog, value) -> {
            });

        return builder.create();
    }
}
