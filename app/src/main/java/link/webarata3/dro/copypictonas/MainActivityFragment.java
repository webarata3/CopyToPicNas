package link.webarata3.dro.copypictonas;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.Date;

import link.webarata3.dro.copypictonas.util.FileSizeUtil;

public class MainActivityFragment extends Fragment
    implements View.OnClickListener, SelectDirDialogFragment.SelectDirListener {

    private TextInputLayout ipTextInputLayout;
    private AppCompatEditText ipEditText;
    private TextInputLayout toDirTextInputLayout;
    private AppCompatEditText toDirEditText;

    private TextInputLayout userIdTextLayoutInput;
    private AppCompatEditText userIdEditText;
    private TextInputLayout passwordTextInputLayout;
    private AppCompatEditText passwordEditText;

    private TextInputLayout fromDirTextInputLayout;
    private AppCompatEditText fromDirEditText;
    private AppCompatButton selectDirButton;
    private AppCompatButton copyButton;
    private AppCompatTextView dirInfo;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_main, container, false);

        ipTextInputLayout = (TextInputLayout) fragment.findViewById(R.id.ipTextInputLayout);
        ipEditText = (AppCompatEditText) fragment.findViewById(R.id.ipEditText);
        toDirTextInputLayout = (TextInputLayout) fragment.findViewById(R.id.toDirTextInputLayout);
        toDirEditText = (AppCompatEditText) fragment.findViewById(R.id.toDirEditText);
        userIdTextLayoutInput = (TextInputLayout) fragment.findViewById(R.id.userIdTextLayoutInput);
        userIdEditText = (AppCompatEditText) fragment.findViewById(R.id.userIdEditText);
        passwordTextInputLayout = (TextInputLayout) fragment.findViewById(R.id.passowrdTextInputLayout);
        passwordEditText = (AppCompatEditText) fragment.findViewById(R.id.passwordEditText);
        fromDirTextInputLayout = (TextInputLayout) fragment.findViewById(R.id.fromDirTextInputLayout);
        fromDirEditText = (AppCompatEditText) fragment.findViewById(R.id.fromDirEditText);

        dirInfo = (AppCompatTextView) fragment.findViewById(R.id.dirInfo);

        fragment.findViewById(R.id.selectDirButton).setOnClickListener(this);
        fragment.findViewById(R.id.copyButton).setOnClickListener(this);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.selectDirButton:
                selectDirButton();
                break;
            case R.id.copyButton:
                String timestamp = DateFormat.format("yyyyMMddHHmmss", new Date())
                    .toString();
                String serverPath = ipEditText.getText().toString();
                if (!serverPath.endsWith("/")) {
                    serverPath = serverPath + "/";
                }
                CopySetting cs = new CopySetting(fromDirEditText.getText().toString(),
                    serverPath + timestamp + "/", userIdEditText.getText()
                    .toString(), passwordEditText.getText().toString()
                );
                RefTask refTask = new RefTask(view.getContext(), cs);
                refTask.setFileSize((int) (calcDirSize() / 1024));
                refTask.execute();

                break;
        }
    }

    private void selectDirButton() {
        SelectDirDialogFragment selectDirDialogFragment = SelectDirDialogFragment.newInstance(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath());
        selectDirDialogFragment.setListener(this);
        selectDirDialogFragment.show(getFragmentManager(), "");
    }

    private Long calcDirSize() {
        File file = new File(fromDirEditText.getText().toString());

        if (!file.exists()) {
            fromDirEditText.setText("ディレクトリが存在しません");
            return 0L;
        }
        if (!file.isDirectory()) {
            fromDirEditText.setText("ディレクトリではありません");
            return 0L;
        }

        File[] files = file.listFiles();
        int fileCount = 0;
        long fileSize = 0;
        for (File localFile : files) {
            if (!localFile.isDirectory()) {
                fileCount++;
                try {
                    fileSize += localFile.length();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        dirInfo.setText(String.format("ファイル数: %1$d サイズ: %2$s",
            fileCount, FileSizeUtil.getFileSizeForView(fileSize)));

        return fileSize;
    }

    @Override
    public void onSelect(@NonNull String dir) {
        Log.i("##########", dir);
    }
}
