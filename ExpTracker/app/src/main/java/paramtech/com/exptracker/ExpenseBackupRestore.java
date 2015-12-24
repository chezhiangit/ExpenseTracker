package paramtech.com.exptracker;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupDataOutput;
import android.app.backup.FileBackupHelper;
import android.content.Context;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by 324590 on 12/21/2015.
 */
public class ExpenseBackupRestore extends BackupAgentHelper {
    public static final String EXPENSE_DATABASE_NAME = "ExpenseTracker";

    @Override
    public void onCreate() {
        DbBackupHelper dbs = new DbBackupHelper(this, EXPENSE_DATABASE_NAME);
        addHelper("ExpenseTrackerKey", dbs);
    }

    @Override
    public File getFilesDir() {
        File path = getDatabasePath(EXPENSE_DATABASE_NAME);
        return path.getParentFile();
    }

    public class DbBackupHelper extends FileBackupHelper {

        public DbBackupHelper(Context ctx, String dbName) {
            super(ctx, ctx.getDatabasePath(dbName).getAbsolutePath());
        }
    }

    @Override
    public void onBackup(ParcelFileDescriptor oldState, BackupDataOutput data, ParcelFileDescriptor newState) throws IOException {
        super.onBackup(oldState, data, newState);
        Log.d("ExpTracker App", "Backup Expense Database");
    }

    @Override
    public void onRestore(BackupDataInput data, int appVersionCode, ParcelFileDescriptor newState) throws IOException {
        super.onRestore(data, appVersionCode, newState);
        Log.d("ExpTracker App", "Restore Expense Database");
    }

    @Override
    public void onRestoreFile(ParcelFileDescriptor data, long size, File destination, int type, long mode, long mtime) throws IOException {
        super.onRestoreFile(data, size, destination, type, mode, mtime);
        Log.d("ExpTracker App", "Restore Expense Database - File");
    }
    //    @Override
//    public void onBackup(ParcelFileDescriptor oldState, BackupDataOutput data,
//                         ParcelFileDescriptor newState) throws IOException {
//        // Hold the lock while the FileBackupHelper performs backup
//        synchronized (MyActivity.sDataLock) {
//            super.onBackup(oldState, data, newState);
//        }
//    }
//
//    @Override
//    public void onRestore(BackupDataInput data, int appVersionCode,
//                          ParcelFileDescriptor newState) throws IOException {
//        // Hold the lock while the FileBackupHelper restores the file
//        synchronized (MyActivity.sDataLock) {
//            super.onRestore(data, appVersionCode, newState);
//        }
//    }

}
