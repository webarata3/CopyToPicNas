package link.webarata3.dro.copypictonas;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;

import link.webarata3.dro.copypictonas.util.FileUtil;

public class MainActivityFragment extends Fragment
    implements SelectDirDialogFragment.SelectDirListener {

    private AppCompatTextView fromDirTextView;
    private AppCompatButton selectDirButton;
    private AppCompatTextView dirInfo;

    private TextInputLayout ipTextInputLayout;
    private TextInputEditText ipEditText;
    private TextInputLayout toDirTextInputLayout;
    private TextInputEditText toDirEditText;

    private TextInputLayout userIdTextLayoutInput;
    private TextInputEditText userIdEditText;
    private TextInputLayout passwordTextInputLayout;
    private TextInputEditText passwordEditText;

    private AppCompatButton copyButton;

    private OnFragmentInteractionListener onFragmentInteractionListener;

    public MainActivityFragment() {
    }

    public interface OnFragmentInteractionListener {
        void onClickSelectDirButton();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_main, container, false);

        fromDirTextView = (AppCompatTextView) fragment.findViewById(R.id.fromDirTextView);

        dirInfo = (AppCompatTextView) fragment.findViewById(R.id.dirInfo);

        ipTextInputLayout = (TextInputLayout) fragment.findViewById(R.id.ipTextInputLayout);
        ipEditText = (TextInputEditText) fragment.findViewById(R.id.ipEditText);
        toDirTextInputLayout = (TextInputLayout) fragment.findViewById(R.id.toDirTextInputLayout);
        toDirEditText = (TextInputEditText) fragment.findViewById(R.id.toDirEditText);
        userIdTextLayoutInput = (TextInputLayout) fragment.findViewById(R.id.userIdTextLayoutInput);
        userIdEditText = (TextInputEditText) fragment.findViewById(R.id.userIdEditText);
        passwordTextInputLayout = (TextInputLayout) fragment.findViewById(R.id.passowrdTextInputLayout);
        passwordEditText = (TextInputEditText) fragment.findViewById(R.id.passwordEditText);

        fragment.findViewById(R.id.selectDirButton).setOnClickListener(view -> {
            onFragmentInteractionListener.onClickSelectDirButton();
        });


        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            onFragmentInteractionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onFragmentInteractionListener = null;
    }

    public void onClickSelectDirButton() {
        String fromDir = fromDirTextView.getText().toString();
        if (fromDir.isEmpty()) {
            fromDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        }
        SelectDirDialogFragment selectDirDialogFragment = SelectDirDialogFragment.newInstance(fromDir);
        selectDirDialogFragment.setListener(this);
        selectDirDialogFragment.show(getFragmentManager(), "");
    }

    private Long calcDirSize() {
        File file = new File(fromDirTextView.getText().toString());

        if (!file.exists()) {
            fromDirTextView.setText("ディレクトリが存在しません");
            return 0L;
        }
        if (!file.isDirectory()) {
            fromDirTextView.setText("ディレクトリではありません");
            return 0L;
        }

        File[] files = file.listFiles();
        int fileCount = 0;
        long fileSize = 0;
        for (File localFile : files) {
            Log.d("### Files", localFile.getName());
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
            fileCount, FileUtil.getDisplayFileSize(fileSize)));

        return fileSize;
    }

    @Override
    public void onSelect(@NonNull String dir) {
        fromDirTextView.setText(dir);
        calcDirSize();
    }
}
