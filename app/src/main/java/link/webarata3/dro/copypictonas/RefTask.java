package link.webarata3.dro.copypictonas;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;

public class RefTask extends AsyncTask<String, Integer, Long> {
    private Context context;
    private CopySetting cs;
    private ProgressDialog dialog;
    private Integer totalSize;
    private Long transferSize;

    public RefTask(Context context, CopySetting cs) {
        this.context = context;
        this.cs = cs;
        transferSize = 0L;
    }

    public void setFileSize(@NonNull Integer fileSize) {
        totalSize = fileSize;
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(context);
        dialog.setTitle("コピータイトル");
        dialog.setMessage("コピーメッセージ");
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setMax(0);
        dialog.setProgress(0);
//        dialog.show();
    }

    @Override
    protected Long doInBackground(String... params) {
        try {
            File dir = new File(cs.getLocalPath());

            if (!dir.isDirectory()) {
                return null;
            }
            File[] files = dir.listFiles();
            dialog.setMax(totalSize);

            SmbFile writeDir = new SmbFile("smb://" + cs.getServerPath(),
                new NtlmPasswordAuthentication(null, cs.getServerId(),
                    cs.getServerPassword())
            );
            if (!writeDir.exists()) {
                writeDir.mkdirs();
            }

            for (File f : files) {
                if (!f.isFile()) {
                    continue;
                }

                SmbFile file = new SmbFile(writeDir.getCanonicalPath(),
                    f.getName(), new NtlmPasswordAuthentication(null,
                    cs.getServerId(), cs.getServerPassword())
                );

                if (file.exists()) {
                    continue;
                }
                copy(f, file);
                publishProgress((int) (transferSize / 1024));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(@NonNull Integer... progress) {
        dialog.setProgress(progress[0]);
    }

    @Override
    protected void onPostExecute(@NonNull Long result) {
        dialog.dismiss();
    }

    private void copy(@NonNull File inputFile, @NonNull SmbFile outputFile) throws IOException {
        InputStream is = new FileInputStream(inputFile);
        OutputStream os = outputFile.getOutputStream();

        byte[] buf = new byte[8192];
        try {
            int len;
            int count = 0;
            while ((len = is.read(buf, 0, buf.length)) != -1) {
                transferSize = transferSize + (long) len;
                if (count == 100) {
                    publishProgress((int) (transferSize / 1024));
                    count = 0;
                }
                count++;
                os.write(buf, 0, len);
            }
            os.flush();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                // ignore
            }
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                // ignore
            }
        }
    }
}
