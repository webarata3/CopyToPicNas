package link.webarata3.dro.copypictonas;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

// 参考: http://www.milk-island.net/document/android/fileselectdialog/
public class SelectDirDialogFragment extends DialogFragment implements AdapterView.OnItemClickListener {
    private static final String CURRENT_DIR = "current_dir";

    private ArrayAdapter<String> adapter;
    private File[] fileList;

    private SelectDirListener listener;

    public interface SelectDirListener {
        void onSelect(@NonNull String dir);
    }

    public static SelectDirDialogFragment newInstance(@NonNull String initDir) {
        SelectDirDialogFragment fragment = new SelectDirDialogFragment();

        Bundle args = new Bundle();
        // ディレクトリの正規化（/以外で、最後が/であれば/を取る）
        if (!initDir.equals(File.pathSeparator)
            && initDir.lastIndexOf(File.pathSeparator) == initDir.length()) {
            initDir = initDir.substring(0, initDir.lastIndexOf(File.pathSeparator));
        }

        args.putString(CURRENT_DIR, initDir);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ListView listView = new ListView(getActivity());
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        updateView();

        Bundle args = getArguments();
        String currentDir = args.getString(CURRENT_DIR);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
            .setTitle(currentDir)
            .setView(listView)
            .setPositiveButton("決定", (dialog, value) -> {
                Bundle resultArgs = getArguments();
                String resultDir = args.getString(CURRENT_DIR);
                Objects.requireNonNull(resultDir);
                listener.onSelect(resultDir);
            })
            .setNegativeButton("キャンセル", (dialog, value) -> {
            });

        return builder.create();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle args = getArguments();
        String dir = args.getString(CURRENT_DIR);
        Objects.requireNonNull(dir);

        Dialog dialog = getDialog();

        if (position == 0) {
            // 一番上を選択した場合
            if (!dir.equals("/")) {
                // 通常は戻る処理をする
                dir = dir.substring(0, dir.lastIndexOf(File.separator));
                // 空のディレクトリになった場合にはルートディレクトリに変更する
                if (dir.equals("")) {
                    dir = dir + File.separator;
                }
                // 画面回転で状態を復元するため、状態をBundleに保存しておく。
                args.putString(CURRENT_DIR, dir);
                dialog.setTitle(dir);

                updateView();
            }
        } else {
            if (!dir.equals("/")) {
                dir = dir + File.separator;
            }
            dir = dir + fileList[position - 1].getName();
            File file = new File(dir);
            if (file.isDirectory()) {
                // ディレクトリの場合はその中へ移動
                // 画面回転で状態を復元するため、状態をBundleに保存しておく。
                args.putString(CURRENT_DIR, dir);
                dialog.setTitle(dir);

                updateView();
            }
        }
    }

    private void updateView() {
        adapter.clear();

        Bundle args = getArguments();
        String currentDir = Objects.requireNonNull(args.getString(CURRENT_DIR));

        // <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />をつけないと動かない
        // 読み込み可能ファイル／フォルダーのみ追加する
        File[] allFileList = new File(currentDir).listFiles();
        List<File> readableFileList = new ArrayList<File>();
        Log.i("#########", currentDir + " : " + allFileList + "");
        if (allFileList != null) {
            for (File file : allFileList) {
                if (!file.isDirectory()) continue;
                Log.i("#+++", file.getName());
               // if (!file.canRead()) continue;
                Log.i("#+++", "readable");
                //if (!file.canExecute()) continue;
                readableFileList.add(file);
            }
        }
        fileList = readableFileList.toArray(new File[readableFileList.size()]);

        Arrays.sort(fileList, (o1, o2) -> o1.getName().compareTo(o2.getName()));

        adapter.add("..（上の階層）");
        for (File file : fileList) {
            adapter.add(file.getName());
        }
    }

    public void setListener(@NonNull SelectDirListener listener) {
        this.listener = listener;
    }

    public void removeListener() {
        listener = null;
    }
}
