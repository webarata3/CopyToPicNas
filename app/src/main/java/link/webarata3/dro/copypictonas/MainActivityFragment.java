package link.webarata3.dro.copypictonas;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;

import link.webarata3.dro.copypictonas.service.FileSizeIntentService;
import link.webarata3.dro.copypictonas.service.FileSizeResultReceiver;
import link.webarata3.dro.copypictonas.util.FileUtil;

public class MainActivityFragment extends Fragment
    implements SelectDirDialogFragment.SelectDirListener,
    FileSizeResultReceiver.Receiver {

    private AppCompatTextView fromDirTextView;
    private AppCompatButton selectDirButton;
    private AppCompatTextView dirInfoTextView;

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

    private FileSizeResultReceiver receiver;

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

        dirInfoTextView = (AppCompatTextView) fragment.findViewById(R.id.dirInfoTextView);

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

        receiver = new FileSizeResultReceiver(new Handler());
        receiver.setReceiver(this);

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

    @Override
    public void onFileSizeResult(int resultCode, Bundle resultData) {
        int fileCount = resultData.getInt("fileCount");
        long fileSize = resultData.getLong("fileSize");

        dirInfoTextView.setText(String.format("ファイル数: %1$d サイズ: %2$s",
            fileCount, FileUtil.getDisplayFileSize(fileSize)));
    }

    private void calcDirSize() {
        File dir = new File(fromDirTextView.getText().toString());

        if (!dir.exists()) {
            dirInfoTextView.setText("ディレクトリが存在しません");
            return;
        }
        if (!dir.isDirectory()) {
            dirInfoTextView.setText("ディレクトリではありません");
            return;
        }

        dirInfoTextView.setText("ディレクトリーサイズ計算中");

        Intent intent = new Intent(getActivity(), FileSizeIntentService.class);
        intent.putExtra("receiver", receiver);
        intent.putExtra("dirName", fromDirTextView.getText().toString());

        getActivity().startService(intent);
    }

    @Override
    public void onSelect(@NonNull String dir) {
        fromDirTextView.setText(dir);
        calcDirSize();
    }
}
