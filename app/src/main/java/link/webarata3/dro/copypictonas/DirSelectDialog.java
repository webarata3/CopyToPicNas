package link.webarata3.dro.copypictonas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
    *http://alldaysyu-ya.blogspot.jp/2013/09/android_13.html<br>
    *から一部変更<br>
    *ディレクトリ選択ダイアログ
    */
public class DirSelectDialog extends Activity implements DialogInterface.OnClickListener {
    /** アクティビティ*/
    private Activity activity = null;

    /** リスナー */
    private OnDirSelectDialogListener listener = null;

    /** ファイル情報 */
    private File fileData = null;

    /** 表示中のファイル情報リスト */
    private List<File> viewFileDataList = null;

    /**
     * コントラクト
     *
     * @param activity アクティビティ
     */
    public DirSelectDialog(Activity activity) {
        this.activity = activity;
    }

    /**
     * 選択イベント
     *
     * @param dialog ダイアログ
     * @param which  選択位置
     */
    @Override
    public void onClick(DialogInterface dialog, int which) {
        show(this.viewFileDataList.get(which).getAbsolutePath() + "/");
    }

    /**
     * ダイアログを表示
     *
     * @param dirPath ディレクトリのパス
     */
    public void show(final String dirPath) {
        this.fileData = new File(dirPath);
        File[] fileArray = this.fileData.listFiles();
        List<String> nameList = new ArrayList<String>();
        if (fileArray != null) {
            Map<String, File> map = new HashMap<String, File>();
            for (File file : fileArray) {
                if (file.isDirectory()) {
                    nameList.add(file.getName() + "/");
                    map.put(nameList.get(map.size()), file);
                }
            }
            Collections.sort(nameList);
            this.viewFileDataList = new ArrayList<File>();
            for (int i = 0; i < nameList.size(); i++) {
                this.viewFileDataList.add(map.get(nameList.get(i)));
            }
        }

        AlertDialog.Builder dialog = new AlertDialog.Builder(this.activity);
        dialog.setTitle(dirPath);
        // dialog.setIcon(R.drawable.directory);
        dialog.setItems(nameList.toArray(new String[nameList.size()]), this);
        dialog.setPositiveButton("決定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int value) {
                DirSelectDialog.this.listener
                    .onClickDirSelect(DirSelectDialog.this.fileData);
            }
        });

        dialog.setNeutralButton("上へ", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int value) {
                if (!"/".equals(dirPath)) {
                    String dirPathNew = dirPath.substring(0,
                        dirPath.length() - 1);
                    dirPathNew = dirPathNew.substring(0,
                        dirPathNew.lastIndexOf("/") + 1);
                    // 1つ上へ
                    show(dirPathNew);
                } else {
                    // 現状維持
                    show(dirPath);
                }
            }
        });

        dialog.setNegativeButton("キャンセル",
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int value) {
                    DirSelectDialog.this.listener.onClickDirSelect(null);
                }
            }
        );

        dialog.show();
    }

    /**
     * リスナーを設定
     *
     * @param listener 選択イベントリスナー
     */
    public void setOnDirSelectDialogListener(OnDirSelectDialogListener listener) {
        this.listener = listener;
    }

    /**
     * ボタン押下インターフェース
     */
    public interface OnDirSelectDialogListener {
        /**
         * 選択イベント
         *
         * @param file ファイル
         */
        public void onClickDirSelect(File file);
    }
}
